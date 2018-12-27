package com.ts.user.test;/*
package com.ts.user.test;

import com.ts.user.pojo.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class Test {

    public static final String key = "hello";
    public static final int time = 30 * 60 * 1000;
//    public static final int time = 1000;

    public static void main(String[] args){
        User user = new User();
        user.setId("100002");
        user.setNickname("bbb");
        String token = create(user);
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        parse(token);
    }

    public static String create(User user){
        JwtBuilder jwtBuilder = Jwts.builder().setId(user.getId())  //用户id
                .setSubject(user.getNickname())     //用户名
                .setIssuedAt(new Date())    //登录时间
                .setExpiration(new Date(new Date().getTime() + time)) //设置过期时间(30分)
                .signWith(SignatureAlgorithm.HS256, key)    //指定编码方式, 盐
                .claim("","");      //添加自定义的属性
        String token = jwtBuilder.compact();
        log.info("{}",token);
        return token;
    }

    public static void parse(String token){
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        log.info("ID:{}",claims.getId());
        log.info("用户名:{}",claims.getSubject());
        log.info("登录时间:{}",claims.getIssuedAt());
        log.info("过期时间:{}",claims.getExpiration());
    }
}
*/
