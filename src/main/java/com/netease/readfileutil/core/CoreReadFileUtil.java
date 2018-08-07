package com.netease.readfileutil.core;

import java.util.List;

/**
 * @date: 2018-07-31
 * @author: liguobin
 * @description: 核心读取接口
 */
public interface CoreReadFileUtil {

    /**
     * 解析并读取数据
     *
     * @param client         当前客户端id
     * @param path           当前文件的路径
     * @param currentBytePos 当前读取的总字节数，即流的位置
     */
     void render(String client, String path, Integer currentBytePos);

    /**
     * 发送消息到队列，redis
     *
     * @param client         当前客户端id
     * @param currentBytePos 当前读取的总字节数，即流的位置
     * @param list           当前发送的消息列表
     * @return
     */
    boolean sendMessage(String client, int currentBytePos, List<String> list);

    /**
     * 发送读取完成的信号至redis
     *
     * @param currentBytePos 当前读取的总字节数，即流的位置
     */
    void sendEndSign(int currentBytePos);

    /**
     * 检测当前客户端是否可以继续进行发送/更新redis中的数据
     *
     * @param client 当前客户端id
     * @return 是：可以继续更新redis标记
     */
    boolean isCanUpdate(String client);

}