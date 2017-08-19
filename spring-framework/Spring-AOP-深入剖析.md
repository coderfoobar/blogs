
# Spring AOP 深入剖析

> Create Time : 2017年8月19日 Ref : http://blog.csdn.net/derrantcm/article/details/46284811

AOP是Spring提供的关键特性之一。AOP即面向切面变成，是OOP编程的有效补充。使用AOP技术，可以将一些系统性相关的编程工作，独立提取出来，独立实现，然后通过切面切入进系统。从而避免了在业务逻辑的代码中混入很多的系统相关的逻辑--比如权限管理，事务管理，日志记录等等。这些系统性的编程工作都可以独立编码实现，然后通过AOP技术切入进系统即可。从而达到了`关注点分离`的效果。

## AOP相关概念

1. `Aspect` - 切面，切入一个系统的切面，比如事务管理是一个切面，权限管理是一个切面；
2. `Join Point` - 连接点，也就是可以进行横向切入的位置；
3. `Advice` - 通知 ， 切面在某个连接点执行的操作(分为: Before , After returning , After Throwing , After (final) , Around )
4. `Pointcut` - 切点，符合切点表达式的连接点，也就是真正被切入的地方。

## AOP的实现原理

AOP 分为静态AOP和动态AOP，静态AOP是指AspectJ实现的AOP，他是将切面代码直接编译到Java类文件中，动态AOP是指将切面代码进行动态织入的实现的AOP。Spring的AOP为动态AOP，实现技术为： `JDK提供的动态代理技术`和`CGLIB（动态字节码增强技术）`。尽管实现技术不一样，但`都是基于代理模式，都是生成一个代理对象。`

### JDK 动态代理

主要用到InvocationHandler接口和Proxy.newProxyInstance()方法。`JDK动态代理要求被代理实现一个接口，只有接口中的方法才能被代理`。其方法是将被代理对象注入到一个中间对象，而中间对象实现InvocationHandler接口，在实现该接口时，可以在被代理对象调用它的方法时，在调用的前后插入一些代码。而Proxy.newProxyInstance()能够利用中间对象来生产代理对象。插入的代码就是切面代码。所以使用JDK动态代理可以实现AOP。我们看个例子：

被代理对象实现的接口，只有接口中的方法才能够被代理：

```Java
public interface UserInterface {
    public void addUser(User user);
    public User getUser(int id);
}
```

被代理对象：

```Java
public class UserServiceImpl implements UserService {
    public void addUser(User user){
        System.out.println("add user into database.");
    }

    public User getUser(int id) {
        User user = new User();
        user.setId(id);
        System.out.println("get user from database.");
        return user;
    }
}
```

代理中间件:

```Java
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyUtil implements InvocationHandler {
    private Object target ; //被代理的对象
    
    public Object invoke(Object proxy , Method method, Object[] args) 
     throws Throwable {
         System.out.println("do sth before ... ");
         Object result = method.invoke(target , args);
         System.out.println("do sth after ...")
         return result;
    }

    ProxyUtil(Object target) {
        this.target = target;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget (Object target) {
        this.target = target;
    }

}
```

测试:

```Java
import java.lang.reflet.Proxy;
import net.aazj.pojo.User;

public class ProxyTest {
    public static void main(String[] args) {
        Object proxyObject = new UserServiceImpl();// 被代理的对象
        ProxyUtil proxyUtils = new ProxyUtil(proxyedObject);
        
        //生成代理对象，对被代理对象的这些接口进行代理: //UserServiceImpl.class.getInterfaces()
        UserService proxyObject = (UserService)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),UserServiceImpl.class.getInterfaces(),proxyUtils);
        proxyObject.getUser();
        proxyObject.addUser(new User());
    }
}
```

执行结果：

```
do sth before....
getUser from database.
do sth after....
do sth before....
add user info database.
do sth after....
```

我们看到在UserService接口中的方法addUser和getUser方法前面插入了我们自己的代码。这就是JDK动态代理实现AOP的原理。

我们看到该方式有一个要求： `被代理对象必须实现接口，而且只有接口方法才能被代理`。

### CGLIB （code generate library）

字节码生成技术实现AOP，其实就是`继承被代理对象，然后Override需要被代理的方法`，在覆盖该方法时，自然是可以插入我们自己的代码的。因为需要Override被代理对象的方法，所以自然CGLIB技术实现AOP时，就`必须要求需要被代理的方法不能是final方法，被代理的类不能是final类`。我们使用CGLIB的实现上边的例子：

