
# spring-tx 事务管理

> Create Time : 2017年4月14日  摘自 : 《Spring实战》 第六章 ： 事务管理

> 相关性文档：JTA（XA） 分布式事务.md

> `事务` - 在软件开发领域，全有或全无的操作叫做事务。

事务允许将几个操作组合成一个要么全部发生，要么全部不发生的工作单元。如果一切顺利，事务将会产生，。但是有任何一件事情出错的话，所发生的行为将会被清除干净，就像什么事情都没有发生一样。

Spring为事务管理提供了丰富的功能支持，包括编码方式，和声明方式。

## 理解事务

事务确保了数据或资源处于一致的状态。

### 事务ACID四特性

* `原子性` - Atomic，事务是由一个或多个活动所组成的一个工作单元。原子性确保事务中所有操作全部发生或全部不发生。如果所有的活动都成功了，事务也就成功了。如果任意一个活动失败了，整个事务也失败并回滚。
* `一致性` - Consistent，一旦事务完成（无论成功还是失败），系统必须确保它所建模的业务处于一致的状态。现实的数据不应该被损坏。
* `隔离性` - Isolated，事务允许许多个用户对相同的数据进行操作，每个用户的操作不会与其他的用户纠缠在一起。因此，事务应该被彼此分离，避免发生同步读写相同数据的事情（注意的是，隔离性往往涉及到锁定数据库中的行或者表）。
* `持久性` - 一旦事务完成，事务的结果应该持久化，这样就能从任何的系统崩溃中恢复过来。这一般会涉及到将结果存储到或其他形式的持久化存储中。


### 理解Spring对事务管理的支持

Spring通过回调机制，将实际的事务控制从事务性的代码中抽象出来。

Spring对事务的支持甚至不需要JTA的实现。如果你的应用程序只使用一种持久化资源，Spring可以使用持久化机制本身所提供的事务性支持，这包括JDBC、Hibernate、以及Java持久化API（ Java  Persistence API,JPA）。但是，如果程序的事务跨多个资源，那么Spring会使用第三方的JTA实现来支持分布式（XA）事务。

编码式事务允许用户在代码中精确定义事务的边界，而声明式事务（基于AOP）有助于用户将操作与事务规则进行解耦。

## 选择事务管理器

Spring 并不直接管理事务，而是提供了多种事务管理器，他们将事务管理器的职责委托给JTA或其他持久化机制所提供的平台相关的事务实现。

下面的表格展示了各种场景下的事务管理器。

| 事务管理器 (org.framework.*) | 使用场景 |
| :--                         | :--     |
| `jca.cci.connection.CciLocalTransactionManager` | 使用Spring对`Java EE Connector Architecture,JCA`和`Common Client Interface , CCI` |
| `jdbc.datasource.DataSourceTransactionManager` | 用于Spring对JDBC抽象的支持，也可用于使用iBatis进行持久化的场景 |
| `jms.connection.JmsTransactionManager` | 用于JMS 1.1+ |
| `jms.connection.JmsTransactionManager102` | 用于JMS 1.0.2+ |
| `orm.hibernate3.HibernateTransactionManager` | 用于Hibernate 3的持久化 |
| `orm.jdo.JdoTransactionManager` | 用于JDO进行持久化 |
| `orm.jpa.JpaTransactionManager` | 用于Java持久化API（`Java Persistence API,JPA`进行持久化） |
| `transaction.jta.JtaTransactionManager` | 需要分布式事务管理器或者其他事务管理器满足要求 |
| `transaction.jta.OC4JJtaTransactionManager` | 用于Oracle的OC4J EE容器 |
| `transaction.jta.WebLogicJtaTransactionManager` | 需要分布式事务并且应用程序运行在WebLogic中 |
| `transaction.jta.WebSphereUowTransactionManager` | 需要WebSphere中UOWManager所管理的事务 |

Spring的事务管理器图示:

![Spring的事务管理器]()

### JDBC事务

```xml
<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource" />
</bean>
```
dataSource的属性值配置成了一个名为`dataSource`的Bean的引用。而`dataSource`是定义在上下文中的`javax.sql.DataSource`Bean。

### Hibernate事务

```xml
<bean id="transactionManager" class="org.springframework.orm.hibernate3.HibernateTransactionManager">
    <property name="sessionFactory" ref="sessionFactory" />
</bean>
```

`sessionFactory` 属性需要装配一个Hibernate的`SessionFactory`，这里我们将其命名为`sessionFactory`。

### Java持久化API事务

Hibernate多年来一直是事实上的Jva持久化标准，但是现在Java持久化API作为真正的Java持久化标准进入了大家的视野。

