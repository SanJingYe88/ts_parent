package com.ts.user.controller;

import com.ts.user.pojo.Admin;
import com.ts.user.pojo.User;
import com.ts.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 控制器层
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登录
     * @param map 输入的用户名和密码
     * @return 登录成功,返回 admin 对象
     */
    @PostMapping(value = "/login")
    public Result login(@RequestBody Map<String,String> map){
        //根据手机号和密码登录
        User user = userService.login(map.get("mobile"),map.get("password"));
        return new Result(true,StatusCode.OK,"登录成功",user);
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
        int result = userService.checkMobileIsRegistered(mobile);

        if (result == 0){
            return new Result(false, StatusCode.ERROR, "该手机号已经注册过了");
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
        return new Result(true, StatusCode.OK, "查询成功", userService.findById(id));
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
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<User>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", userService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param user
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody User user) {
        userService.add(user);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param user
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody User user, @PathVariable String id) {
        user.setId(id);
        userService.update(user);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        userService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

}
