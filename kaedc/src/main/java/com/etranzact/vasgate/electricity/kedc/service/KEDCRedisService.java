package com.etranzact.vasgate.electricity.kedc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class KEDCRedisService {

    private final StringRedisTemplate JEDCstringRedisTemplate;

    @Autowired
    public KEDCRedisService(StringRedisTemplate stringRedisTemplate) {
        this.JEDCstringRedisTemplate = stringRedisTemplate;
    }

    public void setValue(String key, String value){

        JEDCstringRedisTemplate.opsForValue().set(key, value);
    }

    public String getValue(String key){

        return JEDCstringRedisTemplate.opsForValue().get(key);
    }

    public void removeAccount(String account){

        JEDCstringRedisTemplate.delete(account);
    }
}
