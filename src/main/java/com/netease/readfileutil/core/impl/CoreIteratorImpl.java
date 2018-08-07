package com.netease.readfileutil.core.impl;

import com.netease.readfileutil.commons.ParamConstants;
import com.netease.readfileutil.core.CoreHttpAPIService;
import com.netease.readfileutil.core.CoreIterator;
import com.netease.readfileutil.rabbitmq.FileMessage;
import com.netease.readfileutil.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @date: 2018-08-01
 * @author: liguobin
 * @description:封装数据拉取请求
 */
@Component
@Order
public class CoreIteratorImpl<T> implements CoreIterator<T> {

    Logger logger = LoggerFactory.getLogger(CoreIteratorImpl.class);

    /**
     * 这里依赖spring bean,最后把这个也提取处理
     */
    @Autowired
    private CoreHttpAPIService httpAPIService;

    /**
     * 缓存hashNset的数据
     */
    private static volatile String ret = null;

    @PostConstruct
    public void init() {
        if (httpAPIService == null) {
            logger.error("系统不存在CoreHttpAPIService实例");
        }
    }

    @Override
    public boolean hasNext() {
        //这里拿不到数据可能是生产者太慢了，所以需要优化这里
        String data;
        try {
            //如果生产者实在太慢了，有必要在这里进行睡眠处理，否则无必要
            Thread.sleep(ParamConstants.SLEEP_TIME);
            data = httpAPIService.doGet(ParamConstants.INNER_HOST, ParamConstants.CHARSET);
            if (data == null) return false;
            ret = data;
            FileMessage fileMessage = RedisService.stringToBean(data, FileMessage.class);
            if (fileMessage == null) return false;
            List<String> list = fileMessage.getList();//使用String来处理
            //发送请求
            if (list == null || list.size() == 0 || list.isEmpty()) {
                return false;
            }
        } catch (Exception e) {
            logger.error("hasNext方法出错，请检查是否有数据，或者链接超时");
        }
        return true;
    }

    //多余的泛型。。
    @Override
    public T next() {

        String data;
        List<String> list = null;
        try {
            if (ret != null) {
                data = ret;
            } else {
                data = httpAPIService.doGet(ParamConstants.INNER_HOST, ParamConstants.CHARSET);
            }
            FileMessage fileMessage = RedisService.stringToBean(data, FileMessage.class);
            if (fileMessage == null) return (T) new ArrayList<String>();
            list = fileMessage.getList();//使用String来处理
            if (list == null || list.size() == 0 || list.isEmpty()) {
                logger.warn("没有获得数据");
                return (T) new ArrayList<String>();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
            ret = null;
        }
        return (T) list;
    }

    @Override
    public void remove() {

        try {
            logger.error("不可调用该方法");
            throw new Exception("本迭代器不允许调用remove()");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void forEachRemaining(Consumer action) {

        try {
            logger.error("不可调用该方法");
            throw new Exception("本迭代器不允许调用forEachRemaining()");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
