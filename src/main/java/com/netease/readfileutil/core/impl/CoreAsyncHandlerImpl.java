package com.netease.readfileutil.core.impl;

import com.netease.readfileutil.commons.ParamConstants;
import com.netease.readfileutil.core.CoreAsyncHandler;
import com.netease.readfileutil.core.CoreReadFileIO;
import com.netease.readfileutil.rabbitmq.FileMessage;
import com.netease.readfileutil.redis.RedisDistributionLock;
import com.netease.readfileutil.redis.RedisService;
import com.netease.readfileutil.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CoreAsyncHandlerImpl implements CoreAsyncHandler {

    private static final Logger log = LoggerFactory.getLogger(RedisDistributionLock.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private CoreReadFileIO coreReadFileUtil;

    @Autowired
    private RedisDistributionLock redisDistributionLock;

    @Async
    @Override
    public void handler() {
        String time = null;
        try {

            time = String.valueOf(System.currentTimeMillis() + ParamConstants.OVER_TIME);
            if (!redisDistributionLock.lock(ParamConstants.LOCK_KEY, time)) {
                log.warn("handler(),加锁失败");
                return;
            }
            log.info("handler(),加锁成功");

            String clientId = null;
            //先查询
            FileMessage fileMessage = redisService.getBean(ParamConstants.REDIS_KEY, FileMessage.class);
            int start;
            if (fileMessage == null) {//第一次开始
                //说明是第一次读取
                start = START;
                clientId = UuidUtil.getUuid();
                fileMessage = new FileMessage();
                fileMessage.setClientId(clientId);
                fileMessage.setLastTimeStamp(System.currentTimeMillis());
                redisService.setBean(ParamConstants.REDIS_KEY, fileMessage);//只记录clientID
            } else if (fileMessage.getClientId().equals("-1")) {
                //读取完成了，以防下次读取，再次请求将重新读取
                clientId = UuidUtil.getUuid();
                start = START;
                fileMessage = new FileMessage();
                fileMessage.setClientId(clientId);
                fileMessage.setLastTimeStamp(System.currentTimeMillis());
                redisService.setBean(ParamConstants.REDIS_KEY, fileMessage);//只记录clientID
            } else {
                //错误继续，或者重启继续
                clientId = UuidUtil.getUuid();
                start = fileMessage.getCurrentBytePos();
                fileMessage = new FileMessage();
                fileMessage.setClientId(clientId);
                fileMessage.setLastTimeStamp(System.currentTimeMillis());
                redisService.setBean(ParamConstants.REDIS_KEY, fileMessage);//只记录clientID
                //发送的时候判断redis的client是否过期
            }
            coreReadFileUtil.render(clientId, ParamConstants.path, start);//默认为0，表示从头开始
            //获取数据，并且返回迭代器,读取完返回null
        } finally {
            redisDistributionLock.unLook(ParamConstants.LOCK_KEY, time);
            log.info("handler(),解锁成功");
        }
    }
}