```Java
package net.aazj.aop;

import java.lang.reflet.Method;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CGProxy implements MethodInterceptor {
    private Object target; //被代理对象
    public CGProxy (Object target) {
        this.target = target;
    }

    public Object intercept(Object obj,Method method,Object[] args,MethodProxy proxy) throws Throwable {
        System.out.println("do sth before....");
        Object result = proxy.invokeSuper(obj,args);
        System.out.println("do sth after....");
        return result;
    }

    public Object getProxyObject() {
        Enhancer Enhancer = new Enhancer();
        enhancer.setSuperclass(this.target.getClass());//设置父类
        //设置回调
        enhancer.setCallback(this);//在调用父类方法时，回调this.intercept()
        //创建代理对象
        return enhancer.creaet();
    }

}
```

```Java
public class CGProxyTest {
    public static void main(String[] args) {
        Object proxyedObject = new UserServiceImpl(); //被代理的对象
        CGProxy cgProxy = new CGProxy(proxyedObject);
        UserService proxyObject = (UserService) cgProxy.getProxyObject();
        proxyObject.getUser(1);
        proxyObject.addUser(new User());
    }
}
```

输出结果:

```Java
do sth before....
getUser from database.
do sth after....
do sth before....
add user into database.
do sth after....
```

我们看到达到了同样的效果。它的原理是生成一个父类enhancer.setSuperclass(this.target.getClass())的子类enhancer.create()，然后对父类的方法进行拦截enhancer.setCallback(this).对父类的方法进行覆盖，所以父类方法不能是final的。

### Spring 实现AOP的相关源码

```Java
@SupperWarnings("serial")
public class DefaultAopProxyFactory implements AopProxyFactory , Serializable {

    @Override
    public AopProxy createAopProxy (AdvisedSupport config) throws    AopConfigException {
        if (config.isOptimize() 
            || config.isProxyTargetClass()
            || hasNoUserSuppliedProxyInterfaces(config)) {
            
            Class<?> targetClass = config.getTargetClass();
            if (targetClass == null) {
                throw new AopConfigException("TargetSource cannot determine target class : Either an interface or a target is required for proxy creation.");
            }

            if (targetClass.isInterface()) {
                return new JdkDynamicAopProxy(config);
            }
            return new ObjenesisCglibAopProxy(config);
        }
        return new JdkDynamicAopProxy(config);
    }
}
```

从上面的源码可以看到:

```Java
if (targetClass.isInterface()) {
    return new JdkDynamicAopProxy(config);
}
return new ObjenesisCglibAopProxy(config);
```

如果被代理对象实现了接口，那么就使用JDK的动态代理技术，反之则使用CGLIB来实现AOP，所以`Spring默认是使用JDK的动态代理技术实现AOP的`。

JdkDynamicAopProxy的实现其实很简单：

```Java
final class JdkDynamicAopProxy implements AopProxy , InvocationHandler , Serializable {
    @Override
    public Object getProxy (ClassLoader classLoader) {
        if (logger.isDebugEnabled()) {
            logger.debug("Creating JDK dynamic proxy : target source is " + this.advised.getTargetSource());
        }
        Class<?>[] proxiedInterfaces = AopProxyUtils.completeProxiedInterfaces(this.advised);
        findDefinedEqualsAndHashCodeMethods(proxiedInterfaces);
        return Proxy.newProxyInstance(classLoader , proxiedInterfaces , this);
    }
}
```

## Spring AOP的配置

Spring中AOP的配置一般有两种方法，一种是使用<aop:config>标签在xml中进行配置，一种是使用注解以及@Aspect风格的配置。

### 基于<aop:config>的AOP配置

下面是一个典型的事务AOP的配置：

