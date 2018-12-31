package com.ts.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import util.IdWorker;

@EnableEurekaClient
@SpringBootApplication
public class Base_Application {
    public static void main(String[] args){
        SpringApplication.run(Base_Application.class,args);
    }

    //将 IdWorker 加入Spring容器
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }
}
