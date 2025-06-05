package com.etranzact.vasgate.electricity.redisutility.redisservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class NewVasgateRedisService {

    @Autowired
    private  StringRedisTemplate NewVasgatestringRedisTemplate;


    public void setValue(String key, String value){

        NewVasgatestringRedisTemplate.opsForValue().set(key, value);
    }

    public String getValue(String key){

        return NewVasgatestringRedisTemplate.opsForValue().get(key);
    }

    public void removeAccount(String account){

        NewVasgatestringRedisTemplate.delete(account);
    }
}
