package com.netease.readfileutil.core;

/**
 * @date: 2018-08-02
 * @author: liguobin
 * @description: httpclient 请求处理接口，客户端需要实现该Bean
 */
public interface CoreHttpAPIService {
    /**
     * 不带参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
     *
     * @param url
     * @return
     * @throws Exception
     */
    String doGet(String url, String charset);

}