package com.netease.readfileutil.rabbitmq;

import com.netease.readfileutil.commons.ParamConstants;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @date: 2018-07-31
 * @author: liguobin
 * @description:
 */
@Component
public class RabbitMQConfig {

    @Autowired
    private RabbitPoolConfig rabbitPoolConfig;

    // Channel
    @Bean("channel")
    @ConditionalOnMissingBean
    public Channel connectionFactory() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(rabbitPoolConfig.getUsername());
        factory.setPassword(rabbitPoolConfig.getPassword());
        factory.setVirtualHost(rabbitPoolConfig.getVirtualHost());
        factory.setHost(rabbitPoolConfig.getHost());
        factory.setPort(Integer.parseInt(rabbitPoolConfig.getPort()));
        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();
        channel.exchangeDeclare(ParamConstants.FANOUT_EXCHANGE, "fanout", true); //创建路由
        channel.queueDeclare(ParamConstants.FILE_QUEUE, true, false, false, null);
        channel.queueBind(ParamConstants.FILE_QUEUE, ParamConstants.FANOUT_EXCHANGE, "");
        return channel;
    }
}
