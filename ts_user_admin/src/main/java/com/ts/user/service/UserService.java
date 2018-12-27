package com.ts.user.service;

import com.ts.user.dao.UserDao;
import com.ts.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
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
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

//    @Autowired
//    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

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
        User user = userDao.findByMobileIs(mobile);
        if (user == null){
            throw new RuntimeException("该手机号码还没有注册!");
        }
        boolean flag = bCryptPasswordEncoder.matches(password,user.getPassword());
        if (!flag){
            throw new RuntimeException("手机号码或者密码错误!");
        }
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
            //删除本次的短信验证码
            stringRedisTemplate.delete(key);
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
        String newpassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(newpassword);
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

        //TODO : 将验证码和手机号发送给rabbitmq
//        Map<String, String> map = new HashMap<>();
//        map.put("mobile", mobile);
//        map.put("code", code + "");
//        rabbitTemplate.convertAndSend("sms", map);
    }


    /**
     * 检测该手机号是否已经被注册
     *
     * @param mobile 手机号
     * @return 0-已被注册,1-还未注册
     */
    public int checkMobileIsRegistered(String mobile) {
        //检测手机号是否合法
        if (mobile == null || mobile.length() != 11 || !MobileUtils.checkMobile(mobile)) {
            throw new RuntimeException("手机号码错误!");
        }

        int num = userDao.countByMobileIs(mobile);
        if (num > 0) {  //该手机号已被注册
            return 0;
        }
        return 1;
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
        return userDao.findById(id).get();
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
    public void update(User user) {
        userDao.save(user);
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
    private Specification<User> createSpecification(final Map searchMap) {

        return new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
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
            }
        };
    }



}
