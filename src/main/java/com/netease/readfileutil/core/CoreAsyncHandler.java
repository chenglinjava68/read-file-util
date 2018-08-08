package com.netease.readfileutil.core;

import com.netease.readfileutil.commons.ParamConstants;

/**
 * @date: 2018-08-02
 * @author: liguobin
 * @description:核心异步请求接口
 */
public interface CoreAsyncHandler {

    /**
     * 默认错误位置为0，即默认开始位置是从头开始
     */
    int START = ParamConstants.ERROR_POS;

    /**
     * 异步处理方法
     */
    void handler();

}
