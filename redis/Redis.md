
# Redis

> Create Time : 2017年6月19日 Author : huduku.io

> Ref : http://blog.csdn.net/sinat_27406925/article/details/73382169

## NoSQL 的概述

## 什么是NoSQL

* NoSQL = Not only SQL
* 非关系型数据库

### 为什么需要NoSQL

WEB2.0技术的兴起，非关系型数据库成了一个非常热门的领域。关系型数据库在应对web2.0中的高并发等问题出现瓶颈。

* High Performance - 高并发读写
* High Storage - 海量数据的高效率存储和访问
* High Scalability && High Availability - 高可扩展性和高可用性

NoSQL数据库的四大分类
* key-value存储
* 列族存储
* 文档数据库
* 图数据库

比较：

| 分类 | 相关产品 | 典型应用 | 数据模型 | 优点 | 缺点 |
| :-- | :-- | :-- | :-- | :-- | :-- | :-- | :-- |
| 键值 | Tokyo cabinet/Tyrant、redis、voldemort、Berkeley DB | 内容缓存，主要用于处理大量数据的高访问负载 | 一系列键值对 | 快速查询 | 存储的数据较少，结构化 | 
| 列族 | Cassandra 、 Hbase 、 Riak | 分布式的文件系统 | 以列族模式存储，将同一列数据放在一起 | 查找速度快，可扩展性强，更容易进行分布式扩展 | 功能相对局限 |
| 文档 | CouchDB、MongoDB | WEB应用（与k-v类似，是结构化的） | 一系列键值对 | 数据结构要求不严格 | 查询性能不高，而且缺乏统一的查询语法 |
| 图 | Neo4j 、 InfoGrid 、 Infinite Graph | 社交网络，推荐系统，专注于构建关系图谱 | 图结构 | 利用图结构相关算法 | 需要对整个图做计算才能得出结果，不同意做分布式集群方案 |

NoSQL的特点：
* 易扩展
* 灵活的数据模型
* 大数据量 ， 高性能
* 高可用

## Redis概述

高性能键值对数据库，支持的键值数据类型：
* 字符串类型
* 列表类型
* 有序集合类型
* 散列类型

应用场景：
* 缓存
* 除重
* 网站访问统计
* 任务队列
* 数据过期处理
* 应用排行榜
* 分布式集群架构中的session分离


## Redis的安装和使用

搭建环境
* 虚拟机： VMWare 10.0.2
* Linux系统： centos-6.5
* SSH客户端： xshell

centos安装redis
* `yum -y install gcc-c++`
* `tar -zxvf redis-3.0.7.tar.gz`
* `cd  redis-3.0.7`
* `make && make test && make install`
* `cp redis.conf /usr/local/redis`
* 启动redis，`./bin/redis-server ./redis.conf`
*  `./bin/redis-cli`
* ping  -- PONG
* set nname imooc
* get name
* keys * 
* del name

## Jedis的入门

Jedis 介绍
* Jedis是Redis官方首选的Java客户端开发包
* https://github.com/xetorthio/jedis

Jedis Java demo


## Redis的数据类型

五种数据类型

* 字符串（String）
* 字符串列表（list）
* 有序字符串集合（sorted set）
* 哈希（hash）
* 字符串集合 （set）

key定义的注意点：
* 不要太长
* 不要过短
* 统一的命名规范

存储String
* 二进制安全的，存入和获取的数据相同
* Value最多可以容纳的数据长度是512M

存储String常用命令
* 赋值
* 取值
* 删除
* 扩展命令
* 数值增减

```
set company tencent
get company
exists company
getset company baidu
del company
incr num (不存在，num=0并加1 ，num=1，如果存在，并能转为数字，则增加1，否则转换失败)
decr num

mset name jack age 18

incrby num2 3 (没有则默认为0，之后加3，num2=3)
decrby num3 5 (没有默认为0，并减5 num3=-5)

append num4 123 (不存在，返回3，get后得到“123”)
```

Redis 存储Hash

* String key 和String value的map容器
* 每一个hash可以存储429496295个键值对

存储Hash常用命令

* 赋值
* 取值
* 删除
* 自学命令
* 数值增减

```
hset myhash username jack
hset myhash age 18
hmset myhash2 username rose age 21
hget myhash username
hmget myhash2 username age
hgetall myhash
hdel myhash2 username age
hgetall myhash2

hdel myhash2 username
hmset myhash2 username rose age 21
del myhash2
hget myhash2 username

hget myhash age
hincrby myhash age 5
hget myhash age

hmget myhash username  age

hexists myhash username
hexists myhash password

hgetall myhash
hlen myhash
hkeys myhash
hvals myhash

```

存储list：
* 按照插入顺序排序的一个链表，可以在头部左侧，和尾部右侧插入顺序。
* ArrayList使用数组的方式存储数据
* LinkedList使用双向链表的方式
* 双向链表中增加数据
* 双向链表中删除数据

