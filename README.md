## 项目介绍

秒杀场景无非就是多个用户在同时抢购一件或者多件商品，专用词汇就是所谓的高并发。

### 业务特点

+ 瞬间高并发、电脑旁边的小哥哥、小姐姐们如超市哄抢的大妈一般，疯狂的点着鼠标
+ 库存少、便宜、稀缺限量，值得大家去抢购，如苹果肾，小米粉，锤子粉(理解万岁)

### 用户规模

用户规模可大可小，几百或者上千人的活动单体架构足以可以应付，简单的加锁、进程内队列就可以轻松搞定。一旦上升到百万、千万级别的规模就要考虑分布式集群来应对瞬时高并发。

### 秒杀框架

![](https://s2.ax1x.com/2019/08/23/mr5KO0.png)

#### 优化思路

+ 限流、毕竟秒杀商品有限，防刷的前提下没有绝对的公平，根据每个服务的负载能力，设定流量极限。
+ 缓存、尽量不要让大量请求穿透到DB层，活动开始前商品信息可以推送至分布式缓存。

#### 分层优化

+ 前端优化：尽量使用静态页面推送缓存，少量数据异步提交
+ 后台优化：使用Redis缓存，减小数据库压力

### 代码结构

```
├─src
│  ├─main
│  │  ├─java
│  │  │  └─top
│  │  │      └─arrietty
│  │  │          │  Main.java
│  │  │          │  config 
│  │  │          │  controller 
│  │  │          │  dao
│  │  │          │  exception
│  │  │          │  rabbitmq
│  │  │          │  redis
│  │  │          │  result
│  │  │          │  service
│  │  │          │  validator
│  │  │          │  vo
```



