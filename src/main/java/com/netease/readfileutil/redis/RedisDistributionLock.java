package com.netease.readfileutil.redis;

import com.netease.readfileutil.commons.ParamConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @date: 2018-08-07
 * @author: liguobin
 * @description: 分布式锁实现，使用springboot-starter
 */
@Component
public class RedisDistributionLock {

    private static final Logger log = LoggerFactory.getLogger(RedisDistributionLock.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 实现redis分布式锁
     *
     * @param key   常量键
     * @param value 当前时间+超时时间
     * @return boolean
     */
    public boolean lock(String key, String value) {

        /** 如果键不存在则新增,存在则不改变已经有的值 . */
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
            log.info("lock:" + key + ",value:" + value);
            return true;
        }

        /**
         * redis键值已经存在的情况
         * 乐观锁，类似CAS操作，这样使得没有获取锁的用户都不能成功操作，高并发下，可能由很多用户出现错误
         * 不加这步会因为业务抛出异常造成死锁
         */
        String currentValue = redisTemplate.opsForValue().get(key); // 获取当前值
        /** 判断当前新值是否小于当前系统的时间戳 这里系统时间不够精确. */
        if (!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            /** 获取上一个锁【旧值】的时间，getAndSet类似i++ ,先获取，再设置. */
            String oldValue = redisTemplate.opsForValue().getAndSet(key, value); // 返回一个字符串，也就是键的旧值。
            /**
             * 如果键不存在，则返回null。如果键存在，得到的oldValue不是空，使用旧值与currentValue比较，不相同，则说明已经被其他线程修改过.
             */
            if (!StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)) {
                /** 说明没有被其他线程修改，加锁成功. */
                log.info("lock:" + key + ",value:" + value);
                return true;
            }
        }
        /** 已经超时，直接返回false,加锁失败. */
        return false;
    }

    /**
     * 解锁
     *
     * @param key
     * @param value
     */
    public void unLook(String key, String value) {
        try {
            String currentValue = redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);
                log.info("unlock:" + key + ",value:" + value);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            //解锁失败,给锁设置过期
            redisTemplate.opsForValue().set(key, value, ParamConstants.OVER_TIME * 10);
        }
    }
}