存储list常用命令
* 两端添加
* 查看列表
* 两端弹出
* 获取列表元素个数
* 扩展命令

```
lpush mylist a b c 
lpush mylist 1 2 3 
rpush mylist2 a b c 
rpush mylist2 1 2 3

lrange mylist 0 5
3 2 1 c b a

lrange mylist2 0 -1
a b c 1 2 3 

lrange mylist2 0 -2
a b c 1 2


lpop mylist 
lrange mylist 0 -1
2 1 c b a

rpop mylist2
lrange mylist2 0 -1
a b c 1 2

llen mylist
llen mylist3 

lpushx mylist h
lrange mylist 0 -1
h 2 1 c b a

lpushx mylist3 x

rpushx mylist2 t
lrange mylist2 0 -1
a b c 1 2 t

lpush mylist3 1 2 3
lpush mylist3 1 2 3
lpush mylist3 1 2 3
lrange mylist3 0 -1
3 2 1 3 2 1 3 2 1
lrem mylist3   mylist3 2 3  #(从头到尾删除2个3)
lrange mylist3 0 -1
2 1 2 1 3 2 1

lrem mylist3 -2 1   # 从右删除2个1
lrange mylist3 0 -1
2 1 2 3 2

lrem mylist3 0 2         # 删除所有的2
lrange mylist3 0 -1
1 3

lrange mylist 0 -1
h 2 1 c b a

lset mylist 3 mmm
lrange mylist 0 -1
t 2 1 mmm b a

lpush mylist4 a b c
lpush mylist4 a b c
lrange mylist4 0 -1
c b a c b a

linsert mylist4 before b 11
lrange mylist4 0 -1
c 11 b a c b a 
linsert mylist4 0 -1
c 11 b 22 a c b a

lpush mylist5 1 2 3 
lpush mylist6 a b c
rpoplpush mylist5 mylist6

lrange mylist5 0 -1
3 2
lrange mylist6 0 -1
1 c b a

```

rpoplpush 使用场景



存储Set
* 和list类型不同的是，set集合中不允许出现重复的元素
* set可包含最大元素的数量是429496295

存储set常用命令
* 添加/删除元素
* 获得集合中的元素
* 集合中的差集运算
* 集合中的交集运算
* 集合中的并集运算
* 扩展命令

```
sadd myset a b c
sadd myset a
sadd myset 1 2 3 

smembers myset
c b a 3 2 1

srem myset 1 2

sismember myset a #返回1 代表存在，返回0，代表不存在

sadd mya1 a b c
sadd myb1 a c 1 2
sdiff mya1 myb1
b

sadd mya2 a b c
sadd myb2 a c 1 2
sinter mya2 myb2
c a 

sadd mya3 a b c
sadd myb3 a c 1 2
sunion mya3 myb3 
c 2 b a 1

smembers myset

scard myset # 返回元素个数
srandmember myset #随机返回一个成员

sdiffstore my1 mya1 myb1 
smembers my1
b

sinterstore my2 mya2 myb2
smembers my2
c a

sunionstore my3 mya3 myb3
c 2 b a 1

```

存储set的使用场景
* 跟踪一些唯一性数据
* 用于维护数据对象之间的关联关系


存储sorted-set
* sorted-set与set的区别：
* sorted-set中的成员在集合中的位置是有序的


存储sorted-set常用命令
* 添加元素
* 获得元素
* 删除元素
* 查询元素
* 扩展查询

```
zadd mysort 70 zs 80 ls 90 ww
zadd mysort 100 zs # 100 replace 70
zadd mysort 60 tom
zscore mysort zs # 100
zcard mysort # 4
zrem mysort  tom ww

zadd mysort 85 jack 95 rose
zrange mysort 0 -1  # ls jack rose zs
zrange mysort 0 -1 withscores # ls 80 jack 85 rose 95 zs 100

zrevrange mysort 0 -1 withscores # zs 100 rose 95 jack 85 ls 80
zremrangebyrank mysort 0 4 # 

zadd mysort 80 zs 90 ls 100 ww
zremrangebyrank mysort 80 100 
zrange mysort 0 -1 


zadd mysort 70 zs 80 ls 90 ww
zrangebyscore mysort 0 100  # zs ls ww
zrangebyscre mysort 0 100 withscores # zs 70 ls 80 ww 90
zrangebyscre mysort 0 100 withscores limit 0 2   # zs 70 ls 80

zincrby mysort 3 ls
zscore mysort ls   # 83

zcount mysort 80 90   # 2

```


sorted-set使用场景：
* 游戏排名
* 微博热点话题
* 构建索引数据



## Keys的通用操作

```
keys * # 查看所有的keys

keys my? # 查看所有以my开头的三个字符的key

del my1 my2 my3 

exists my1  # 0

exists mya1 # 1

get company
rename company newcompany
get company # nil

expire newcompany 1000 # 设置过期时间，单位秒
ttl newcompany # 
type newcompany # string
type mylist # list
type myset # set
type myhash # hash
type mysort # zset

flushdb 清空当前库
flushall 清空所有
```

