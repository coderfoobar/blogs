
# ZooKeeper 入门

> Create Time : 2017年8月24日 Ref : ke.qq.com Zookeeper入门

> Zookeeper 入门知识点介绍

* zookeeper简介
* paxos算法和zab协议
* zookeeper集群搭建
* zookeeper命令行（zkcli）的使用
* kafka在zookeeper中组织数据的方式
* zookeeper java api
* zookeeper监视器

## zookeeper简介

### zookeeper解决的问题

* 分布式一致
* 分布式协调
* 分布式事务

### 分布式一致

特点： 分布性，对等性，并发性，高可用性



### zookeeper与大数据组件

* HDFS的高可用
* HBase的高可用
* kafka元数据
* Storm元数据

### zookeeper的其他公用

* 数据发布和订阅
* 负载均衡
* 命名服务
* 分布式协调通知
* 集群管理
* 选举
* 分布式队列
* 分布式锁

## paxos算法和zab协议

### paxos算法

* paxos算法是一种基于消息传递且具有高容错性的一致性算法，是目前公认的最有效的解决分布式一致性问题的算法之一。
* paxos可以快速且准确的在集群内部对数据的值达到一致
* 每一个提案（请求）由[有序编号，值]组成
* 由数学归纳法证明

### ZAB协议

* ZAB协议可以算是paxos算法的一个实现，但有区别
* 分为leader、flower和observer三种角色，主从架构
* leader为主，通过选举机制选举
* observer不参与选举，只进行数据存储，用于负载均衡
* leader进程会等待超过半数的follower角色做出正确反馈时才会将一个提案提交

### zookeeper介绍

* 采用java语言编写
* ZAB协议
* TCP长连接Session会话
* 树形结构znode节点
* 监听器Watcher
* ACL权限控制

## zookeeper集群搭建

* 打开防火墙端口
* 安装JDK
* 解压缩zookeeper-3.4.10.tar
* 修改配置文件
    1. cp zoo.sample.cfg zoo.cfg
    2. dataDir=/opt/zookeeper/data (zk安装目录,data目录需要创建)
    3. clientPort=2181 (不用变)
* 设置myid
* 启动ZooKeeper

## zookeeper命令行（zkcli）的使用

```shell
./zkCli.sh -server 127.0.0.1:2181
ls /
create /a 123  # 创建a节点
get /a 

delete /a

```


##  kafka在zookeeper中组织数据的方式

启动： zookeeper CDH发行版

zookeeper-client -server 127.0.0.1:2181

zookeeper可以保存很多大数据相关的元数据

查看kafka的元数据
```shell
ls /brokers

ls /broker/topics

ls /brokers/topics/obd-data-0

get /brokers/topics/obd-data-0/partitions


ls /consumers

ls /consumers/console-consumer-6693
[ids,owners,offsets]


```

## zookeeper java api与监视器 

```Java
package com.abcde;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import java.io.IOException;

public class TestZoo {
    public static void main(String[] args) throws KeeperException, InterruptedException, IOException {
        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    System.out.println(watchedEvent);
                }
            }
        };

        ZooKeeper zoo = new ZooKeeper("127.0.0.1" , 5000, watcher);
        zoo.create("/a","abcdef".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        byte[] rs = zoo.getData("/a",true,new Stat());
        System.out.println(new String(rs));
    }
}

```












