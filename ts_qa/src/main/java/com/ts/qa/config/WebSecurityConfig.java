package com.ts.qa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 密码加密
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()    // 定义哪些URL需要被保护、哪些不需要被保护
                .antMatchers("/**").permitAll()     //任何地址都可以访问
                .anyRequest().authenticated()   // 任何请求,登录后可以访问
                .and().csrf().disable();        // 关闭csrf防护
    }
}
