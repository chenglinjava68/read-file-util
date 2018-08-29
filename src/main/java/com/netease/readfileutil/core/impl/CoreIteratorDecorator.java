package com.netease.readfileutil.core.impl;

import com.netease.readfileutil.core.CoreHttpAPIService;
import com.netease.readfileutil.core.CoreIterator;

import java.io.Serializable;

/**
 * @date: 2018-08-09
 * @author: liguobin
 * @description:返回给客户端Bean
 */
public class CoreIteratorDecorator<T> implements Serializable {

    //CoreIterator接口无法被序列化
    private CoreIteratorImpl<T> coreIterator;//返回的迭代器,这个由服务端提供，实际无任何依赖

    public CoreIteratorDecorator(CoreIteratorImpl<T> coreIterator) {
        this.coreIterator = coreIterator;
    }

    public CoreIteratorDecorator() {

    }

    /**
     * 当这个方法调用成功，表示用户成功设置自己的http bean 了
     *
     * @return
     * @throws Exception
     */
    public CoreIterator<T> setCoreHttpApi(CoreHttpAPIService coreHttpAPIService) {
        boolean success = coreIterator.setCoreHttpApiBean(coreHttpAPIService);
        if (!success) return null;
        return coreIterator;
    }


}
