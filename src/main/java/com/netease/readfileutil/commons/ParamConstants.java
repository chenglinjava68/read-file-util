package com.netease.readfileutil.commons;

/**
 * @date: 2018-07-27
 * @author: liguobin
 * @description:系统主要常量定义，不配application.properties则以这里的默认值为准
 */
public class ParamConstants {

    //文件读取控制配置
    public static final int TRANSMISSION_FREQUENCY = 10; //定义消息发送频率，根据系统IO的性能设置
    public static final int START_DELIMITER = 13; //开始字节判定
    public static final int END_DELIMITER = 10;//结束字节判定
    public static final int BUFFER_SIZE = 1024;//缓冲区大小，数据大则需要增加该值
    public static final String REDIS_KEY = "task";//redis消息键，表示最新任务的状态
    public static final String IS_STRING = "true";//是否为String字节，false表示int long，默认已经去掉String的代码

    //以下基本不需要修改
    //public static boolean IS_ERROR = false;//是否错误处理线程，现在的方案是不考虑使用多线程，而是采用多实例，所以不需要这个了，多线程状态控制太难了，老大说用多服务实例就行了
    public static final int ERROR_POS = 0;//错误的位置，实时获取
    public static final String MODE = "r";//文件打开方式，默认以读取的方式
    public static final int EXPIRE = 300000;//5分钟，一个clientId最大可用时间，超过则无法继续更新/发送数据，通过检查这个注册中心可以判断服务是否存活，并把请求代理到其他存活的机器上
    public static final double RATELIMITER = 1.0;//限流  实际运用时老大说每秒不超过500个，测试设置的小一点


    //队列路由配置
    public static final String FILE_QUEUE = "file_queue"; //消息队列
    public static final String FANOUT_EXCHANGE = "fanoutExchange";//交换机
    //public static final String ROUTINGKEY = "routingkey";//路由键，默认关闭，采用广播


    //迭代器配置
    public static final String HOST = "http://127.0.0.1:8080/read";//暴露给外界的读取接口地址
    public static final String INNER_HOST = "http://127.0.0.1:8080/get";//内部迭代器所需要的数据拉取地址
    public static final String CHARSET = "UTF-8";//默认编码方式配置
    public static final String PATH = "D:\\read-file-util\\data.txt";//默认文件地址，当且仅当测试时应该使用

    //拉取线程暂停时间
    public static final Integer SLEEP_TIME = 1000;//只有在生成者实在太慢的情况下才有必要设置,实际情况下几乎不可能，本地测试发现，生产者实在太慢可能导致消费者hasNext提前退出循环，后续处理可能需要加上重试

    //redis分布式锁
    public static final long OVER_TIME = 2000; //单位毫秒
    public static final String LOCK_KEY = "lock_key"; //核心处理锁
    public static final String MESSAGE_STATUS_KEY = "message_staus_key"; //状态查询锁


}
