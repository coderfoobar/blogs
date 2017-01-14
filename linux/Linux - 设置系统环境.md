
# Linux - 设置系统环境

> Create Time : 2017年1月14日


## 设置机器名


以`root`身份登陆，试用`vi /etc/sysconfig/network`打开配置文件，根据实际情况设置该服务器的机器名，新机器名在重启后生效。

```shell
$ vi /etc/sysconfig/network

# ---------------------

NETWORKING=yes
HOSTNAME=hadoop1

#-----------------------
```

## 设置IP地址

* 1. 点击System --> Preferences --> Network Connections
* 2. 修改或重建网络连接，设置该连接为手工方式，设置如下网络信息：
        * IP地址 - 192.168.0.61 
        * 子网掩码 - 255.255.255.0
        * 网关 - 192.168.0.1
        * DNS - 221.12.1.227

***注意：***
* 1. 网管、DNS等根据所在网络实际情况进行设置，并设置谅解方式为"Available to all users",否则通过远程连接时会在服务器重启后无法连接。
* 2. 如果是运行在VMWare 虚拟机，网络试用桥接模式，设置能够连接到互联网中，以方便后面Hadoop和Spark编译等试验。
* 3. 在命令行中，试用ifconfig命令查看设置IP地址信息，如果修改IP不生效，需要重启机器，再进行设置（如果该机器在设置后需要通过远程访问，建议重启机器，确认IP是否生效）

## 设置Host映射文件

* 1. 使用`root`身份编辑`/etc/hosts`映射文件，设置IP地址与机器名映射，设置信息如下：
        * 192.168.0.61 hadoop1
        * 192.168.0.62 hadoop2
        * 192.168.0.63 hadoop3

* 2. 使用如下命令对网络设置进行重启：
```shell
$ /etc/init.d/network restart
# 或者
$ service network restart

```
* 3. 验证是否成功
```shell
$ ping hadoop1 
```

## 关闭防火墙

在hadoop安装过程中需要关闭防火墙和SELinux，否则会出现异常

* 1. service iptables status 查看防火墙状态，如下表示iptables 已经开启

* 2. 以root用户使用命令`chkconfig iptables off`关闭iptables。

## 关闭SELinux

* 1. 使用`getenforce`命令查看是否关闭
* 2. 修改`/etc/selinux/config`文件,将SELINUX=enforcing 改为SELINUX=disabled，修改完成后重启机器生效。




