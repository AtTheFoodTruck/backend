package com.sesac.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

public class RedisService {
    private static final long JWT_REFRESH_TOKEN_VALIDITY = 60 * 60 * 24 * 7;

    @Autowired
    private RedisTemplate<String, Object> tokenRedisTemplate;

    public void setRefreshToken(RedisToken redisToken) {
        ValueOperations<String, Object> tokenVop = tokenRedisTemplate.opsForValue();
        tokenVop.set(redisToken.getEmail(), redisToken, JWT_REFRESH_TOKEN_VALIDITY * 1000, TimeUnit.MILLISECONDS);
    }


}
