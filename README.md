# 手写rpc的demo

### 一、介绍
手写RPC的Demo，主要流程如下所示：
#### 服务端（RpcServer）
* ①项目启动的时候，通过`Startup.java`类实现`CommandLineRunner`接口，来启动服务
* ②服务主要是基于ServerSocket来实现
* ③服务启动之前，扫描`自定义包`路径下，并且含有`@Rpc`注解的类，把它们注册到Zookeeper
* ④ServerSocket通过反射进行处理客户端发起的请求，并且响应

#### 客户端（RpcClient）
* ①接口调用的时候，通过动态代理来进行处理，动态代理所做的工作如下：
* ②根据`接口类`去Zookeeper获取对应的服务信息
* ③通过Socket发起请求
* ④获取服务端响应信息


### 二、项目运行
* ①获取源码到本地
* ②本地安装Zookeeper
* ③修改RpcServer/src/main/resource/application.properties下的`host`信息
* ④修改RpcClient/src/main/resource/application.properties下的`host`信息
* ⑤分别启动RpcServer和RpcClient，这两个工程是基于SpringBoot实现的，直接运行启动类即可
* ⑥访问RpcClient的Controller类，浏览器运行`http://127.0.0.1/RpcClient/test/sayHello?name=test`

### 三、推荐看以下几篇文章
     **以下几篇文章是我额外写的，有助于理解RPC** 

    1、RPC的原理讲解
    https://www.imooc.com/article/303057

    2、负载均衡算法的讲解
    https://www.imooc.com/article/303061

    3、序列化和反序列化的讲解
    https://www.imooc.com/article/303099

# 微信公众号开通了，坚持手写有技术含量的原创文章
![输入图片说明](https://images.gitee.com/uploads/images/2020/0407/231510_6b308b9b_798389.jpeg "二维码.jpg")