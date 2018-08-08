package com.netease.readfileutil.rabbitmq;

import com.google.common.util.concurrent.RateLimiter;
import com.netease.readfileutil.commons.ParamConstants;
import com.netease.readfileutil.redis.RedisService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;


/**
 * @date: 2018-07-31
 * @author: liguobin
 * @description: 消息生成者
 */
@Service
@Order
public class MQSender {

    private static final Logger log = LoggerFactory.getLogger(MQSender.class);
    public final RateLimiter limiter = RateLimiter.create(ParamConstants.RATELIMITER); // 每秒不超过500个任务被提交

    @Autowired
    private Channel channel;

    public void send(FileMessage data) {
        try {
            log.info("发送>>>" + data);
            limiter.acquire();
            String mess = RedisService.beanToString(data);
            byte[] messageBodyBytes = mess.getBytes();
            channel.basicPublish(ParamConstants.FANOUT_EXCHANGE, "", null, messageBodyBytes);
        } catch (Exception e) {
            log.error("MQ发送异常：" + e.getMessage());
        }
    }

}
