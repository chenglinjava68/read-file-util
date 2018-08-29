package com.netease.readfileutil.core;

import java.io.Serializable;
import java.util.Iterator;

/**
 * @date: 2018-08-01
 * @author: liguobin
 * @description:封装数据拉取请求
 */
public interface CoreIterator<T> extends Iterator<T>, Serializable {

    boolean setCoreHttpApiBean(CoreHttpAPIService httpAPIService);

}
