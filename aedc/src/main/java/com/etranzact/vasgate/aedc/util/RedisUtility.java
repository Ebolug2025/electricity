package com.etranzact.vasgate.aedc.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

@Configuration
@Slf4j

public class RedisUtility {

    @Value("${REDIS_URL}")
    private String redisHost;

    @Value("${REDIS_PORT}")
    private String port;

    @Value("${REDIS_PASSWORD}")
    private String password;


    @Bean
    public RedisConnectionFactory redisConnectionFactory(){

        JedisConnectionFactory factory = new JedisConnectionFactory();

        try {

            factory.setHostName(redisHost);
            //factory.set
            factory.setPort(Integer.parseInt(port));
            factory.setPassword(password);
            //factory.setClientName(redisusername);
            factory.afterPropertiesSet();


        }catch(Exception e){

            e.printStackTrace();
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>> "+e.getMessage());
        }

        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(){

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        return redisTemplate;
    }



    }
