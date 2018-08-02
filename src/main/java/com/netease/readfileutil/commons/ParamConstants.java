package com.netease.readfileutil.commons;

/**
 * @date: 2018-07-27
 * @author: liguobin
 * @description:
 */
public class ParamConstants {

    public static final int TRANSMISSION_FREQUENCY = 10; //定义消息发送频率
    public static final int START_DELIMITER = 13; //开始字节
    public static final int END_DELIMITER = 10;//结束字节
    public static final int BUFFER_SIZE = 1024;//缓冲区大小
    public static final String IS_STRING = "true";//是否为String字节，false表示int long
    //public static boolean IS_ERROR = false;//是否错误处理线程
    public static final int ERROR_POS = 0;//错误的位置
    public static final String MODE = "r";//文件打开方式
    public static final int EXPIRE = 300000;//5分钟
    public static final String REDIS_KEY = "task";//redis消息键
    public static final double RATELIMITER = 1.0;//限流  每秒不超过500个


    public static final String FILE_QUEUE = "file_queue";
    public static final String FANOUT_EXCHANGE = "fanoutExchange";
//    public static final String ROUTINGKEY = "routingkey";

    public static final String path = "C:\\Users\\liguobin\\Desktop\\read-file-util\\read-file-util\\data.txt";

    //迭代器默认发送的请求地址
    public static final String HOST = "http://127.0.0.1:8080/read";
    public static final String INNER_HOST = "http://127.0.0.1:8080/get";
    public static final String CHARSET = "UTF-8";


}
