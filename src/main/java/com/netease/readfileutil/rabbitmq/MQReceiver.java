package com.netease.readfileutil.rabbitmq;

import com.netease.readfileutil.commons.ParamConstants;
import com.netease.readfileutil.redis.RedisService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class MQReceiver {


    private static final Logger log = LoggerFactory.getLogger(MQReceiver.class);

//    public Iterator receive() {
//
//        String re;
//        FileMessage fileMessage;
//        List list = null;
//        Iterator iterator = null;
//        try {
//            //主动拉取
//            GetResponse response = channel.basicGet(ParamConstants.FILE_QUEUE, true);
//            byte[] data = response.getBody();
//            re = new String(data);
//            fileMessage = RedisService.stringToBean(re, FileMessage.class);
//            list = fileMessage.getList();
////            //如果是最后一条数据，末尾放end
////            if (fileMessage.getClientId().equals("-1")) {
////                list.add(list.size(), "end");
////            }
//            log.info("拉取>>>" + re);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (list != null) {
//            iterator = list.iterator();
//        }
//        return iterator;
//    }

    public List receive(Channel channel) {

        String re;
        FileMessage fileMessage;
        List list = null;
        Iterator iterator = null;
        try {
            //主动拉取
            GetResponse response = channel.basicGet(ParamConstants.FILE_QUEUE, true);
            byte[] data = response.getBody();
            re = new String(data);
            fileMessage = RedisService.stringToBean(re, FileMessage.class);
            list = fileMessage.getList();
//            //如果是最后一条数据，末尾放end
//            if (fileMessage.getClientId().equals("-1")) {
//                list.add(list.size(), "end");
//            }
            log.info("拉取>>>" + re);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list != null) {
            iterator = list.iterator();
        }
        return list;
    }
}

