package com.kennen.yebserver.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import springfox.documentation.annotations.Cacheable;

import javax.annotation.Resource;

/**
 * @Author: hejiyuan
 * @Date: 2021/3/11 17:32
 * @Description: redis工具类
 */
@Component
public class RedisUtil {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * String 存
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * String 取
     * @param key
     * @return
     */
    public Object get(String key) {
        Object result = redisTemplate.opsForValue().get(key);
        return result;
    }

    /**
     * hash 存
     * @param key
     * @param hashKey
     * @param value
     */
    public void setHash(String key, String hashKey, String value){
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * hash 取
     * @param key
     * @param hashKey
     * @return
     */
    public Object getHash(String key, String hashKey){
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 判断hash中是否存在指定hashKey
     * @param key
     * @param hashKey
     * @return
     */
    public boolean hContains(String key, String hashKey){
        return redisTemplate.opsForHash().entries(key).containsKey(hashKey);
    }
}
