package com.netease.readfileutil.commons;

/**
 * @date: 2018-07-27
 * @author: liguobin
 * @description:
 */
public class ParamConstants {

    //文件读取控制配置
    public static final int TRANSMISSION_FREQUENCY = 10; //定义消息发送频率
    public static final int START_DELIMITER = 13; //开始字节
    public static final int END_DELIMITER = 10;//结束字节
    public static final int BUFFER_SIZE = 1024;//缓冲区大小
    public static final String REDIS_KEY = "task";//redis消息键
    public static final String IS_STRING = "true";//是否为String字节，false表示int long

    //以下基本不需要修改
    //public static boolean IS_ERROR = false;//是否错误处理线程
    public static final int ERROR_POS = 0;//错误的位置
    public static final String MODE = "r";//文件打开方式
    public static final int EXPIRE = 300000;//5分钟
    public static final double RATELIMITER = 1.0;//限流  每秒不超过500个


    //队列路由配置
    public static final String FILE_QUEUE = "file_queue";
    public static final String FANOUT_EXCHANGE = "fanoutExchange";
    //public static final String ROUTINGKEY = "routingkey";


    //迭代器配置
    public static final String HOST = "http://127.0.0.1:8080/read";
    public static final String INNER_HOST = "http://127.0.0.1:8080/get";
    public static final String CHARSET = "UTF-8";
    public static final String path = "D:\\read-file-util\\data.txt";

    //拉取线程暂停时间，只有在生成者实在太慢的情况下才有必要设置,实际情况下几乎不可能
    public static final Integer SLEEP_TIME = 1000;

    //redis分布式锁
    public static final long OVER_TIME = 2000; //单位毫秒
    public static final String LOCK_KEY = "lock_key"; //核心处理锁
    public static final String MESSAGE_STATUS_KEY = "message_staus_key"; //状态查询锁


}
