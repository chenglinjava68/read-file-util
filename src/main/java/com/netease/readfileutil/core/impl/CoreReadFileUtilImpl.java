package com.netease.readfileutil.core.impl;

import com.netease.readfileutil.commons.ParamConstants;
import com.netease.readfileutil.config.FileConfigBean;
import com.netease.readfileutil.core.CoreReadFileUtil;
import com.netease.readfileutil.rabbitmq.FileMessage;
import com.netease.readfileutil.rabbitmq.MQSender;
import com.netease.readfileutil.redis.RedisDistributionLock;
import com.netease.readfileutil.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @date: 2018-07-31
 * @author: liguobin
 * @description:
 */
@Component
@Order
public class CoreReadFileUtilImpl implements CoreReadFileUtil {

    private static final Logger log = LoggerFactory.getLogger(CoreReadFileUtilImpl.class);

    @Autowired
    private FileConfigBean fileConfigBean;

    @Autowired
    private MQSender mqSender;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisDistributionLock redisDistributionLock;


    private List<String> longList = new ArrayList<>();
    private List<String> tempList = new ArrayList<>();
    RandomAccessFile fileInputStream = null;
    private static AtomicInteger count = new AtomicInteger(0);

    @Override
    public void render(String client, String path, Integer currentBytePos) {
        int byteOfChar = 0;
        byte[] bytes = new byte[fileConfigBean.getBufferSize()];
        int lineNumber = 0;
        int pos = 0;
        try {
            fileInputStream = new RandomAccessFile(new File(path), ParamConstants.MODE);
            fileInputStream.skipBytes(currentBytePos);
            int totalPos = currentBytePos;
            while ((byteOfChar = fileInputStream.read()) != -1) {
                totalPos++;
                if (byteOfChar == ParamConstants.START_DELIMITER) {
                    int lastPos = totalPos;
                    byteOfChar = fileInputStream.read();
                    totalPos++;
                    if (byteOfChar == ParamConstants.END_DELIMITER) {
                        String number = (new String(bytes)).trim();

                        if (!"true".equals(fileConfigBean.getIsString()) && number.length() > 0) {
                            Long numberInLong = Long.valueOf(number);
                            longList.add(String.valueOf(numberInLong));
                            tempList.add(String.valueOf(numberInLong));
                            lineNumber++;
                            log.info("total:" + totalPos + ",line:" + numberInLong + ",lineNumber:" + lineNumber);
                            count.incrementAndGet();
                            //目前只能处理数据量大于发送频率的
                            if (count.intValue() == fileConfigBean.getTransmissionFrequenc()) {
                                boolean success = sendMessage(client, totalPos, longList);
                                if (!success) return;
                                count = new AtomicInteger(0);
                                tempList = new ArrayList<>(fileConfigBean.getTransmissionFrequenc());
                                longList = new ArrayList<>(fileConfigBean.getTransmissionFrequenc());
                            }
                            bytes = new byte[fileConfigBean.getBufferSize()];
                            pos = 0;
                            continue;
                        }
                    } else {
                        fileInputStream.seek(lastPos);
                    }
                }
                bytes[pos++] = (byte) byteOfChar;
            }
            if (tempList.size() != 0) {
                sendMessage(client, totalPos, tempList);//添加最后的一个
            }
            sendEndSign(totalPos);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isCanUpdate(String client) {

        //防止死掉的线程，重新复活进而更新redis的标记位
        String time = String.valueOf(System.currentTimeMillis() + ParamConstants.OVER_TIME);
        try {
            if (!redisDistributionLock.lock(ParamConstants.MESSAGE_STATUS_KEY, time)) {
                log.warn("isCanUpdate(),加锁失败");
            }
            log.info("isCanUpdate(),加锁成功");
            FileMessage fileMessage = redisService.getBean(ParamConstants.REDIS_KEY, FileMessage.class);
            if (fileMessage == null) {
                return false;
            }
            String c = fileMessage.getClientId();
            if (!c.equals(client)) {
                return false;
            }
            if (System.currentTimeMillis() - fileMessage.getLastTimeStamp() > ParamConstants.EXPIRE) {
                //大于5分钟
                return false;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            redisDistributionLock.unLook(ParamConstants.MESSAGE_STATUS_KEY, time);
            log.info("isCanUpdate(),解锁成功");
        }
        return true;
    }

    @Override
    public boolean sendMessage(String client, int currentBytePos, List<String> list) {
        try {

            FileMessage longMessage = new FileMessage();
            longMessage.setCurrentBytePos(currentBytePos);
            longMessage.setThreadId(Thread.currentThread().getId());
            longMessage.setClientId(client);
            longMessage.setLastTimeStamp(System.currentTimeMillis());
            //更新reids中的
            if (isCanUpdate(client)) {
                redisService.setBean(ParamConstants.REDIS_KEY, longMessage);
                longMessage.setList(list);
                mqSender.send(longMessage);
                return true;
            } else {
                //不是本客户端或者本客户端超时
                //下次还是得从上次开始的地方开始读取
                return false;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public void sendEndSign(int currentBytePos) {

        FileMessage longMessage = new FileMessage();
        longMessage.setCurrentBytePos(currentBytePos);
        longMessage.setThreadId(Thread.currentThread().getId());
        longMessage.setClientId(-1 + "");
        longMessage.setLastTimeStamp(System.currentTimeMillis());
        redisService.setBean(ParamConstants.REDIS_KEY, longMessage);
    }
}