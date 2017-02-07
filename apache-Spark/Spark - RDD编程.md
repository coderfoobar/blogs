
# Spark - RDD编程

> Create Time ： 2017年2月4日 ； author ： huduku

## RDD基础

> RDD ： 弹性分布式数据集( Resilient Distributed Dataset )。
RDD其实就是不可变的分布式的对象集合（元素集合）。
在Spark中，对数据的所有操作不外乎`创建RDD`，`转化已有RDD`以及`调用RDD操作进行求值`。而在这一切背后，Spark会自动将RDD中的数据分发到集群上，并将操作并行化执行。

每个RDD都被分为多个分区，这些分区运行在集群中的不同节点上。
RDD可以包含Python，Java，Scala中任意类型的对象，甚至可以包含用户自定义的对象。

用户可以有两种方式创建RDD：
* 读取一个外部数据集
* 在驱动器程序里分发驱动器程序中的对象集合（比如list和set）。

创建出来后，RDD支持两种类型的操作:
* `转化操作(transformation)` - 由一个RDD生成一个新的RDD。
* `行动操作(action)` - 对RDD计算出一个结果,把最终结果返回到驱动器程序，或者写入到外部存储系统中。

虽然你可以在任何时候顶一个新的RDD，但是Spark只会惰性计算这些RDD。它们只有第一次在一个行动操作时，才会真正进行计算。

默认情况下，Spark的RDD会在你每次对它们进行行动操作时重新计算，如果想在多个行动操作中重用一个RDD，可以使用RDD.persist()让Spark把这个RDD缓存下来。

总的来说，每个Spark程序或shell会话都会按如下方式工作：
* 从外部数据创建出输入RDD；
* 使用注入filter()这样的转化操作对RDD进行转化，以及定义新的RDD；
* 告诉Spark对需要被重用的中间结果RDD执行persist()操作；
* 使用行动操作来触发一次并行计算，Spark会对计算进行优化后再执行。

## 创建RDD

创建一个RDD最简单的方式就是把程序中一个已有的集合传给SparkContext的parallelized()方法。除了开发原型和测试时，这种方式用的并不多。

用户可以有两种方式创建RDD：
* 读取一个外部数据集
* 在驱动器程序里分发驱动器程序中的对象集合（比如list和set）。

## RDD操作

```Java
JavaRDD<String> inputRDD = sc.textFile("log.txt");
JavaRDD<String> errorRDD = inputRDD.filter(
    new Function<String,Boolean> (){
        public Boolean call(String x){
            return x.contains("error");
        }
    }
);
```


转化操作:
对每个元素的转化操作：
* `map()`
* `flatMap()`
* `filter()`
* `distinct()`  - 开销较大，需要对所有元素通过网络进行混洗shuffle
* `sample(withReplacement,fraction,[seed])` - 对RDD采样，以及是否替换

伪集合操作：
* `union(other)`
* `intersection(otehr)` - 只选择两个RDD中都拥有的元素；运行时，会去掉所有重复的元素(单个RDD中重复的元素也会一并移除)（需要通过网络混洗来发现共有的元素）
* `subtract(other)`  - 接收另一个RDD作为参数，返回一个由只存在第一个RDD中而不存在于第二个RDD中的所有元素组成的RDD
* `cartesian(other)` - 笛卡儿积

行动操作：

每当调用一个行动操作时，整个RDD都会从头开始计算。
* `collect()` - 获取整个RDD中的数据，不能用在大规模数据集上。通常要把数据写到注入HDFS或者Amazon S3这样的分布式存储系统中。
* `count()` - RDD中的元素个数
* `countByValue()` - 各元素在RDD中出现的次数
* `take(num)` - 从RDD中返回num个元素
* `top()` - 从RDD中返回最前面的num个元素
* `takeOrdered(num)(ordering)` - 从RDD中按照提供的顺序返回最前面的num个元素
* `takeSample(withReplacemet , num , [seed])` - 从RDD中返回任意一些元素 
* `reduce(func)` - 并行整合RDD中的所有数据
* `fold(zero)(func)` - 和reduce函数一样，但是需要提供初始值
* `aggregate(zeroValue)(seqOp,combOp)` - 和reduce()相似，但是通常返回不同类型的函数
* `first()`
* `foreach(func)` - 对RDD中的每个元素使用给定的函数

惰性求值：
我们不应该把RDD看作存储特定数据的数据集，而最好把每个RDD当作我们通过转化操作构建出来的、记录如何计算数据的指令列表。
把数据读取到RDD的操作也同样是惰性求值的。和转化操作一样的是，读取数据的操作也有可能会多次执行。

## 在不同的RDD类型之间转换

```Java
JavaDoubleRDD result = rdd.mapToDouble(
    new DoubleFunction<Integer>(){
        public double call(Integer x){
            return (double) x * x;
        }
    }
);
System.out.println(result.mean());
```

## 持久化（缓存）

Spark RDD是惰性求值的，而有时我们希望多次使用同一个RDD。如果简单的对RDD调用行动操作，Spark每次都会重算RDD以及它的所有依赖。

当我们让Spark存储一个RDD时，计算出RDD的节点会分别保存它们所求出的分区数据。如果一个有持久化数据的节点发生故障，Spark会在需要用到的缓存数据时重算丢失的数据分区。

处于不同的目的，我们可以为RDD选择不同的持久化级别。在Scala和Java中，默认情况下persist()会把数据以序列化的形式缓存在JVM的堆空间中。当我们把数据写到磁盘或者堆外存储上时，也总是使用序列化后的数据。

* MEMORY_ONLY
* MEMORY_ONLY_SER
* MEMORY_AND_DISK
* MEMORY_AND_DISK_SER
* DISK_ONLY

最后RDD中还有一个方法叫做unpersist()，调用该方法可以手动把持久化存储的RDD从缓存中移除。
































