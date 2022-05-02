# 枚举类型的使用
# @Valid
# 异常处理
# @CookieValue(value = "sessionId", required = false)
# 借助redis和spring session解决session丢失(只需要添加依赖，配置redis即可)
# 直接将用户信息保存到redis中
# key相同的cookie，如果不是同一个地址产生的是不会覆盖的
# 设置redis序列化，返回的RedisTemplate的方法必须是redisTemplate
# 自定义参数解析器，在请求到达controller之前对参数进行处理，比如登录状态验证，避免每次到达controller后自己编写代码。可以解析成需要的参数类型
# mvc配置类
# 秒杀需要满足两个条件
# 插件
## 根据mapper接口直接生成sql标签
## 为对象设置属性时，生成所有setter方法
# 为什么要将商品表和秒杀商品表分开
# 部署springboot项目，直接打成jar包，执行java -jar jar包名称


# 单点登录
## spring session + redis
## redis

# 错误
## 逆向工程生成的是RestController注解，导致html中的文件无法识别
## 打包时没有将yml编译到target里面，打包后在本地运行成功后再上传到服务器
## 出现了莫名其妙的错误，尤其是添加了maven插件以后，检查一下target里面是不是少内容，如yml
## 应该是对的，但是没有得到想要的结果，可能是服务没重启
## 超卖   保证判断库存和修改库存是原子操作
## 重复购买

# 简历
## 登录功能
### redis实现单点登录，使用参数解析器将参数解析成方法需要的类型
## 秒杀功能
### 超卖问题，mysql将判断和减库存设置为原子操作，redis先减1，再判断是否还有库存
### 重复购买问题，除了设置唯一索引，还将购买的用户放入redis中，可以提前阻止一部分重复秒杀
## 压力测试
### 使用jmeter，不同用户
## 优化
### 在redis中减库存，降低数据库的访问
#### 在对应的controller初始化完成后，将商品及库存保存到redis中
#### 秒杀时利用redis的-1函数预减库存
### 内存标记，避免库存为0后，依旧对redis进行访问
### 下单请求进入mq
#### 限流
#### 异步下单，减少等待时间（成功后可通过短信邮件等方式提醒）