package com.netease.readfileutil.core;

import com.netease.readfileutil.commons.ParamConstants;
import com.netease.readfileutil.rabbitmq.FileMessage;
import com.netease.readfileutil.rabbitmq.MQReceiver;
import com.netease.readfileutil.rabbitmq.MQSender;
import com.netease.readfileutil.redis.RedisService;
import com.netease.readfileutil.util.UuidUtil;
import com.rabbitmq.client.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @date: 2018-07-31
 * @author: liguobin
 * @description:
 */

@Controller
public class CoreController {

    @Autowired
    MQSender mqSender;

    @Autowired
    MQReceiver mqReceiver;

    @Autowired
    RedisService redisService;

    @Autowired
    ReadFileUtilCore readFileUtilCore;

    @Autowired
    Channel channel;


//    @RequestMapping("/get")
//    @ResponseBody
//    public Iterator get() {
//
//        String clientId = UuidUtil.getUuid();
//        //先查询
//        FileMessage fileMessage = redisService.getBean(ParamConstants.REDIS_KEY, FileMessage.class);
//        int start;
//        if (fileMessage == null) {
//            //说明是第一次读取
//            start = 0;
//            fileMessage = new FileMessage();
//            fileMessage.setClientId(clientId);
//            redisService.setBean(ParamConstants.REDIS_KEY, 600, fileMessage);//只记录clientID
//        } else {
//            //错误继续，或者重启继续
//            clientId = fileMessage.getClientId();
//            start = fileMessage.getCurrentBytePos();
//        }
//        readFileUtilCore.render(clientId, ParamConstants.path, start);//默认为0，表示从头开始
//
//        return mqReceiver.receive();//获取数据，并且返回迭代器,读取完返回null
//    }

    @RequestMapping("/get")
    @ResponseBody
    public void get() {

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
        readFileUtilCore.render(clientId, ParamConstants.path, start);//默认为0，表示从头开始
        //获取数据，并且返回迭代器,读取完返回null
    }

    @RequestMapping("/read")
    @ResponseBody
    public List read() {
        return mqReceiver.receive(channel);
    }
}
