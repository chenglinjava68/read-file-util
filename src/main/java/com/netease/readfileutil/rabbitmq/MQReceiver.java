package com.netease.readfileutil.rabbitmq;

import com.netease.readfileutil.commons.ParamConstants;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * @date: 2018-07-31
 * @author: liguobin
 * @description: 消息接受者，拆分消息，原本是返回集合，现在直接返还字符串
 */
@Service
public class MQReceiver {


    private static final Logger log = LoggerFactory.getLogger(MQReceiver.class);

    public String receive(Channel channel) {

        String re = null;
        try {
            //主动拉取
            GetResponse response = channel.basicGet(ParamConstants.FILE_QUEUE, true);
            if (response == null) {
                return null;
            }
            byte[] data = response.getBody();
            re = new String(data);
            log.info("拉取>>>" + re);
        } catch (Exception e) {
            log.error("MQ拉取异常" + e.getMessage());
        }
        return re;
    }
}

