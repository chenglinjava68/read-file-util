package com.netease.readfileutil.core;

import com.netease.readfileutil.commons.ParamConstants;
import com.netease.readfileutil.config.FileConfigBean;
import com.netease.readfileutil.rabbitmq.FileMessage;
import com.netease.readfileutil.rabbitmq.MQSender;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @date: 2018-07-31
 * @author: liguobin
 * @description:
 */
@Component
@Order
public class CoreReadFileUtil {

    private static final Logger log = LoggerFactory.getLogger(CoreReadFileUtil.class);
    private static final Lock lock = new ReentrantLock();

    @Autowired
    private FileConfigBean fileConfigBean;

    @Autowired
    private MQSender mqSender;

    @Autowired
    private RedisService redisService;


    private List<String> longList = new ArrayList<>();
    //private int currentBytePos = 0;
    RandomAccessFile fileInputStream = null;
    private static AtomicInteger count = new AtomicInteger(0);
    private static volatile AtomicInteger atomicInteger = new AtomicInteger(0);

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
                            lineNumber++;
                            log.info("int/long :total:" + totalPos + ",line:" + numberInLong + ",lineNumber:" + lineNumber);
                            count.incrementAndGet();
                            //目前只能处理数据量大于发送频率的
                            if (count.intValue() % fileConfigBean.getTransmissionFrequenc() == 0 ||
                                    (count.intValue() > fileConfigBean.getTransmissionFrequenc()
                                            && count.intValue() - fileConfigBean.getTransmissionFrequenc() * atomicInteger.intValue() > 0)) {
                                atomicInteger.incrementAndGet();
                                //发送消息到mq
                                FileMessage longMessage = new FileMessage();
                                longMessage.setCurrentBytePos(totalPos);
                                longMessage.setThreadId(Thread.currentThread().getId());
                                longMessage.setClientId(client);
                                longMessage.setLastTimeStamp(System.currentTimeMillis());
                                //更新reids中的
                                if (isCanUpdate(client)) {
                                    redisService.setBean(ParamConstants.REDIS_KEY, longMessage);
                                } else {
                                    //不是本客户端或者本客户端超时
                                    //下次还是得从上次开始的地方开始读取
                                    return;
                                }
                                longMessage.setList(longList);
                                mqSender.send(longMessage);
                                count = new AtomicInteger(0);

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
            //读完标记redis中为结束状态
            FileMessage nullMessage = new FileMessage();
            nullMessage.setCurrentBytePos(totalPos);
            nullMessage.setLastTimeStamp(System.currentTimeMillis());
            nullMessage.setClientId(-1 + "");
            nullMessage.setThreadId(Thread.currentThread().getId());
            redisService.setBean(ParamConstants.REDIS_KEY, nullMessage);//将key变为不过期
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isCanUpdate(String client) {
        try {
            lock.tryLock(2000, TimeUnit.MILLISECONDS);
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return true;
    }
}