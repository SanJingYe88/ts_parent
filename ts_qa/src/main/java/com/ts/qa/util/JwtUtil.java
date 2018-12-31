package com.ts.qa.util;

import exception.JwtParserException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

public class JwtUtil {

    private String key = "xxxxx";  //盐
    private Long ttl = 600000L;  //过期时间

    /**
     * 创建 token
     *
     * @param userId
     * @param username
     * @param roles
     * @return
     */
    public String createToken(String userId, String username, String roles) {
        Date now = new Date();
        JwtBuilder jwtBuilder = Jwts.builder().setId(userId)
                .setSubject(username)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, key)
                .claim("roles", roles);
        if (ttl > 0) {
            jwtBuilder.setExpiration(new Date(now.getTime() + ttl));
        }
        return jwtBuilder.compact();
    }

    /**
     * 解析 token
     *
     * @param token
     * @return
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw new JwtParserException("token出错");
        }

    }
}