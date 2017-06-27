
# Memcached

> Create Time : 2017年6月19日 Author:huduku.io

# 什么是Memcache ？

分布式 

高速

## Memcache有什么用

缓存层，减轻数据库的压力。

## 怎样理解Memcache ？

只有一张表的数据库。

Key-value

# 使用场景

1. 非持久化存储： 对数据存储要求不高
2. 分布式存储：不适合单机使用
3. key/value存储：格式简单，不支持List，Array等数据格式

# Memcache服务端的安装过程

## 安装前说明 

1. 编译安装Libevent Memcache （优点：进行自定义的设置，缺点：）
2. yum、apt-get
3. Memcache和Memcached（升级版）的区别

## 实际操作

```
$ yum -y install memcached
```

```
$ /usr/bin/memcached -d -l 127.0.0.1 -p 11211 -m 150 -u root
## 
$ ps -ef | grep memcached

```

## Memcache 客户端的安装过程

1.  安装Libmemcached
2. 为PHP安装memcached扩展

```
$ tar -zxvf libmemcached-1.0.18.tar.gz
$ cd libmemcached-1.0.18
$ ./confiure --prefix=/usr/lib/libmemcached
$ make && make install

$ tar -zxvf memcached-2.2.0.tgz
$ cd memcached-2.2.0
$ phpize
```