```xml
<bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
    <property name="entityManagerFactory" ref="entityManagerFactory" />
</bean>
```

`JpaTransactionManager`只需要装配一个JPA实体管理工厂（`javax.persistence.EntityManagerFactory`接口的任意实现）。`JpaTransactionManager`将与由工厂产生的`JPA` `EntityManager` 合作来构建事务。

除了将事务应用于JAP操作，JpaTransactionManager还支持将事务应用于简单的JDBC操作中。这些JDBC操作所使用的`DataSource`与`EntityManagerFactory`所使用的`DataSource`必须是相同的。为了做到这一点，`JpaTransactionManager`必须装配一个JpaDialect的实现。例如：假设你已经配置了`EclipseLinkJpaDialect`，如下所示：

```xml
<bean id="jpaDialect" class="org.springframework.orm.jpa.vendor.EclipseLinkJpaDialect" />

<bean id="transactionManager" class="org.springframework.orm.jpa.JpaTrasactionManager">
    <property name="entityManagerFactory" ref="entityManagerFactory" />
    <property name="jpaDialect" ref="jpaDialect" />
</bean>
```

需要重点指出的是，JpaDialect实现必须同时支持JPA/JDBC访问。所有Spring所支持的特定厂商JpaDialect实现(`EclipseLinkJpaDialect`、`HibernateJpaDialect`、`OpenJpaDialect`、`TopLinkJpaDialect`)都提供了对JPA和JDBC混合的支持，而`DefaultJpaDialect`并不支持。

### JTA（Java Transaction API）事务

如果前面的事务管理器都不能满足你的要求，或者需要使用多个事务资源（比如分布式事务），需要使用`JtaTransactionManager`：

```xml
<bena id="transactionManager" class="org.springframework.transaction.jpa.JtaTransactionManager">
    <property name="transactionManagerName" value="java:/TransactionManager" />
</bean>
```

`JtaTransactionManager`将事务管理的职责委托给了一个JTA的实现。JTA规定了应用程序与一个或多个数据源之间协调事务的标准API。`transactionManagerName`属性指明了要在JNDI上查找JTA事务管理器。


`JtaTransactionManager`将事务管理的职责委托给`javax.transaction.UserTransaction`和`javax.transaction.TransactionManager`对象。通过`UserTransaction.commit()`方法来提交事务。类似的，如果事务失败，`UserTransaction`的`rollback()`方法将会被调用。

## 在Spring中使用编码事务

编码事务能够精准的定位事务的边界。

示例代码:

```Java
public void saveSpittle(Spittle spittle) {
    spitterDao.saveSpittle(spittle);
}

public void saveSpittle(final Spittle spittle) {
    //此处将会执行TransactionCallback实例中的代码（以内部类的形式）。
    txTemplate.execute(new TransactionCallback<Void>(){
        try{
            //如果代码没有抛出异常，则事务将会提交
            spitterDao.saveSpittle(spittle);
        } catch(Exeption e) {
            //如果代码遇到了问题，调用TransactionStatus对象的setRollbackOnly()方法，
            //将回滚事务
            txStatus.setRollbackOnly();
            // 注意此处抛出RuntimeException
            throw new RuntimeException(e);
        }
        return null;
    });
}
```

下面是bean装配的配置方法：

```xml
<bean id="spitterService" class="com.habuma.spitter.service.SpitterServiceImpl" >

    ...
    <property name="transactionTemplate" >
        <bean class="org.springframework.transaction.support.TransactionTemplate" >
            <property name="transactionManager" ref="transactionManager" />
        </bean>
    </property>
</bean>
```

如果想完全控制事务的边界，可以使用编码式事务。但是这种方式具有侵入性。而声明式事务不具有侵入性。

Spring提供了3中方式来声明事务的边界：`Spring AOP`、`TransactionProxyFactoryBean`(已淘汰)的代理Bean、`tx`的明明空间和`@Transactional`注解（Spring 2.0之后）。

### 定义事务属性

* 传播行为 （Propergation Behavior）  propagation
* 隔离级别 （Isolation Level）
* 只读
* 事务超时
* 回滚规则

#### 传播行为

传播规则规定了一个事务是应该被启动，还是应该被挂起，或者方法是否要在事务环境中运行。

传播行为常量在接口`org.springframework.transaction.TransactionDefinition`接口中定义。下面是传播规则常量的说明表：

