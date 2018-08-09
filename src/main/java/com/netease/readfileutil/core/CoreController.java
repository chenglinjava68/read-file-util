package com.netease.readfileutil.core;

import com.netease.readfileutil.commons.ParamConstants;
import com.netease.readfileutil.core.impl.CoreIteratorDecorator;
import com.netease.readfileutil.core.impl.CoreIteratorImpl;
import com.netease.readfileutil.rabbitmq.MQReceiver;
import com.netease.readfileutil.util.ByteArrayUtils;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

/**
 * @date: 2018-07-31
 * @author: liguobin
 * @description: 暴露给客户端的接口
 */
@RestController
public class CoreController {

    Logger log = LoggerFactory.getLogger(CoreController.class);


    @Autowired
    private MQReceiver mqReceiver;

    @Autowired
    private CoreAsyncHandler coreAsyncHandler;

    @Autowired
    private Channel channel;


    @Autowired
    private CoreHttpAPIService coreHttpAPIService;

    /**
     * 暴露给客户端获取数据的接口
     * <p>
     * 只有异步请求返回true,才有必要读取数据，否则应该重新发送请求，这个还没有做
     *
     * @return
     */
    @RequestMapping(value = "/read", method = RequestMethod.GET)
    public String read() throws UnsupportedEncodingException {
        byte[] ret = null;
        try {
            coreAsyncHandler.handler();//异步调用
            //封装迭代器，此迭代器不会反回含有任何依赖于服务器的bean，而是需要由客户端来提供bean
            CoreIteratorDecorator coreIteratorDecorator = new CoreIteratorDecorator(new CoreIteratorImpl());
            Optional<byte[]> bytes = ByteArrayUtils.objectToBytes(coreIteratorDecorator);//将对象转换为二进制字节数组
            ret = bytes.get();
            log.info("Bean:" + ret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ByteArrayUtils.toHexString(ret);
        //测试的时候注释掉返回值，否则返回值这个迭代器会自动请求http,会出现两条重复数据，找了好久才找的问题所在
        //具体原因可能是返回值中的bean引起的副作用，待处理
        //return coreIterator;
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
