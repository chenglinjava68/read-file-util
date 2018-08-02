package com.netease.readfileutil.core;

import com.netease.readfileutil.commons.ParamConstants;
import com.netease.readfileutil.rabbitmq.FileMessage;
import com.netease.readfileutil.redis.RedisService;
import com.netease.readfileutil.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @date: 2018-08-02
 * @author: liguobin
 * @description:
 */
@Component
public class CoreAsyncHandler {

    @Autowired
    private RedisService redisService;

    @Autowired
    private CoreReadFileUtil coreReadFileUtil;


    @Async
    protected void handler() {

        String clientId = null;
        //先查询
        FileMessage fileMessage = redisService.getBean(ParamConstants.REDIS_KEY, FileMessage.class);
        int start;
        if (fileMessage == null) {//第一次开始
            //说明是第一次读取
            start = 0;
            clientId = UuidUtil.getUuid();
            fileMessage = new FileMessage();
            fileMessage.setClientId(clientId);
            fileMessage.setLastTimeStamp(System.currentTimeMillis());
            redisService.setBean(ParamConstants.REDIS_KEY, fileMessage);//只记录clientID
        } else if (fileMessage.getClientId().equals("-1")) {
            //读取完成了，以防下次读取，再次请求将重新读取
            clientId = UuidUtil.getUuid();
            start = 0;
            fileMessage = new FileMessage();
            fileMessage.setClientId(clientId);
            fileMessage.setLastTimeStamp(System.currentTimeMillis());
            redisService.setBean(ParamConstants.REDIS_KEY, fileMessage);//只记录clientID
        } else {
            //错误继续，或者重启继续
            clientId = fileMessage.getClientId();
            start = fileMessage.getCurrentBytePos();
            fileMessage = new FileMessage();
            fileMessage.setClientId(clientId);
            fileMessage.setLastTimeStamp(System.currentTimeMillis());
            redisService.setBean(ParamConstants.REDIS_KEY, fileMessage);//只记录clientID
            //发送的时候判断redis的client是否过期
        }
        coreReadFileUtil.render(clientId, ParamConstants.path, start);//默认为0，表示从头开始
        //获取数据，并且返回迭代器,读取完返回null
    }
}