| 传播行为 | 含义 |
| :--     | :-- |
| `PROPAGATION_MANDATORY` | 表示该方法必须在事务中运行，如果当前事务不存在，则会抛出一个异常 |
| `PROPAGATION_NESTED`    | 表示当前已经存在一个事务，那么该方法将会在嵌套事务中运行。嵌套的事务可以独立于当前事务进行单独的提交或回滚。如果当前事务不存在，那么其行为与`PROPAGATION_REQUIRED`一样。注意各厂商对这种传播行为的支持是有所差异时的。可以参考资源管理器的文档来确定它们是否支持嵌套式事务。
| `PROPAGATION_NEVER` | 表示当前方法不应该运行在事务上下文当中，如果当前正有一个事务在运行，则会抛出异常 |
| `PROPAGATION_NOT_SUPPORTED` | 表示该方法不应该运行在事务中，如果存在当前事务，在该方法运行期间，当前事务被挂起，如果使用`JTATransactionManager`的话，则需要访问`TransactionManager` |
| `PROPAGATION_REQUIRED` | 表示当前方法必须运行在事务当中，如果当前事务存在，方法将会在该事务当中运行。否则会启动一个新的事务 | 
| `PROPAGTION_REQUIRED_NEW` | 表示该方法必须运行在自己的事务中。一个新的事务将被启动，如果当前存在事务，在该方法执行期间，当前事务被挂起。如果使用`JTATransactionManager`，则需要访问`TransactionManager`。
| `PROPAGATION_SUPPORTED` | 表示当前不需要事务上下文，但是如果存在当前事务的话，那么该方法会在这个事务中运行 |

#### 隔离级别

隔离级别的常量定义在`org.springframework.transaction.TransactionDefinition`接口中。

隔离级别定义了一个事务可能收到其他并发事务影响的成都。另一种定义隔离级别的方式就是将其想象成事务对于事务性数据的自私程度。

多个事务并发运行，经常会操作相同的数据来完成各自的任务。并发，虽然是必需的，但是，可能会导致以下问题：

* `脏读` - 脏读发生在一个事务读取了另一个事务改写但尚未提交的数据时。如果改写的数据稍后提交了，那么第一个事务获取的数据就是无效的。
*  `不可重复读` - 发生在一个事务执行相同的查询两次或者两次以上，但是每次都得到不同的数据时。这通常是因为另一个并发事务在两次查询期间更新了数据。
* `幻读` - 幻读和不可重复读类似。它发生在一个事务（T1）读取了几行数据，接着另一个并发事务（T2）插入了一些数据时。在随后的查询中，第一个事务（T1）就会发现多了一些原本不存在的记录。

在理想情况下，事务之间是相互隔离的，从而可以防止这些问题发生。但是完全隔离会导致性能的问题，因为它通常会涉及锁定数据库中的记录（有时候甚至是整张表）。所以有时，应用程序需要在事务上有一定的灵活性。因此，就会有多种隔离级别。

| 隔离级别 | 含义 |
| :--     | :--  |
| ISOLATION_DEFAULT | 使用后端数据库默认的隔离级别 |
| ISOLATION_READ_UNCOMMITED | 允许读取尚未提交的数据变更。可能会导致脏读、幻读或不可重复读 |
| ISOLATION_READ_COMMITED   | 允许读取并发实物已经提交的数据。可以阻止脏读，但是幻读或不可重复读仍有可能发生 |
| ISOLATION_REPEATABLE_READ | 对同一字段的多次读取结果是一致的，除非数据是是被本事务自己所修改。可以阻止脏读和不可重复读，但幻读仍有可能发生。 |
| ISLATION_SERIALIEZABLE | 完全服从ACID的隔离级别，确保阻止脏读，不可重复读，以及幻读。这是最慢的事务隔离级别。因为它通常是通过锁定事务相关的数据库表来实现的。 |

#### 只读

如果事务只对后端的数据库进行读操作，数据库可以利用事务的只读特性来进行一些特定的优化。

只有对那些具备启动一个新事务的传播行为（PROPAGATION_REQUIRED , PROPAGATION_REQUIRES_NEW以及PROPAGATION_NESTED）的方法来说，将事务声明为只读才有意义。

另外，如果采用Hibernate作为持久化机制，那么将事务声明为只读会导致Hibernate的flush模式被设置为FLUSH_NEVER。

#### 事务超时

假设事务的运行时间特别长。因为事务可能涉及到对后端数据库的锁定，所以长时间的事务会不必要的占用数据库资源。你可以声明一个事务，在特定的秒数后自动回滚，而不是等待其结束。

因为超时时钟会在事务开始时启动，所以，只有对那些具备可能启动一个新事务的传播行为（PROPAGATION_REQUIRED , PROPAGATION_REQUIRES_NEW以及PROPAGATION_NESTED）的方法来说，声明事务超时才有意义。

#### 回滚规则

默认情况下，事务只有在遇到运行期异常时才会回滚，而在遇到检查型异常时不会回滚。但是可以声明事务在遇到特定的检查型异常像遇到运行期异常那样回滚。同样，你还可以声明事务遇到特定的异常不回滚，即使这些异常是运行期异常。

