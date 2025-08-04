package com.lemonpay.lemonpayvas.electricity.redisutility.redisservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@Slf4j
public class LemonpayConnectionFactory {

    @Value("${REDIS_URL}")
    private String redisHost;

    @Value("${REDIS_PORT}")
    private String port;

    @Value("${REDIS_PASSWORD}")
    private String password;

    @Value("${REDIS_USERNAME}")
    private String redisusername;

    @Bean
    public RedisConnectionFactory NewVasgateRedisConnectionFactory(){

        JedisConnectionFactory factory = new JedisConnectionFactory();
        try {

            factory.setHostName(redisHost);

            log.info("=========== redis host: "+redisHost);
            factory.setPort(Integer.parseInt(port));
            log.info("=========== redis port: "+port);
//            factory.setPassword(password);
//            log.info("=========== redis password: "+password);
//            //factory.setClientName();
//            factory.setClientName(redisusername);
//            log.info("=========== redis username: "+redisusername);
            factory.afterPropertiesSet();


        }catch(Exception e){

            e.printStackTrace();
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>> "+e.getMessage());
        }

        return factory;
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(){

        RedisTemplate<String, String> redisTemplate = null;
        try {


           redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(NewVasgateRedisConnectionFactory());
            //redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        }catch (Exception e){
            e.printStackTrace();
            log.info("===============the redis connection exception is: "+e.getMessage());
        }
        return redisTemplate;
    }
}
