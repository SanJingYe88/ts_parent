package com.ts.user.controller;

import com.ts.user.pojo.User;
import com.ts.user.service.UserService;
import com.ts.user.util.JwtUtil;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import sun.nio.cs.US_ASCII;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制器层
 */
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtUtil jwtUtil;


    /**
     * 登录
     * 该方法需要 token
     *
     * @param map 输入的用户名和密码
     * @return 登录成功, 返回 admin 对象
     */
    @PostMapping(value = "/login")
    public Result login(@RequestBody Map<String, String> map) {
        //根据手机号和密码登录
        User user = userService.login(map.get("mobile"), map.get("password"));
        //生成token
        String token = jwtUtil.createToken(user.getId(), user.getNickname(), "user");
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("nickname", user.getNickname());
        data.put("avatar", user.getAvatar());
        return new Result(true, StatusCode.OK, "登录成功", data);
    }

    /**
     * 用户注册, 验证码已经发送
     *
     * @param user 用户
     * @param code 用户填写的验证码
     * @return
     */
    @PostMapping(value = "/register/{code}")
    public Result register(@RequestBody User user, @PathVariable String code) {
        //添加用户
        userService.register(user, code);
        return new Result(true, StatusCode.OK, "注册成功");
    }

    /**
     * 用户注册,点击发送短信验证码
     *
     * @param mobile 手机号
     * @return
     */
    @GetMapping(value = "/sendSms/{mobile}")
    public Result sendSms(@PathVariable String mobile) {
        //检测该手机号是否已经被注册
        if (userService.checkMobileIsRegistered(mobile)) {
            return new Result(false, StatusCode.ERROR, "该手机号已经注册过了");
        }

        //检测用户是否可以发送验证码
        if (!userService.checkUserCanSendSms(mobile)){
            return new Result(false, StatusCode.ERROR, "验证码发送频繁,请稍后再试");
        }

        //发送验证码
        userService.sendSms(mobile);
        return new Result(true, StatusCode.OK, "短信验证码发送成功");
    }

    /**
     * 查询全部数据
     *
     * @return
     */
    @GetMapping
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", userService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @GetMapping(value = "/{id}")
    public Result findById(@PathVariable String id) {
        User user = userService.findById(id);
        if (user == null) {
            return new Result(true, StatusCode.NOT_FOUND, "查询成功");
        }
        return new Result(true, StatusCode.OK, "查询成功", user);
    }

    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<User> pageList = userService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @PostMapping(value = "/search")
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", userService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param user
     */
    @PostMapping
    public Result add(@RequestBody User user) {
        userService.add(user);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改用户信息
     * 该方法需要 token
     *
     * @param user
     */
    @PutMapping(value = "/{id}")
    public Result update(@RequestBody User user, @PathVariable String id) {
        //先检查权限
        checkPermission("user");
        user.setId(id);
        user = userService.update(user);
        if (user == null){
            return new Result(true, StatusCode.NOT_FOUND, "出错了");
        }
        //重新签发令牌
        String token = jwtUtil.createToken(user.getId(), user.getNickname(), "user");
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("nickname", user.getNickname());
        data.put("avatar", user.getAvatar());
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除指定用户
     *
     * @param id 用户id
     */
    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable String id) {
        //先检查权限
        checkPermission("admin");
        userService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 权限检查
     *
     * @param needRole 需要的角色
     */
    private void checkPermission(String needRole) {
        //获取角色
        String hasRole = (String) request.getAttribute("roles");
        log.info("hasRole={}", hasRole);
        if (hasRole == null || !hasRole.equals(needRole)) {
            throw new PermissionException("权限不足");
        }
    }
}
