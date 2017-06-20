
# Kafka 集群监控

* Kafka Offset Monitor 介绍
* Kafka Offset Monitor 使用
* Kafka Manager 介绍
* Kafka Manager 使用

## Kafka Offset Monitor 介绍

在生产环境需要集群高可用，所以需要对Kafka集群进行监控。Kafka Offset　Monitor可以监控Kafka集群以下几项：
* Kafka集群当前存活的broker集合
* Kafka集群当前活动topic集合
* 消费者组列表
* Kafka集群当前consumer按组消费的offset lag数（即当前topic当前分区有多少消息积压而没有及时消费）

## Kafka Offset Monitor 使用

部署Kafka Offset Monitor：
* github下载jar包： KafkaOffsetMonitor-assembly-0.2.0.jar
https://github.com/quantifind/KafkaOffsetMonitor/releases
* 启动Kafka Offset Monitor
java -cp KakfaOffsetMonitor-assembly-0.2.0.jar com.quantifind.kafka.offsetapp.OffsetGetterWeb --zk zk-01 , zk-02 --refresh 5.minutes --retain 1.day &


# Kafka Manager 介绍

Kafka Manager 由雅虎开源，提供以下功能：
* 管理几个不同的集群
* 容易的检查集群的状态（topics，brokers，副本的分布，分区的分布）
* 选择副本
* 基于集群当前状态产生分区分配
* 重新分配分区

# Kafka Manager 使用

Kafka Manger的安装，方法一（翻墙）
* 安装sbt
http://www.scala-sbt.org/download.html
下载后，解压并配置环境变量（将SBT_HOME/bin配置到path变量中）

* 安装Kafka Manager
    1. git clone https://github.com/yahoo/kafka-manager
    2. cd kafka-manager
    3. sbt clean dist

* 部署Kafka Manager
    1. 修改conf/application.conf，把kafka-manager.zkhosts改为自己的zookeeper服务器地址
    2. bin/kafka-manager -Dconfig.file=conf/application.conf -Dhttp.port=8007 & 

Kafka Manager 安装 ，方法二：
* 下载打包好的Kafka Manager：
https://github.com/scootli/kafka-manager-1.0-SNAPSHOT/tree/master/kafka-manager-1.0-SNAPSHOT

* 部署Kafka Manager
    1. 修改conf/application.conf，把kafka-manager.zkhosts改为自己的zookeeper服务器地址
    2. bin/kafka-manager -Dconfig.file=conf/application.conf -Dhttp.port=8007 & 




