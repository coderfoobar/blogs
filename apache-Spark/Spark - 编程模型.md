
# Spark - 编程模型

## 编程模型

### 术语定义

* `Application` - 基于Spark的用户程序，包含了一个`Driver Program`和集群中多个`Executor`；
* `Driver Program` - 运行`Application`的`main()`函数并且创建`SparkContext`，通常用`SparkContext`代表`Driver Program`；
* `Executor` - 是为某`Application`运行在`Worker Node`上的一个进程，该进程负责运行`Task`，并且负责将数据存在内存或者磁盘上，每个`Application`都有各自独立的`Executors`；
* `Cluster Manager` - 在集群上获取资源的外部服务。（例如`Standalone`，`Mesos`或`Yarn`） ；
* `Operation` - 作用于`RDD`的各种操作分为`Transformation`和`Action`。

### 模型组成

```
                                            Work Node(Executor(Task1,Task2))
Driver Program                            /
 SparkContext     <---->  Cluster Manager   
                                          \
                                            Work Node(Executor(Task1,Task2))
```

#### Driver 

`Driver` 部分主要是对`SparkContext`进行`配置`，`初始化`，以及`关闭`。`初始化`SparkContext是为了构建Spark应用程序的运行环境，在`初始化`SparkConetxt时，要先导入一些Spark的类和隐式转换。在Executor部分运行完毕后，需要将SparkContext`关闭`。

#### Executor部分

Spark应用程序的Executor部分是对数据的处理，数据分为三种：
* `原生数据` - 原生的输入数据和原生的输出数据。
* `RDD` - 输入算子，转换算子，缓存算子，行动算子。
* `共享变量` - 广播变量和累加器。


通过一个驱动器程序创建一个SparkContext和一系列RDD，然后进行并行操作。







