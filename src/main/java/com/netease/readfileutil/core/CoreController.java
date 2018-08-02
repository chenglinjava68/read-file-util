package com.netease.readfileutil.core;

import com.netease.readfileutil.commons.ParamConstants;
import com.netease.readfileutil.rabbitmq.MQReceiver;
import com.netease.readfileutil.redis.RedisService;
import com.rabbitmq.client.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;

/**
 * @date: 2018-07-31
 * @author: liguobin
 * @description: 暴露接口
 */

@RestController
public class CoreController {


    @Autowired
    private MQReceiver mqReceiver;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CoreAsyncHandler coreAsyncHandler;

    @Autowired
    private Channel channel;


    @Autowired
    private CoreHttpAPIService coreHttpAPIService;

    @Autowired
    private CoreIterator coreIterator;

    /**
     * 暴露给客户端获取数据的接口
     * <p>
     * 只有异步请求返回true,才有必要读取数据，否则应该重新发送请求，这个还没有做
     *
     * @return
     */
    @RequestMapping(value = "/read", method = RequestMethod.GET)
    public Iterator<List<String>> read() {
        try {
            coreAsyncHandler.handler();//异步调用
            Thread.sleep(3000);
            if (coreIterator == null) {
                return null;
            }
            while (coreIterator.hasNext()) {
                List<String> list = (List<String>) coreIterator.next();
                list.stream().parallel().forEach(s -> System.out.println(s));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coreIterator;
    }

    /**
     * 测试http拉取数据
     *
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "httpclient", method = RequestMethod.GET)
    public String test() {
        String str = coreHttpAPIService.doGet(ParamConstants.HOST, ParamConstants.CHARSET);
        return str;
    }

    /**
     * 由迭代器请求，得到数据
     *
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String get() {
        //pull数据
        String ret = mqReceiver.receive(channel);
        if (ret == null) return null;
        return ret;
    }

}
