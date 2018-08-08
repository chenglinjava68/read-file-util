package com.netease.readfileutil.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @date: 2018-07-31
 * @author: liguobin
 * @description:Jedis操作redis工具类
 */
@Service
@Order
public class RedisService {

    @Autowired
    private JedisPool jedisPool;


    /**
     * 获取String value
     */
    public String getString(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = jedis.get(key);
            return str;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 获取对象
     */
    public <T> T getBean(String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = jedis.get(key);
            T t = stringToBean(str, clazz);
            return t;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 设置对象
     */
    public <T> boolean setBean(String key, int expire, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            if (str == null || str.length() < 0) {
                return false;
            }
            if (expire < 1) {
                jedis.set(key, str);
            } else {
                jedis.setex(key, expire, str);
            }
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    public <T> boolean setBean(String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            if (str == null || str.length() < 0) {
                return false;
            }
            jedis.set(key, str);
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 设置字符串
     */
    public <T> boolean setString(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (key == null || key.length() < 0) {
                return false;
            }
            jedis.set(key, value);
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 判断key是否存在
     */
    public boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 生成真正的key
            return jedis.exists(key);
        } finally {
            returnToPool(jedis);
        }
    }


    public boolean delete(String key) {
        if (key == null) {
            return false;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            returnToPool(jedis);
        }
    }


    public static <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String) value;
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + value;
        } else {
            return JSON.toJSONString(value);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T stringToBean(String str, Class<T> clazz) {
        if (str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    private void returnToPool(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

}
