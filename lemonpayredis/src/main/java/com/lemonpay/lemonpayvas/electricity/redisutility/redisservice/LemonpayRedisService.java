package com.lemonpay.lemonpayvas.electricity.redisutility.redisservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class LemonpayRedisService {

    @Autowired
    private  StringRedisTemplate NewVasgatestringRedisTemplate;


    public void setValue(String key, String value){

        NewVasgatestringRedisTemplate.opsForValue().set(key, value);
    }

    public void setValue(String key, String value, Long duration){

        NewVasgatestringRedisTemplate.opsForValue().set(key, value, duration);
    }

    public String getValue(String key){

        return NewVasgatestringRedisTemplate.opsForValue().get(key);
    }

    public void removeAccount(String account){

        NewVasgatestringRedisTemplate.delete(account);
    }
}
