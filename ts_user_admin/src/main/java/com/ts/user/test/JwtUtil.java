package com.ts.user.test;/*
package com.ts.user.test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "jwt")
public class JwtUtil {

    private static String key;  //盐
    private static Long ttl;  //过期时间

    */
/**
     * 创建 token
     * @param userid
     * @param username
     * @param roles
     * @return
     *//*

    public static String createToken(String userid,String username,String roles){
        Date now = new Date();
        JwtBuilder jwtBuilder = Jwts.builder().setId(userid)
                .setSubject(username)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, JwtUtil.key)
                .claim("roles", roles);
        if (ttl > 0){
            jwtBuilder.setExpiration(new Date(now.getTime() + JwtUtil.ttl));
        }
        return jwtBuilder.compact();
    }

    */
/**
     * 解析 token
     * @param token
     * @return
     *//*

    public static Claims parseToken(String token){
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        return claims;
    }
}
*/
