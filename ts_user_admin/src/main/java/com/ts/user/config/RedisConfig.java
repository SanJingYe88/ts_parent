package com.ts.user.config;

import com.ts.user.pojo.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.net.UnknownHostException;

//@Configuration
//public class RedisConfig {
//
//    //以 JSON 方式保存对象. 那么我们就需要改变 RedisTemplate 的默认的序列化规则
//    @Bean
//    public RedisTemplate<Object, User> userRedisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
//        RedisTemplate<Object, User> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactory);
//        // 使用Jackson2JsonRedisSerialize 替换默认序列化
//        Jackson2JsonRedisSerializer<User> jsonRedisSerializer = new Jackson2JsonRedisSerializer<User>(User.class);
//        template.setDefaultSerializer(jsonRedisSerializer);
//        return template;
//    }
//}
