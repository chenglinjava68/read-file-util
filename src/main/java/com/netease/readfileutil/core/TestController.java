package com.netease.readfileutil.core;

import com.netease.readfileutil.commons.ParamConstants;
import com.netease.readfileutil.core.impl.CoreHttpAPIServiceImpl;
import com.netease.readfileutil.core.impl.CoreIteratorDecorator;
import com.netease.readfileutil.util.ByteArrayUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * @date: 2018-08-09
 * @author: liguobin
 * @description: 此作为客户端测试 与服务端无关，@Autowired是为了方便，实际需要自己传进来
 */
@RestController
public class TestController {

    @Autowired
    private CloseableHttpClient httpClient;//需要由客户端提供http连接

    @Autowired
    private RequestConfig config;//同时客户端还应该提供自己的请求配置

    /**
     * 测试使用
     */
    @RequestMapping("/test")
    public void test() {
        CoreHttpAPIServiceImpl coreHttpAPIService = new CoreHttpAPIServiceImpl();//用户需要传入的
        coreHttpAPIService.setConfig(config);//用户必须设置连接池参数
        coreHttpAPIService.setHttpClient(httpClient);//用户必须设置http连接池
        //用户发送请求数据的接口
        String s = coreHttpAPIService.doGet(ParamConstants.HOST, ParamConstants.CHARSET);//用户发送到服务端的接口
        CoreIteratorDecorator coreIteratorDecorator = null;
        CoreIterator<List<String>> ret = null;
        Optional<Object> optional = ByteArrayUtils.bytesToObject(ByteArrayUtils.toByteArray(s));//解析服务端的bean
        if (optional.get() != null) {
            coreIteratorDecorator = (CoreIteratorDecorator) optional.get();
            ret = coreIteratorDecorator.setCoreHttpApi(coreHttpAPIService);
        }
        if (ret == null) return;
        //对迭代器发起请求
        while (ret.hasNext()) {
            List<String> list = ret.next();
            list.stream().parallel().forEach(s1 -> System.out.println("client receive :" + s1));
        }
    }
}
