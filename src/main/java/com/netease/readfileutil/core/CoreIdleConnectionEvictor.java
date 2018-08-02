package com.netease.readfileutil.core;

import org.apache.http.conn.HttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @date: 2018-08-02
 * @author: liguobin
 * @description: 关闭失效连接 请求处理
 */
@Component
public class CoreIdleConnectionEvictor extends Thread {

    @Autowired
    private HttpClientConnectionManager connMgr;

    private volatile boolean shutdown;

    public CoreIdleConnectionEvictor() {
        super();
        super.start();
    }

    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(5000);
                    // 关闭失效的连接
                    connMgr.closeExpiredConnections();
                }
            }
        } catch (InterruptedException ex) {
            // 结束
        }
    }

    //关闭清理无效连接的线程
    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }
}