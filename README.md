# read-file-util
IO断点续读工具

     目标：实现高可用，高一致性
     需求：对大量存储在文件中的类似Id/Name等常用数据进行读取。PS：每行一条数据，10W以上的数据


          
            
            
            
            
             主要技术点：
 
             SpringBoot
             Spring-boot-starter-amqp
             Fastjson
             Jedis
             Google限流工具
             
        
             待处理部分：Eureka服务化，心跳检测，Rabbit,Redis持久化处理，延迟重试优化，接口与实现分离
