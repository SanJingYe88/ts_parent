package com.ts.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.ts.manager.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.MyStringUtils;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class ManagerFilter extends ZuulFilter{

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器内执行的操作, 返回任何object对象都表示继续执行
     * 通过 requestContext.setSendZuulResponse(false) 来终止运行
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        //后台网关的 token 校验
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        //任何进入网关的请求都是两次,第一次是 OPTION,第二次是真正的请求
        if(request.getMethod().equals("OPTIONS")){
            return null;
        }

        //对于登录请求,此时还没有 token , 应该放行
        if(request.getRequestURI().indexOf("login") > 0){
            return null;
        }

        //如果是文件上传,则不判断.
        //按时没有更好的解决办法,只能先这样了
        log.info(request.getHeader("Content-Type"));
        if(request.getHeader("Content-Type") != null
                && request.getHeader("Content-Type").contains("multipart/form-data")){
            return null;
        }

        String header = request.getHeader("Authorization");
        if (MyStringUtils.isNullOrEmpty(header)) {
            noPermission(requestContext);
            return null;
        }
        String prefix = header.substring(0, 7);
        log.info("prefix={}", prefix);
        if (!"Bearer ".equals(prefix)) {
            noPermission(requestContext);
            return null;
        }
        String token = header.substring(7);
        log.info("token={}", token);
        Claims claims = jwtUtil.parseToken(token);
        log.info("claims={}", claims);
        if (claims == null) {
            noPermission(requestContext);
            return null;
        }
        String role = (String) claims.get("roles");
        log.info("role={}", role);
        if ("admin".equals(role)) {
            request.setAttribute("roles", "admin");
        } else {
            noPermission(requestContext);
            return null;
        }
        return null;
    }

    private void noPermission(RequestContext requestContext) {
        log.info("无权访问");
        requestContext.setSendZuulResponse(false);//终止运行
        requestContext.setResponseStatusCode(401);//响应状态码
        requestContext.setResponseBody("No Permission");
        requestContext.getResponse().setContentType("text/html;charset=UTF‐8");
    }
}
