# 高可用远程过程调用框架——Hrpc

## 一、介绍
Hrpc 是一款高可用的远程过程调用（RPC）框架，旨在为分布式系统提供高效、可靠的服务通信解决方案。通过灵活的服务注册与发现、智能的负载均衡机制及强大的故障恢复能力，Hrpc 助力开发者快速构建高可用的微服务架构。
## 二、主要特性
### 1. 高可用性
Hrpc 采用动态服务注册与健康检查机制，确保客户端始终能够发现可用的服务实例。即使在部分服务出现故障时，系统仍能保持稳定运行。

### 2. 灵活的负载均衡
Hrpc 支持多种负载均衡策略，包括轮询、随机、最少连接等，能够根据实际负载情况智能分配请求，提高系统的整体性能和响应速度。

### 3. 故障转移与熔断
Hrpc 内置重试机制和熔断器模式，能够在服务故障时自动切换到其他健康实例，减少请求失败的概率，保护系统资源。

### 4. 性能优化
Hrpc 支持异步调用和高效的线程池管理，能够处理高并发请求，最大限度提升系统的吞吐量和响应速度。

### 5. 安全与监控
Hrpc 提供 TLS/SSL 加密通信，确保数据传输安全。同时，支持与主流监控工具集成，实时监控服务性能和健康状态，及时发现并处理异常。

### 6. 应用场景
Hrpc 适用于各种需要高可用性和高性能的分布式系统场景，如微服务架构、云原生应用、大数据处理等。无论是大型企业系统还是初创项目，Hrpc 都能为你的服务通信提供强有力的支持。
## 三、基本架构
### 服务端（RpcServer）
* ①项目启动的时候，通过`Startup.java`类实现`CommandLineRunner`接口，来启动服务
* ②服务主要是基于ServerSocket来实现
* ③服务启动之前，扫描`自定义包`路径下，并且含有`@Rpc`注解的类，把它们注册到Zookeeper
* ④ServerSocket通过反射进行处理客户端发起的请求，并且响应

### 客户端（RpcClient）
* ①接口调用的时候，通过动态代理来进行处理，动态代理所做的工作如下：
* ②根据`接口类`去Zookeeper获取对应的服务信息
* ③通过Socket发起请求
* ④获取服务端响应信息


## 四、项目运行
* ①获取源码到本地
* ②本地安装Zookeeper
* ③修改RpcServer/src/main/resource/application.properties下的`host`信息
* ④修改RpcClient/src/main/resource/application.properties下的`host`信息
* ⑤分别启动RpcServer和RpcClient，这两个工程是基于SpringBoot实现的，直接运行启动类即可
* ⑥访问RpcClient的Controller类，浏览器运行`http://127.0.0.1/RpcClient/test/sayHello?name=test`

#### 参考
    1、RPC的原理讲解
    https://www.imooc.com/article/303057
    2、负载均衡算法的讲解
    https://www.imooc.com/article/303061
    3、序列化和反序列化的讲解
    https://www.imooc.com/article/303099
