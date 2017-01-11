
# Spark - 生态圈简介

## 简介

### 起源

Spark是加州伯克利分校AMP实验室开发的通用内存并行计算框架。使用Scala语言实现。

优点：

* 运行速度快

DGA执行引擎，支持在内存中对数据进行迭代计算。

* 易用性好

支持Scala，Java，Python

* 通用性强

Spark Core , Spark SQL , Spark Streaming , MLLib , GraphX

* 随处运行

能够读取HDFS,Cassandra,HBase,S3,Techyon为持久层读写原生数据

能够以Mesos,YARN,和自身携带的Standalone作为资源管理器调度job来完成Spark应用程序的计算。

### 与Hadoop的差异

Spark借鉴了MapReduce，继承了其分布式并行计算的优点并改进了MapReduce明显的缺陷。具体如下：

Spark把中间数据放到内存中，迭代运算效率高。支持DAG图的分布式并行计算编程框架，减少了迭代过程中数据的落地，提高了处理效率。
MapReduce中计算结果需要落地，保存到磁盘上，这势必会影响整体速度。

Spark容错性高。Spark引入了RDD（Resilient Distributed Dataset）弹性分布式数据集的抽象，它是分布在一组节点中的只读对象集合，这些集合是弹性的，如果数据集一部分丢失，则可以根据“血统”（即充许基于数据衍生过程）对他们进行重建。另外RDD可以通过CheckPoint（CheckPoint Data , Logging The Updates）来实现容错。

Spark更加通用。更多的操作。


### Spark 适用场景

目前大数据处理场景有以下几个类型

* 复杂的批量处理（Batch Data Processing） ，偏重于处理海量数据的能力，处理时间数十分钟至数小时。（MapReduce）
* 基于历史数据的交互式查询（Interactive Query），通常在数十秒到数分钟之间。（Impala）
* 基于实时数据流的数据处理（Streaming Data Processing）,通常在数百毫秒到数秒之间。（storm）

以上三种解决方案比较独立，Spark能够提供一站式平台服务。

Spark适用场景：

* Spark是基于内存的迭代计算框架，适用于需要多次操作特定数据集的应用场合，需要反复操作的次数越多，所需读取的数据量越大，受益越大。数据量小但计算密度较大的场合，受益相对较小。
* 由于RDD的特性，Spark不适用那种异步细粒度更新状态的应用。（对于增量修改的应用模型不适合）
*  数据量不是特别大，但是要求实时统计分析需求。

### 术语

#### 运行模式

| 运行环境 | 模式 | 描述 |
| :-----: | :---: | :--- |
| Local | 本地模式 | 常用于本地开发测试，本地还分为local单线程和local-cluster多线程 |
| Standalone | 集群模式 | 典型的Master/Slave模式，不过也能看出Master是有单点故障的；Spark支持Zookeeper来实现HA |
| On yarn | 集群模式 | 运行在yarn资源管理器框架上，由yarn负责资源管理，Spark负责资源调度和计算 |
| On mesos | 集群模式 | 运行在mesos资源管理框架之上，由mesos负责资源管理，Spark负责任务调度和计算 |
| On Cloud | 集群模式 | 比如AWS的EC2，使用这个模式能很方便的访问Amazon的S3；Spark支持多种分布式存储系统：HDFS，S3 |

#### 常用术语

| 术语 | 描述 |
| :--  | -- |
| Application | Spark的应用程序，包含一个Driver program和若干Executor |
| SparkContext | Spark应用程序入口，负责调度各个运算资源，协调各个Worker Node上的Executor |
| Driver Program | 运行Application的main()函数并创建SparkContext |
| Executor | 是为Application运行在Worker node上的一个进程，该进程负责运行task，并且负责将数据存储在内存或者磁盘上。每个Application都会申请各自的Executor来处理任务 |
| Cluster Manager | 在集群上获取资源的外部服务 |
| Worker Node | 集群中任何可以运行Application代码的节点，运行一个或多个Executor进程 |
| Task | 运行在Executor上的工作单元 |
| Job | SparkContext提交具体Action操作，常和Action对应 |
| Stage | 每个Job会被拆分很多组task，每组任务被称为stage，也成为TaskSet |
| RDD | 是Resilient distributed dataset的简称，中文为弹性分布式数据集，是Spark最核心的模块和类 |
| DAGSchedular | 根据Job构建基于Stage的DAG，并提交Stage给TaskScheduler |
| TaskSchedular | 将Taskset提交给Worker node集群运行并返回结果 |
| Transformations | 是Spark API的一种类型，Transformation返回值还是一个RDD，所有的Transformations采用的都是懒策略，如果只是将Transformations提交是不会执行计算的 |
| Action | 是Spark API的一种类型，Action返回值不是一个RDD，而是一个Scala集合，计算只有在Action被提交的时候计算才被触发 |


## 生态系统

### Spark Core

* DAG
* RDD
* 移动计算
* 多线程池模型
* 容错、高可伸缩的akka作为通讯框架

### Spark Streaming

Spark Streaming 是一个对实时数据进行高通量，容错处理的流式处理系统，可以对多种数据源（如kafka，Flume，Twitter，Zero，和TCP套接字）进行类似Map、Reduce和Join等复杂操作。将结果保存到外部文件系统、数据库或应用到实时Dashboard。

Spark Streaming架构：

* 计算流程

Spark Streaming 是将流式计算分解成一系列短小的批处理作业。这里的批处理引擎是Spark Core，也就是把Spark Streaming的输入数据按照batch size（如1秒）分成一段一段的数据（Discretized Stream），每一段数据都转换成Spark的RDD，然后将Spark Streaming中对DStream的Transformation操作变为针对Spark中对RDD的Transformation操作，将RDD经过操作变成中间结果保存在内存中。整个流式计算，根据业务需求，可以对中间结果进行叠加或者存储到外部设备。

* 容错性


* 实时性

* 扩展性与吞吐量

### Spark SQL

### BlinkDB