```xml
<tx:advice id="transactionAdvice" transaction-manager="transactionManager">
    <tx:attributes>
        <tx:method name="add*" propagation="REQUIRED" />
        <tx:method name="append*" propagation="REQUIRED" />
        <tx:method name="insert*" propagation="REQUIRED" />
        <tx:method name="save*" propagation="REQUIRED" />
        <tx:method name="update*" propagation="REQUIRED" />

        <tx:method name="get*" propagation="REQUIRED" />
        <tx:method name="find*" propagation="REQUIRED" />
        <tx:method name="load*" propagation="REQUIRED" />
        <tx:method name="search*" propagation="REQUIRED" />
    </tx:attributes>
</tx:advice>

<aop:config>
    <aop:pointcut id="transactionPointcut" expression="execution(* net.aazj.service..*Impl.*(..))" />
    <aop:advisor pointcut-ref="transactionPointcut" advice-ref="transactionAdvice" />
</aop:config>
```

再看一个例子：

```xml
<bean id="aspectBean" class="net.aazj.aop.DataSourceInterceptor" />

<aop:config>
    <aop:aspect id="dataSourceAspect" ref="aspectBean">
        <aop:pointcut id="dataSourcePoint" expression="execution(public * net.aazj.service..*.getUser(..))" />
        <aop:pointcut expression="" id="" />
        <aop:before method="before" pointcut-ref="dataSourcePoint" />
        <aop:after method="" />
        <aop:around method="" />
    </aop:aspect>

    <aop:aspect>
    </aop:aspect>
</aop:config>
```

<aop:aspect>配置一个切面，<aop:pointcut>配置一个切点，基于切点表达式；<aop:before>,<aop:after>,<aop:around>是具体定义不同类型的advise，aspectBean是切面处理bean ： 

```Java
public class DataSourceInterceptor {
    public void before(JoinPoint jp) {
        DataSourceTypeManager.set(DataSources.SLAVE);
    }
}
```

### 基于注解和@Aspect风格的AOP配置

我们以事务配置为例：首先我们启用基于注解的事务配置：

```xml
<!--使用annotation定义事务-->
<tx:annotation-driver transaction-manager="transactionManager" />
```

然后扫描Service包：

```Java
<context:component-scan base-package="net.aazj.service,net.aazj.aop" />
```

最后在Service上进行注解：

```Java
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Transactional (readOnly=true)
    public User getUser(int userId) {
        System.out.println("in UserServiceImpl getUser");
        System.out.println(DataSourceInterceptor.get());
        return userMapper.getUser(userId);
    }

    public void addUser(String username) {
        userMapper.addUser(username);
    }

    public void deleteUser(int id) {
        userMapper.addUser(username);
        // int i = 1/0; 测试事务回滚
    }

    public void deleteUser(int id) {
        userMapper.deleteByPrimaryKey(id);
        // int i = 1/0; 测试事务回滚
    }

    @Transactional(rollback = BaseBusinessException.class)
    public void addAndDeleteUser(String username,int id) throws BaseBusinessException {
        userMapper.addUser(username);
        this.m1();
        userMapper.deleteByPrimaryKey(id);
    }

    private void m1() throws BaseBusinessException{
        throw new BaseBusinessException("xxx");
    }

    public int insertUser(User user) {
        return this.userMapper.insert(user);
    }
}
```

搞定。这种事务配置方式，不需要我们书写pointcut表达式，而是我们在需要事务的类上进行注解。但是如果我们自己来写切面的代码时，还是要写pointcut表达式。下面看一个例子（自己写切面逻辑）：

首先，去扫描@Aspect注解定义的切面：

```xml
<context:component-scan base-package="net.aazj.aop" />
```

启用@Aspect风格的注解：

```xml
<aop:aspectj-autoproxy />
```

这里有两个属性，`<aop:aspectj-autoproxy proxy-target-class="true" expose-proxy="true" />`， `proxy-target-class="true"`这个最好不要随便使用，他是指定只能使用CGLIB代理，`那么对于final方法时会抛出错误`，所以还是让spring自己选择是使用JDK动态代理，还是CGLIB， `expose-proxy="true" `的作用后面会讲到。

切面代码：

```Java
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect //for aop
@Component // for auto scan
@Order(0) // execute before @Transactional
public class DataSourceInterceptor {
    @Pointcut("execution(public * net.aazj.service.service..*.get*(...))")
    public void dataSourceSlave(){};

    @Before 
    public void before(JoinPoint jp) {
        DataSourceTypeManager.set(DataSources.SLAVE);
    }
}
```

