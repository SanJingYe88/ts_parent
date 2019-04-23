package com.ts.user.service;

import com.ts.user.dao.UserDao;
import com.ts.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;
import util.MobileUtils;
import util.MyStringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 服务层
 */
@Slf4j
@Transactional
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 根据手机号和密码登录
     * @param mobile
     * @param password
     * @return
     */
    public User login(String mobile, String password) {
        if (MyStringUtils.isNullOrEmpty(mobile) || MyStringUtils.isNullOrEmpty(password)){
            throw new RuntimeException("手机号或者密码为空");
        }
        if (!MobileUtils.checkMobile(mobile)){
            throw new RuntimeException("手机号码或者密码错误!");
        }
        //查询用户
        User user = userDao.findByMobileIs(mobile);
        if (user == null){
            throw new RuntimeException("该手机号码还没有注册!");
        }
        //密码判断
        boolean flag = bCryptPasswordEncoder.matches(password,user.getPassword());
        if (!flag){
            throw new RuntimeException("手机号码或者密码错误!");
        }
        //更新登陆时间
        userDao.updateLastdate(user.getId());
        return user;
    }

    /**
     * 用户注册
     *
     * @param user 用户
     * @param code 用户填写的验证码
     */
    public void register(User user, String code) {
        if (MyStringUtils.isNullOrEmpty(user.getPassword())) {
            throw new RuntimeException("密码不能为空");
        }
        log.info("code={}", code);
        // 验证码格式是否正确,必须为6位数字
        if (code == null || !Pattern.matches("^[0-9]{6}$", code)) {
            throw new RuntimeException("短信验证码错误");
        }

        //判断验证码是否正确
        String key = "sms:mobile:" + user.getMobile();
        String codeInRedis = stringRedisTemplate.opsForValue().get(key);
        log.info("codeInRedis={}", codeInRedis);

        if (codeInRedis == null) {
            throw new RuntimeException("请获取短信验证码");
        }

        if (!code.equals(codeInRedis)) {
            //删除本次的短信验证码,后面参照其他APP注册时的实际情况,发现当用户输错验证码时删除是没必要的,
            //因为用户输错验证码的情况很常见,每次输错都重发短信验证码的话,用户就绝对很烦,而且会额外增加服务器带宽,
            //还要消耗短信验证码的发送次数,增加了成本,所以注释掉删除的代码.
            //stringRedisTemplate.delete(key);
            throw new RuntimeException("短信验证码错误");
        }
        //注册成功后,删除本次的短信验证码,也可以让其自动失效
        stringRedisTemplate.delete(key);
        user.setId(idWorker.nextId() + "");
        user.setFollowcount(0);//关注数
        user.setFanscount(0);//粉丝数
        user.setOnline(0L);//在线时长
        user.setRegdate(new Date());//注册日期
        user.setUpdatedate(new Date());//更新日期
        user.setLastdate(new Date());//最后登陆日期
        //密码加密
        String newPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(newPassword);
        userDao.save(user);
    }

    /**
     * 发送短信验证码
     *
     * @param mobile
     */
    public void sendSms(String mobile) {
        //1.生成6位短信验证码
        Random random = new Random();
        int max = 999999;//最大数
        int min = 100000;//最小数
        int code = random.nextInt(max);//随机生成
        if (code < min) {
            code = code + min;
        }
        log.info("mobile={},收到短信验证码={}", mobile, code);

        //存放验证码到 redis
        //如果该key存在,就更新其值.(一个用户已经请求过验证码,这是第二次请求验证码,所以将其key对应的value重新设置)
        //set() 方法可以实现如果有旧值就覆盖
        stringRedisTemplate.opsForValue().set("sms:mobile:" + mobile, code + "", 5, TimeUnit.MINUTES);    //5分钟过期
        //同时存放一个key,代表该mobile已近发送过验证码了,这个key的有效期是1分钟.
        stringRedisTemplate.opsForValue().set("sms:send:" + mobile,"1",5, TimeUnit.MINUTES);    //1分钟过期

        //将验证码和手机号发送给rabbitmq
        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("code", code + "");
        rabbitTemplate.convertAndSend("sms", map);
    }

    /**
     * 检测用户是否可以发送验证码.
     * 控制每个用户 每分钟只能发送一条 短信验证码,避免恶意攻击.
     * @param mobile
     * @return
     */
    public boolean checkUserCanSendSms(String mobile){
        //检测手机号是否合法
        if (mobile == null || mobile.length() != 11 || !MobileUtils.checkMobile(mobile)) {
            throw new RuntimeException("手机号码错误!");
        }
        //从redis获取该key,看是否存在,如果存在,则表示暂时不能发送验证码,否则可以发送验证码.
        return stringRedisTemplate.opsForValue().get("sms:send:" + mobile) == null;
    }


    /**
     * 检测该手机号是否已经被注册
     *
     * @param mobile 手机号
     * @return true-已被注册,false-还未注册
     */
    public boolean checkMobileIsRegistered(String mobile) {
        //检测手机号是否合法
        if (mobile == null || mobile.length() != 11 || !MobileUtils.checkMobile(mobile)) {
            throw new RuntimeException("手机号码错误!");
        }

        if (userDao.countByMobileIs(mobile) > 0) {  //该手机号已被注册
            return true;
        }
        return false;
    }


    /**
     * 查询全部列表
     *
     * @return
     */
    public List<User> findAll() {
        return userDao.findAll();
    }


    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<User> findSearch(Map whereMap, int page, int size) {
        Specification<User> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return userDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     *
     * @param whereMap
     * @return
     */
    public List<User> findSearch(Map whereMap) {
        Specification<User> specification = createSpecification(whereMap);
        return userDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public User findById(String id) {
        //存在,返回user,否则返回Null
        return userDao.findById(id).orElse(null);
    }

    /**
     * 增加
     *
     * @param user
     */
    public void add(User user) {
        user.setId(idWorker.nextId() + "");
        userDao.save(user);
    }

    /**
     * 修改
     *
     * @param user
     */
    public User update(User user) {
        //1.先从数据库查询
        User userInDB = userDao.findById(user.getId()).orElse(null);
        if (userInDB == null){
            return null;
        }
        //2.更新部分字段
        if (MyStringUtils.isNullOrEmpty(user.getNickname())){
            userInDB.setNickname(user.getNickname());
        }
        if(MyStringUtils.isNullOrEmpty(user.getPassword())){
            userInDB.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        if (MyStringUtils.isNullOrEmpty(user.getMobile())){
            userInDB.setNickname(user.getMobile());
        }
        if (MyStringUtils.isNullOrEmpty(user.getAvatar())){
            userInDB.setNickname(user.getAvatar());
        }
        if (MyStringUtils.isNullOrEmpty(user.getSex())){
            userInDB.setNickname(user.getSex());
        }
        //3.保存到数据库
        userDao.save(userInDB);
        return userInDB;
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        userDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<User> createSpecification(Map searchMap) {

        return (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            // ID ID不能修改
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
            }
            // 手机号码
            if (searchMap.get("mobile") != null && !"".equals(searchMap.get("mobile"))) {
                predicateList.add(cb.like(root.get("mobile").as(String.class), "%" + (String) searchMap.get("mobile") + "%"));
            }
            // 密码
            if (searchMap.get("password") != null && !"".equals(searchMap.get("password"))) {
                predicateList.add(cb.like(root.get("password").as(String.class), "%" + (String) searchMap.get("password") + "%"));
            }
            // 昵称
            if (searchMap.get("nickname") != null && !"".equals(searchMap.get("nickname"))) {
                predicateList.add(cb.like(root.get("nickname").as(String.class), "%" + (String) searchMap.get("nickname") + "%"));
            }
            // 性别
            if (searchMap.get("sex") != null && !"".equals(searchMap.get("sex"))) {
                predicateList.add(cb.like(root.get("sex").as(String.class), "%" + (String) searchMap.get("sex") + "%"));
            }
            // 头像
            if (searchMap.get("avatar") != null && !"".equals(searchMap.get("avatar"))) {
                predicateList.add(cb.like(root.get("avatar").as(String.class), "%" + (String) searchMap.get("avatar") + "%"));
            }
            // E-Mail
            if (searchMap.get("email") != null && !"".equals(searchMap.get("email"))) {
                predicateList.add(cb.like(root.get("email").as(String.class), "%" + (String) searchMap.get("email") + "%"));
            }
            // 兴趣
            if (searchMap.get("interest") != null && !"".equals(searchMap.get("interest"))) {
                predicateList.add(cb.like(root.get("interest").as(String.class), "%" + (String) searchMap.get("interest") + "%"));
            }
            // 个性
            if (searchMap.get("personality") != null && !"".equals(searchMap.get("personality"))) {
                predicateList.add(cb.like(root.get("personality").as(String.class), "%" + (String) searchMap.get("personality") + "%"));
            }

            return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
        };
    }

}
