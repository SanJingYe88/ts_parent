package com.ts.user.interceptor;

import com.ts.user.util.JwtUtil;
import exception.PermissionException;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("拦截器 - preHandle");
        //有些请求是不需要头信息 Authorization 的, 所以即使在没有头信息的时候, 也会放行.
        //但是如果有头信息 Authorization , 并且其值的格式也和我们设置的一样, 那么就需要对其进行检测了:
        //符合,放行.
        //不符合,不放行.
        String header = request.getHeader("Authorization");
        log.info("header={}", header);
        if (header == null) {
            return true;        //虽然没有头信息,依旧放行
        }
        String prefix = header.substring(0, 7);
        log.info("prefix={}", prefix);
        if (!"Bearer ".equals(prefix)) {
            throw new PermissionException("权限不足");
        }
        String token = header.substring(7);
        log.info("token={}", token);
        Claims claims = jwtUtil.parseToken(token);
        log.info("claims={}", claims);
        if (claims == null) {
            throw new PermissionException("权限不足");
        }
        String role = (String) claims.get("roles");
        log.info("role={}", role);
        if ("user".equals(role)) {
            request.setAttribute("roles", "user");
        } else if ("admin".equals(role)) {
            request.setAttribute("roles", "admin");
        } else {
            throw new PermissionException("权限不足");
        }
        return true;
    }
}