## Redis的特性

相关特性
* 多数据库
* redis事务

### 多数据库
一个redis实例可以包含多个数据库。
```
select 1 # 选择1号数据库
select 0 # 选择默认的0号数据库

move myset 1 # 将myset移动到1号数据库
```

### 事务
并行命令串行化
```
multi  # begin transaction
exec   # commit
discard  # rollback
```

```
set num 1
get num

```


## Redis的持久化

两种持久化方式
* RDB
* AOF

持久化的使用方式
* RDB  （隔一段时间就写一次磁盘）
* AOF （日志）
* 无持久化 （仅缓存）
* RDB + AOF 

### RDB

优点：
* 整个数据库仅包含一个文件
* 灾难恢复
* 性能最大化

劣势:

如果你需要尽量避免在服务器故障时丢失数据，那么 RDB 不适合你。 虽然 Redis 允许你设置不同的保存点（save point）来控制保存 RDB 文件的频率， 但是， 因为RDB 文件需要保存整个数据集的状态， 所以它并不是一个轻松的操作。 因此你可能会至少 5 分钟才保存一次 RDB 文件。 在这种情况下， 一旦发生故障停机， 你就可能会丢失好几分钟的数据。每次保存 RDB 的时候，Redis 都要 fork() 出一个子进程，并由子进程来进行实际的持久化工作。 在数据集比较庞大时， fork() 可能会非常耗时，造成服务器在某某毫秒内停止处理客户端； 如果数据集非常巨大，并且 CPU 时间非常紧张的话，那么这种停止时间甚至可能会长达整整一秒。 虽然 AOF 重写也需要进行 fork() ，但无论 AOF 重写的执行间隔有多长，数据的耐久性都不会有任何损失

配置：

```
vim redis.conf


```

### AOF

优势:

使用 AOF 持久化会让 Redis 变得非常耐久（much more durable）：你可以设置不同的 fsync 策略，比如无 fsync ，每秒钟一次 fsync ，或者每次执行写入命令时 fsync 。 AOF 的默认策略为每秒钟 fsync 一次，在这种配置下，Redis 仍然可以保持良好的性能，并且就算发生故障停机，也最多只会丢失一秒钟的数据（ fsync 会在后台线程执行，所以主线程可以继续努力地处理命令请求）。AOF 文件是一个只进行追加操作的日志文件（append only log）， 因此对 AOF 文件的写入不需要进行 seek ， 即使日志因为某些原因而包含了未写入完整的命令（比如写入时磁盘已满，写入中途停机，等等）， redis-check-aof 工具也可以轻易地修复这种问题。 
Redis 可以在 AOF 文件体积变得过大时，自动地在后台对 AOF 进行重写： 重写后的新 AOF 文件包含了恢复当前数据集所需的最小命令集合。 整个重写操作是绝对安全的，因为 Redis 在创建新 AOF 文件的过程中，会继续将命令追加到现有的 AOF 文件里面，即使重写过程中发生停机，现有的 AOF 文件也不会丢失。 而一旦新 AOF 文件创建完毕，Redis 就会从旧 AOF 文件切换到新 AOF 文件，并开始对新 AOF 文件进行追加操作。AOF 文件有序地保存了对数据库执行的所有写入操作， 这些写入操作以 Redis 协议的格式保存， 因此 AOF 文件的内容非常容易被人读懂， 对文件进行分析（parse）也很轻松。 导出（export） AOF 文件也非常简单： 举个例子， 如果你不小心执行了 FLUSHALL 命令， 但只要 AOF 文件未被重写， 那么只要停止服务器， 移除 AOF 文件末尾的 FLUSHALL 命令， 并重启 Redis ， 就可以将数据集恢复到 FLUSHALL 执行之前的状态。

劣势: 
对于相同的数据集来说，AOF 文件的体积通常要大于 RDB 文件的体积。根据所使用的 fsync 策略，AOF 的速度可能会慢于 RDB 。 在一般情况下， 每秒 fsync 的性能依然非常高， 而关闭 fsync 可以让 AOF 的速度和 RDB 一样快， 即使在高负荷之下也是如此。 不过在处理巨大的写入载入时，RDB 可以提供更有保证的最大延迟时间（latency）。AOF 在过去曾经发生过这样的 bug ： 因为个别命令的原因，导致 AOF 文件在重新载入时，无法将数据集恢复成保存时的原样。 （举个例子，阻塞命令 BRPOPLPUSH 就曾经引起过这样的 bug 。） 测试套件里为这种情况添加了测试： 它们会自动生成随机的、复杂的数据集， 并通过重新载入这些数据来确保一切正常。 虽然这种 bug 在 AOF 文件中并不常见， 但是对比来说， RDB 几乎是不可能出现这种 bug 的。

配置
```
appendonly yes
appendfsync always
#appendfsync every sec
#appendfsync no
# 重启redis


```






## 总结

flushall 清空数据库
