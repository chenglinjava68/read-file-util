package com.netease.readfileutil.rabbitmq;

import java.util.List;

/**
 * @date: 2018-07-27
 * @author: liguobin
 * @description: MQ消息
 */
public class FileMessage {

    private int currentBytePos;//当前已读取的字节数
    private long threadId;//当前线程ID
    private String clientId;//当前客户端ID
    private List<String> list;//需要发送的消息数据
    private long lastTimeStamp;

    public FileMessage() {

    }

    @Override
    public String toString() {
        return "FileMessage{" +
                "currentBytePos=" + currentBytePos +
                ", threadId=" + threadId +
                ", clientId='" + clientId + '\'' +
                ", list=" + list +
                ", lastTimeStamp=" + lastTimeStamp +
                '}';
    }

    public int getCurrentBytePos() {
        return currentBytePos;
    }

    public void setCurrentBytePos(int currentBytePos) {
        this.currentBytePos = currentBytePos;
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public long getLastTimeStamp() {
        return lastTimeStamp;
    }

    public void setLastTimeStamp(long lastTimeStamp) {
        this.lastTimeStamp = lastTimeStamp;
    }

}
