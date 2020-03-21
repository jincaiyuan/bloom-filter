package com.rxy.redislock.lock;

import ch.qos.logback.classic.Logger;
import com.rxy.redislock.context.ThreadLocalContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Data
public class RedisLock {

    private RedisTemplate<String, String> redisTemplate;

    private int expireSeconds = 60; // 过期时间
    private int interval = 60;
    private int retries = 3; //默认尝试3次

    public RedisLock(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisLock(RedisTemplate<String, String> redisTemplate, int interval) {
        this(redisTemplate);
        this.interval = interval;
    }

    public RedisLock(RedisTemplate<String, String> redisTemplate, int interval, int expireSeconds) {
        this(redisTemplate, interval);
        this.expireSeconds = expireSeconds;
    }

    // 获取锁
    public boolean acquire(String key) {
        int count = retries;
        do {
            String lockValue = UUID.randomUUID().toString();
            if (redisTemplate.opsForValue().setIfAbsent(key, lockValue, expireSeconds, TimeUnit.SECONDS)) {
                ThreadLocalContext.put(key, lockValue);
                log.info("加锁成功：{}", key);
                return true;
            }
            count--;
            if (count >= 0) {
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    log.error(e.getMessage());
                }
            }
        } while (count >= 0);
        return false;
    }

    // 释放锁
    public void release(String key) {
        Object lockValue = ThreadLocalContext.get(key);
        try {
            if(lockValue != null && lockValue.toString().equals(redisTemplate.opsForValue().get(key))) {
                redisTemplate.delete(key);
            }
        } finally {
            ThreadLocalContext.remove(key);
        }
    }

}