### 在xML中定义事务

早期版本的Spring中，声明事务需要装配一个名为`TransactionProxyFactoryBean`的特殊Bean。`TransactionProxyFactoryBean`的问题在于使用它会导致非常冗长的Spring配置文件。现在，spring提供了一个tx配置命名空间，借助它可以极大的简化Spring中的声明式事务。

使用tx命名空间会涉及到将其添加到Spring XML配置文件中：

```xml
<beans xmlns="http://wwww.springframework.org/schema/beans" 
       xmlns="http://www.w3.org/2001/XMLSchema-instance"
       xmlns="http://www.springframework.org/schema/aop"
       xmlns="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
                           http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
                           http://www.springframework.org/aop
                           http://www.springframework.org/aop/spring-aop-3.0.xsd
                           http://www.springframework.org/schema/tx
                           http://www.springframework.org/schema/tx/spring-tx-3.0.xsd" >
```

需要注意的是,aop的命名空间应该包括在内。因为一些声明式的事务配置元素依赖于部分Spring的AOP配置元素。

tx命名空间提供了一些新的XML配置元素，其中最值得注意的是`<tx:advice>`元素:

```xml
<tx:advice id="txAdvice">
    <tx:attributes>
        <tx:method name="save*" propagation="REQUIRED"  />
        <tx:method name="*" propagation="SUPPORTS" read-only="true" />
    </tx:attributes>
</tx:advice>
```

对于`<tx:advice>`来说，事务属性定义在`<tx:attributes>`当中，该元素包含了一个多多个`<tx:method>`元素。`<tx:method>`元素为某个或某些name属性（使用通配符）指定的方法定义事务参数。

| 隔离级别 | 含义 |
| :------ | :--- |
| isolation | 事务的隔离级别 |
| propagation | 事务的传播规则 |
| read-only | 事务只读 |
| rollback-for | 对于检查型事务应该回滚而不提交 |
| no-rollback-for | 对于哪些异常应该继续运行而不回滚 |
| timeout | 对于长时间运行的事务定义超时时间 |

当使用`<tx:advice>`来声明事务时，你还需要一个事务管理器，就像使用`TransactionProxyFactoryBean`那样。根据约定优于配置，`<tx:advice>`假定事务管理器被声明为一个`id`为`transactionManager`的`Bean`。如果碰巧为事务管理器配置了一个不同的`id`(如`txManager`)，则需要在`transactionManager`属性中明确指定事务管理器的`id`:

```xml
<tx:advice id="txAdvice" transaction-manager="txManager">
...
</tx:advice>
```

`<tx:advice>`只是定义了一个AOP通知，用于把事务边界通知给方法。但是这只是事务通知，而不是完整的事务性切面。

为了完整的定义事务性切面，我们必须定义一个通知器（`advisor`）。这就涉及aop明明空间了。

```xml
<aop:config>
    <aop:advisor pointcut="execution (* *..SpitterService.*(..))" advice-ref="txAdvice" />
</aop:config>
```

这里的`pointcut`属性使用了`AspectJ`切入点表达式来表明通知器适用于SpitterService接口的所有方法。哪些方法应该真正运行在事务中以及方法的事务属性都是由这个事务通知来定义的，而事务通知是`advice-ref`属性来指定的，它引用了`id`为`txAdvice`的通知。

### 定义注解驱动的事务

除了`<tx:advice>`元素，`tx`命名空间还提供了`<tx:annotation-driven />`元素。

```xml
<tx:annotation-driven /> 
```

还可以通过`transaction-manager`属性指定特定的事务管理器。

```xml
<tx:annotation-driven transaction-manager="txManager" />
```

`<tx:annotation-driven />`元素告诉Spring检查上下文中所有的Bean并查找并使用`@Transactional`注解的`Bean`，而不管这个注解是使用在类级别上还是方法级别上。对于每一个使用`@Transactional`注解的Bean，`<tx:annotation-driven>`会自动为它添加事务通知。通知属性是通过`@Transactional`注解的参数来定义的。

```Java
@Transactional(propagation=Propagation.SUPPORTS , readOnly=true)
public class SpitterServiceImpl implements SpittleService {
    //...
    @Transactional (propagation=Propagation.REQUIRED , readOnly=false)
    public void addSpitter(Spitter spitter) {
        //...
    }
    //....
}
```

在类级别上，`SpitterServiceImpl`使用了`@Transactional`注解，表示所有的方法都支持事务并且是只读的。在方法级别上，`addSpitter()`方法通过注解来标识这个方法所需要的上下文。




































