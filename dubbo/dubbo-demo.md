
# dubbo demo

> Create Time : 2017年8月22日 Ref : http://dubbo.io/#tutorial

通过一个简单的demo从代码层面介绍了如何使用dubbo开发一个远程通信服务。快速启动&源码下载，参见[Github](https://github.com/alibaba/dubbo)

通过这个例子，你将学会：

* 定义一个远程服务接口
* provider发布远程服务到注册中心
* consumer自动发现远程服务并完成服务调用

开始前请注意：

* 示例使用的是Dubbo推荐的[Spring配置方式](http://dubbo.io/user-guide/configuration/xml.html),要使用API配置请参考[API配置](http://dubbo.io/user-guide/configuration/api.html)

* 示例使用[多播模式](http://dubbo.io/user-guide/reference-registry/multicast.html)实现服务自动注册与发现，生产环境中，一般要部署单独的[注册中心](http://dubbo.io/user-guide/reference-registry/introduction.html)

## 定义接口

定义服务接口：（该接口需要单独打包，在服务提供方和消费放共享）

DemoService.java

```java
package com.alibaba.dubbo.demo;

public interface DemoService {
    String sayHello(String name);
}
```

## Provider实现

服务提供方实现接口（对服务消费方隐藏实现）：

DemoServiceImpl.java

```java
package com.alibaba.dubbo.demo.provider;

import com.alibaba.dubbo.demo.DemoService;

public class DemoServiceImpl implements DemoService {
    public String sayHello (String name) {
        return "Hello " + name;
    }
}
```

用Spring配置声明暴露服务：

dubbo-demo-provider.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
    xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://code.alibabatech.com/schema/dubbo http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

    <!--提供方应用信息，用于计算依赖关系-->
    <dubbo:application name="demo-provider" />

    <!--使用multicast广播注册中心暴露服务地址-->
    <demo:registry address="multicast://224:5.6.7:12" />

    <!--用dubbo协议在20880端口暴露服务-->
    <dubbo:protocol name="dubbo" port="20880" />

    <!--声明需要暴露的服务接口-->
    <dubbo:service interface="com.alibaba.dubbo.demo.DemoService" ref="demoService" />

    <!--和本地bean一样实现服务-->
    <bean id="demoService" class="com.alibaba.dubbo.demo.provider.DemoServiceImpl" />

</beans>
```

加载Spring配置

Provider.java

```java
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Provider {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"META-INFO/spring/dubbo-demo-provider.xml"});

        context.start();

        System.in.read(); //按任意键退出
    }
}
```

## Consumer实现

通过Spring配置引用远程服务：

dubbo-demo-consumer.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
    xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://code.alibabatech.com/schema/dubbo http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

    <!--消费方应用名，用于计算依赖关系，不是匹配条件，不要与提供方一样-->
    <dubbo:application name="demo-consumer" />

    <!--使用multicast广播注册中心暴露发现服务地址-->
    <dubbo:registy address="multicast://224.5.6.7:1234" />

    <!--生成远程服务代理，可以和本地bean一样使用demoService-->
    <dubbo:reference id="demoService" interface="com.alibaba.dubbo.demo.DemoService" />
</beans>
```

加载Spring配置，并调用远程服务：（也可以使用IoC注入）

Consumer.java

```java
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.alibaba.dubbo.demo.DemoService;

public class Consumer {
    public static void main(String args){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/dubbo-demo-consumer.xml");

        context.start();

        //获取远程服务代理
        DemoService demoService = (DemoService)context.getBean("demoService");
        String hello = demoService.sayHello("world");

        System.out.println(hello);
    }
}
```