我们使用了`@Aspect来定义一个切面；@Component是配置<context:component-scan />，不然扫描不到；@Order定义了该切面切入的顺序`，因为在同一个切点，可能存在多个切面，那么在这多个切面之间就存在一个执行顺序的问题。该例子是一个切换数据源的切面，那么他应该在事务处理切面之前执行，所以我们使用@Order(0)来确保先切换数据源，然后加入事务处理。@Order的参数越小，优先级越高，默认的优先级最低：

```Java
/**
 * Annotation that defines ordering. The value is optional, and represents order value
 * as defined in the {@link Ordered} interface. Lower values have higher priority.
 * The default value is {@code Ordered.LOWEST_PRECEDENCE}, indicating
 * lowest priority (losing to any other specified order value).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface Order {
    /**
     * The order value. Default is {@link Ordered#LOWEST_PRECEDENCE}.
     * @see Ordered#getOrder()
     */
    int value() default Ordered.LOWEST_PRECEDENCE;
}
```
 
关于数据源的切换可以参考下面专门的博文：http://www.cnblogs.com/digdeep/p/4512368.html

### 切点表达式(pointcut)

上面我们看到，无论是<aop:config>风格的配置，还是@Aspect风格的配置，切点表达式都是重点，都是我们必须掌握的。

上面我们看到，无论是<aop:config>风格的配置，还是@Aspect风格的配置，切点表达式都是重点。都是我们必须要掌握的。

1. pointcut的语法形式(execution):

```
execution (modifiers-pattern? ret-type-pattern declaring-type-pattern? name-pattern(param-pattern) throws-pattern?)
```

带有?号的部分是可选的，所以可以简化成：`ret-type-pattern name-pattern(param_pattern)返回类型，方法名称，参数三部分来匹配`。

配置起来其实也很简单：`* 表示任意的返回类型，任意方法名，任意一个参数类型；..连续两个点表示0个或多个包路径，还有0个或者多个参数`。就是这么简单，看下例子：

`execution(* net.aazj.service..*.get(..)) ` : 表示AccountService接口下的任何方法，参数不限；

注意这里，将类名和包路径是一起来处理的，并没有进行区分，因为类名也是包路径的一部分。

参数param-pattern部分比较复杂： `()表示没有参数，(..)表示参数不限，(*,String)第一个参数不限类型，第二个参数为String`。

2. within()语法

within()只能指定（限定）包路径（类名也可看作是包路径），表示某个包下或者子包下的所有方法。
```
within(net.aazj.service.*)
within(net.aazj.service..*)
within(net.aazj.service.UserServiceImpl.*)
```

3. this()和target()

this是指代理对象，target是指被代理对象（目标对象），所以this()和target()分别限定代理对象的类型和被代理对象的类型：

`this(net.aazj.service.UserService)` - 实现了UserService的代理对象（中的所有方法）；
`target(net.aazj.service.UserService)` - 被代理对象实现了UserService(中的所有方法)；

4. args()

限定方法的参数的类型：

`args(net.aazj.pojo.User)` - 参数为User类型的方法。

5. @target() , @within() , @annotation() , @args()

`这些语法形式都是针对注解的，比如带有某个注解的类，带有某个注解的方法，参数的类型带有某个注解` 


`@within(org.springframework.transaction.annotation.Transactional)`

`@target(org.springframework.transaction.annotation.Transactional)`

两者都是指被代理对象`类`上有@Transactional注解的（类的所有方法），（两者似乎没有区别？）

`@annotation(org.springframework.transaction.annotation.Transactional)` - 方法带有@Transactional注解的所有方法。

`@args(org.springframework.transaction.annotation.Transactional)` - `参数的类型` 带有@Transactional注解的所有方法

6. bean() - 指定某个bean的名称

* `bean(userService)` - bean的id为userService的所有方法
* `bean(*Service)` - bean的id为Service字符串结尾的所有方法

另外，注意上面这些表达式是可以利用`||,&&,!`进行自由组合的。比如: 
`execution(public * net.aazj.service..*.getUser(..) && args(Integer , ..))`

## 向注解处理方法传递参数

有时候我们在写注解处理方法时，需要访问被拦截的方法的参数。此时我们可以使用args()来传递参数，下面看一个例子:

