
# Spring 强制使用cglib进行代理

> Create Time : 2017年7月5日 Ref : http://www.cnblogs.com/signheart/p/6609638.html

Spring对AOP的支持：

* 如果目标对象实现了接口，默认情况下会采用JDK的动态代理实现AOP
* 如果目标对象实现了接口，可以强制使用CGLIB实现AOP
* 如果目标对象未实现接口，必须采用CGLIB库，Spring会自动在JDK动态代理和CGLIB之间转换

如果强制使用CGLIB实现AOP ？

* 添加CGLIB依赖
```xml
<dependency>
    <groupId>cglib</groupId>
    <artifactId>cglib</artifactId>
    <version>2.2.2</version>
</dependency>
```
* 在Spring配置文件中加入`<aop:aspectj-autoproxy proxy-target-class="true">`

JDK动态代理和CGLIB字节码生成之间的区别：

* JDK动态代理只能对实现接口的类生成代理，而不能针对类
* CGLIB是针对类实现代理，主要是对制定的类生成一个子类，覆盖其中的方法。因为是继承，所以该类最好不要声明成`final`