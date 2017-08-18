
# Spring事务-无Transactional方法调用另一个有Transactional注解的方法

> Create Time : 2017年8月19日 Ref : http://blog.csdn.net/rainbow702/article/details/53907474

```Java
public class Foo {
    @Transactional 
    public void bar() {
        /* --- * /
    }

    public void baz() {
        this.bar();
    }
}
```

可能会有不少人会跟我一样，觉得上面这种方式调用baz()方法时，bar()上的@Transactional注解还是会起作用的，即bar()在被调用时，将会开启事务。

但是，实际操作之后，你会发现，这样并不会开启新的事务。为什么呢？

我们知道，spring之所以可以开启@Transactional的方法进行事务管理，是因为Spring当前类生成了一个代理类，然后在执行相关方法时，会判断这个方法有没有@Transactional注解，如果有的话，则会开启一个事务。

但是上面这种调用方式时，在调用baz()时，使用的并不是代理对象，从而导致this.bar()时也不是代理对象，从而导致@Transactional失效。

那么，对于这种情况要怎么处理呢？

1. 首先，在Spring的配置文件中加上如下配置:

```Java
<aop:aspectj-autoproxy expose-proxy="true" />
```

2. 然后，在baz()中，改成如下方式调用：

```Java
public class Foo {
    @Timed
    public void bar(){/* --- */}

    public void baz() {
        ((Foo)AopContext.currentProxy()).bar();
    }
}
```

> PS : 如果是通过 @Aspect 注解实现的AOP，那么暂时还没有找到方法来解决

> 参考文档

* [Spring AOP 深入剖析](./Spring-AOP-深入剖析.md)


