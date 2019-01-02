package com.ts.web.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class WebFilter extends ZuulFilter{
    @Override
    public String filterType() {
        // 返回一个字符串,代表过滤器的类型,在Zuul中定义了四种不同生命周期的过滤器类型
        // "pre" 前置过滤器,以在请求被路由之前调用
        // "route" 在路由请求时候被调用
        // "error",处理请求时发生错误时被调用
        // "post" 后置过滤器,在route和error过滤器之后被调用
        return "pre";
    }

    @Override
    public int filterOrder() {
        //用来指定当前过滤器的执行顺序.数值越小,越先执行.
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        // 表示是否执行该过滤器
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        log.info("{}","过滤器 WebFilter...");
        //网站前台的 token 转发
        //获取到被zuul包装后的 requestContext 上下文
        RequestContext requestContext = RequestContext.getCurrentContext();
        //获取到 request 对象
        HttpServletRequest request = requestContext.getRequest();
        //获取头信息
        String header = request.getHeader("Authorization");
        if (header != null){
            //添加
            requestContext.addZuulRequestHeader("Authorization",header);
        }
        return null;
    }
}
