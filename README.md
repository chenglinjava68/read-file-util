# read-file-util
IO断点续读工具

     目标：实现高可用，高一致性
     需求：对大量存储在文件中的类似Id/Name等常用数据进行读取。
     PS：每行一条数据，10W以上的数据，且应该使得对外的接口简洁       
            
            
             主要技术点：
 
             SpringBoot
             Spring-boot-starter-amqp
             Fastjson
             Jedis
             Google guava
             Apache HttpClient
             
            
            
             获取请求封装为迭代器√
             读取请求异步化 √
             定时心跳检测 ×
             Eureka服务化  ×
             Rabbit、Redis持久化处理 ×
             延迟重试优化 ×                   
             接口与实现分离 ×
             迭代器请求优化 ×
 

粗略架构
![架构](https://github.com/jxnu-liguobin/read-file-util/blob/master/src/main/resources/images/IMG20180731_110807.png)
  
暴露的接口
![暴露的接口](https://github.com/jxnu-liguobin/read-file-util/blob/master/src/main/resources/images/%E6%9A%B4%E9%9C%B2%E6%8E%A5%E5%8F%A3.png)          

核心读取方法
![核心读取方法](https://github.com/jxnu-liguobin/read-file-util/blob/master/src/main/resources/images/%E6%A0%B8%E5%BF%83%E8%AF%BB%E5%8F%96%E6%96%B9%E6%B3%95.png)

返回迭代器
![返回迭代器](https://github.com/jxnu-liguobin/read-file-util/blob/master/src/main/resources/images/%E8%BF%94%E5%9B%9E%E7%9A%84%E6%A0%B8%E5%BF%83%E8%BF%AD%E4%BB%A3%E5%99%A8.png)  
 
测试单机
![测试单机](https://github.com/jxnu-liguobin/read-file-util/blob/master/src/main/resources/images/%E6%B5%8B%E8%AF%95%E6%89%93%E5%8D%B0%E5%88%B0%E6%8E%A7%E5%88%B6%E5%8F%B0.png)

   使用:</br>
   1.启动Redis </br>
   2.启动RabbitMQ </br>
   3.启动SpringBoot </br>

   
   <del>发送读取请求：http://localhost:8080/get</del><br>
   <del>发送获取请求：http://localhost:8080/read</del><br>
   以上方法需要由客户端发送2次请求，已废除
   
   新接口：</br>
   http://localhost:8080/read <br>
   返回：
   Iterator<List<String>>的实现类
             
     遵守GPL3.0