```Java
@Aspect
@Component

public class LogInterceptor {
    @Pointcut("exection (public * net.aazj.service..*.getUser(..))")
    public void myMethod(){};

    @Before ("myMethod()")
    public void before() {
        System.out.println("method start");
    }

    @After ("myMethod()")
    public void after() {
        System.out.println("method after");
    }

    @AfterReturning("execution(public * net.aazj.mapper..*.*(..))")
    public void afterReturning() {
        System.out.println("method AfterReturning");
    }

    @AfterThrowing("execution (public * net.aazj.mapper..*.*(..))")
    public void afterThrowing() {
        System.out.println("method AfterThrowing");
    }

    @Around("execution(public * net.aazj.mapper..*.*(..))")
    public Object around(ProceedingJoinPoint jp) throws Throwable {
        System.out.println("method around");
        SourceLocation sl = jp.getSourceLocation();
        Object ret = jp.proceed();
        System.out.println(jp.getTarget());
        return ret;
    }

    @Before ("execution (public * net.aazj.service..*.getUser(..)) && args(userId,..)")
    public void before3(int userId) {
        System.out.println("userId ---- " + userId);
    }

    @Before ("myMethod()")
    public void before2(JoinPoint jp) {
        Object[] args = jp.getArgs();
        System.out.println("userId11111: " + (Integer)args[0]);
        System.out.println(jp.getTarget());
        System.out.println(jp.getThis());
        System.out.println(jp.getSignature());
        System.out.println("method start");
    }
}
``` 

```Java
@Before ("execution (public * net.aazj.service..*.getUser(..)) && args(userId,..) ")
public void before3(int userId) {
    System.out.println("userId-----" + userId);
}
```

它会链接net.aazj.service包下或者子包下的getUser方法，并且该方法的第一个参数必须是int型的，`那么使用切点表达式args(userId,..)`就可以使我们在切面中的处理方法before3中可以访问这个参数。

before2方法也让我们知道也可以通过JoinPoint参数来获得被拦截方法的参数数组。JoinPoint是每一个切面处理方法都具有的参数，@Around类型的具有的参数类型为ProceedingJoinPoing。通过JoinPoint或者ProceedingJoinPoint参数可以访问到被拦截对象的一些信息（参见上面的before2方法）。

## Spring AOP的缺陷

因为Spring AOP是基于动态代理对象的，那么如果target中的方法不是被代理对象调用的，那么就不会织入切面代码，看个例子：

```Java
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Transactional(readOnly=true)
    public User getUser(int userId) {
        return userMapper.getUser(userId);
    }

    public void addUser(String username) {
        getUser(2);
        userMapper.addUser(username);
    }
}
```

看到上面的addUser()方法中，我们调用了getUser()方法，而getUser()方法是谁调用的呢？是UserServiceImpl的实例，而不是代理都西昂，那么getUser()方法就不会被织入切面代码。

切面代码如下：

```Java
@Aspect
@Component
public class AOPTest {
    @Before ("exection (public * net.aazj.service..*.getUser(..))")
    public void m1(){
        System.out.println("in m1...");
    }

    @Before ("execution (public * net.aazj.service..*.addUser(..))")
    public void m2(){
        System.out.println("in m2...");
    }
}
```

执行如下代码：

```Java
public class Test{
    public static void main(String[] args){
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"config/spring-mvc.xml","config/application2.xml"});

        UserService us = context.getBean("userService",UserService.class);
        if(us != null) {
            us.addUser("aaa");
        }
    }
}
```

输出结果如下：

```
in m2...
```

虽然getUser()方法被调用了，但是因为不是代理对象调用的，所以AOPTest.m1()方法并没有执行，这就是Spring AOP的缺陷。解决方法如下：

首先： 将`<aop:aspectj-autoproxy  />`改为`<aop:aspectj-autoproxy expose-proxy="true" />`。

然后，修改UserServiceImpl中addUser()方法:

```Java
@Service("userSerice")
@Transactional
public class UserServcieImpl implements UserService{
    @Autowired
    private UserMapper userMapper;

    @Transactional(readOnly=true)
    public User getUser(int userId) {
        return userMapper.getUser(userId);
    }

    public void addUser(String username) {
        ((UserService)AopContext.currentProxy()).getUser(2);
        userMapper.addUser(username);
    }
}
```

`((UserService)AopContext.currentProxy()).getUser(2); 先获得当前代理对象，然后在调用getUser()方法`，就行了。

`expose-proxy="true"`表示将当前代理对象暴露出去，不然`AopContext.currentProxy`获得的是null。

修改之后的运行结果：

```
in m2...
in m1...
```


