# read-file-util
IO断点续读工具

     目标：尽量实现高可用，高一致性
     需求：对大量存储在文件中的类似Id/Name等常用数据进行读取。
     PS：每行一条数据，10W以上的数据，且应该使得对外的接口简洁       
            
            
             主要技术点：
 
             SpringBoot
             Spring-boot-starter-amqp
             Fastjson
             Jedis
             Google guava
             Apache HttpClient
             
            √：基本完成
            ×：未完成
            #：低优先级
            
             参数默认值与可配置处理 √
             获取请求封装为迭代器 √ 目前已经优化，返回对象字节流，由用户传入HTTP请求工具，和HTTP配置信息，并回调迭代器
             流读取分布式锁控制 √
             redis流状态分布式锁控制 √
             读取请求异步化 √
             迭代器hasNext临时缓存 √
             定时心跳检测 ×
             Eureka服务化  ×
             Rabbit、Redis持久化处理 ×
             RabbitMQ确认机制处理  #
             延迟重试优化 ×                   
             接口与实现分离 √
             迭代器请求优化 ×
             无效http连接自动清理 √
             迭代器依赖CoreHttpAPIService Bean,需要进一步分离 ×
             
 

粗略架构

![架构](https://github.com/jxnu-liguobin/read-file-util/blob/master/src/main/resources/images/IMG20180731_110807.png)
  
暴露的接口，这里数据的获取和迭代器返回同时存在时会打印两条相同数据，猜测主要是返回的迭代器含有http bean，导致的副作用

![暴露的接口](https://github.com/jxnu-liguobin/read-file-util/blob/master/src/main/resources/images/%E6%9A%B4%E9%9C%B2%E6%8E%A5%E5%8F%A3.png)          

核心读取方法

![核心读取方法](https://github.com/jxnu-liguobin/read-file-util/blob/master/src/main/resources/images/%E6%A0%B8%E5%BF%83%E8%AF%BB%E5%8F%96%E6%96%B9%E6%B3%95.png)

返回迭代器

![返回迭代器](https://github.com/jxnu-liguobin/read-file-util/blob/master/src/main/resources/images/%E8%BF%94%E5%9B%9E%E7%9A%84%E6%A0%B8%E5%BF%83%E8%BF%AD%E4%BB%A3%E5%99%A8.png)  
 
测试单机

![测试单机](https://github.com/jxnu-liguobin/read-file-util/blob/master/src/main/resources/images/%E6%B5%8B%E8%AF%95%E6%89%93%E5%8D%B0%E5%88%B0%E6%8E%A7%E5%88%B6%E5%8F%B0.png)

使用:
   
<del>1、配置ParamConstants下的path路径，或者编写client，发送http请求，携带uri（必须文件，且此时需要自己增加方法入参）</del><br>
1、配置application.properties中文件路径：filePath，发送频率：transmissionFrequenc，缓冲区大小：bufferSize【取决于一行数据的大小】    
2、使用UuidUtil类生产本地运行所需要的数据（默认long/int,String代码冗余过多，已经去掉，需要自己加上,同时config.isString=false改为true即可）
   
       1. 启动Redis 
       2. 启动RabbitMQ 
       3. 启动SpringBoot

   
<del>发送读取请求：http://localhost:8080/get</del><br>
<del>发送获取请求：http://localhost:8080/read</del><br>
以上方法需要由客户端发送2次请求，已废除

新接口：<br>
http://localhost:8080/read <br>
返回：
Iterator<List<String>>的实现类
         
遵守GPL3.0
