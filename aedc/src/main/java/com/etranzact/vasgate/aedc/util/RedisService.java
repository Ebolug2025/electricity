package com.etranzact.vasgate.aedc.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void setCustomer(String key, String customer) throws Exception {
        redisTemplate.opsForValue().set(key, customer);
    }

    public RedisTemplate<String, Object> getInstance(){

        return redisTemplate;
    }

}
