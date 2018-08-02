# read-file-util
IO断点续读工具

     目标：实现高可用，高一致性
     需求：对大量存储在文件中的类似Id/Name等常用数据进行读取。PS：每行一条数据，10W以上的数据         
            
            
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
