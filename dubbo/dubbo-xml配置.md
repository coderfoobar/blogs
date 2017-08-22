
# dubbo-xml配置

> Create Time : 2017年8月22日 Ref : http://dubbo.io/user-guide/configuration/xml.html

示例

provider.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
    xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://code.alibabatech.com/schema/dubbo http://code.alibabatech.com/schema/dubbo/dubbo.xsd">  
    <dubbo:application name="hello-world-app"  />  
    <dubbo:registry address="multicast://224.5.6.7:1234" />  
    <dubbo:protocol name="dubbo" port="20880" />  
    <dubbo:service interface="com.alibaba.dubbo.demo.DemoService" ref="demoServiceLocal" />  
    <dubbo:reference id="demoServiceRemote" interface="com.alibaba.dubbo.demo.DemoService" />  
</beans>
```

> 所有标签支持自定义参数，用于不同扩展点实现的特殊配置。

如：

```java
<dubbo:protocol name="jms">
    <dubbo:parameter key="queue" value="your_queue" />
</dubbo:protocol>
```

或：（2.1.0开始支持）

注意声明：xmlns="http://www.springframework.org/schema/p"

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
    xmlns:p="http://www.springframework.org/schema/p"
    xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://code.alibabatech.com/schema/dubbo http://code.alibabatech.com/schema/dubbo/dubbo.xsd">  
    <dubbo:protocol name="jms" p:queue="your_queue" />  
</beans>
```

Configuration Relation :

![dubbo config](./dubbo-config.jpg)

* 服务配置，用于暴露一个服务，定义服务的元信息，一个服务可以用多个协议暴露，一个服务也可以注册到多个注册中心。
* 引用配置，用于创建一个远程服务代理，一个引用可以指向多个注册中心。
* 协议配置，用于配置提供服务的协议信息，协议由提供方指定，消费方被动接受。
* 应用配置，用于配置当前的应用信息，不管该应用是提供者还是消费者。
* 模块配置，用于配置连接注册中心相关信息。
* 注册中心配置，用于配置连接注册中心相关信息。
* 监控中心配置，用于配置连接监控中心相关信息。
* 提供方的缺省值，当ProtocolConfig和ServiceConfig某属性没有配置时，采用此缺省值，可选。
* 消费防的缺省值，当ReferenceConfig某属性没有配置时，采用此缺省值，可选。
* 方法配置，用于ServiceConfig和ReferenceConfig指定方法级的配置信息。
* 用于指定方法参数配置。

Configuration Override:

![dubbo-config-override](./dubbo-config-override.jpg)

* 上图中以timeout为例，显示了配置的查找顺序，其他retries,loadbalance,actives等类似。
    * 方法级有限，接口级次之，全局配置再次之。
    * 如果级别一样，则消费方有限，提供方次之。
* 其中，服务提供方配置，通过URL经由注册中心传递给消费方。
* 建议有服务提供方设置超时，因为一个方法需要执行多长时间，服务提供放更清楚，如果一个消费方同时引用多个服务，就不需要关心每个服务的超时设置。
* 理论上ReferenceConfig的非服务标识配置，在ConsumerConfig，ServiceConfig，ProviderConfig均可以缺省配置。






