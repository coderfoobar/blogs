
# VMWare Workstation 12 与 Centos7 下Hadoop虚拟机集群的样板机搭建

> Create Time : 2017年1月16日

## 通过VMWare Workstation 12与 centos7.iso安装centos虚拟机

1. 下载centos7的iso文件

> 官网地址： `https://www.centos.org/download/`

2. 安装虚拟机（步骤省略）

注意： 不要添加用户，只需要给`root`用户设置密码即可。`hadoop`在后续步骤中通过命令添加。

## 调整分辨率及显示字体大小

> 因为我的笔记本是15.6寸4K屏幕，所以，如果分辨率合适，此步骤可以跳过。

VMWare Workstation使用最新版的，虚拟机安装成功后，会自动调整到合适的分辨率（早期版本的VMWare似乎不支持高分屏）。
但是，字体会比较小。

这时，通过按Windows徽标，在最上边输入'them'，`打开Tweak Tool`,切换`Font`选项卡，右侧，将字体调整到合适大小（我的是24）。另外，可以通过`Scaling Factor`适当放大图标等（我的是1.25）。


## 网络配置

1. 更改网卡名称

> 不想更改,可以跳过。本节参考自：http://blog.csdn.net/Jmilk/article/details/51629299

1.1.  虚拟机安装成功之后，默认网卡是类似于`eno16777736`的名字。如果不喜欢可以通过修改grub配置文件即可。

```shell
# root 身份
$ vi /etc/sysconfig/grub

#打开grub文件----------------------

# 在GRUB\_CMDLINE_LINUX配置项中加入
net.ifnames=0 biosdevname=0         
#不启用BIOSDEV的配置
#保存，退出----------------------

```

1.2. 重新加载grub配置

```shell
grub2-mkconfig -o /boot/grub2/grub.cfg
```

1.3. 修改网卡配置文件名

```shell
$ cd /etc/sysconfig/network-scripts
$ mv ifcfg-eno16777736 ifcfg-eth0
```

1.4. 配置网卡

```shell
$ vi /etc/sysconfig/network-scripts/ifcfg-eth0

#------------------
NAME=eth0
DEVICE=eth0
ONBOOT=yes
#------------------

```

1.5. 重启系统生效

> 如果不生效，继续向下配置。

```shell

$ reboot
$ ifconfig #查看网卡信息

```

2. 配置网络

2.1. VMWare Workstation 配置通过 NAT 方式上外网。

2.2. 配置静态IP：

```shell
$ vi /etc/sysconfig/network-scripts/ifcfg-eth0

#------------------
#IP ， 网关 ， DNS ， 子网掩码 配成合适的即可
TYPE=Ethernet
BOOTPROTO=static
DEFROUTE=yes
IPV4_FAILURE_FATAL=no
IPV6INIT=yes
IPV6_AUTOCONF=yes
IPV6_DEFROUTE=yes
IPV6_FAILURE_FATAL=no
NAME=eth0
UUID=50c415e9-93db-4507-b458-0df39d098477
DEVICE=eth0
ONBOOT=yes
IPADDR=192.168.84.1
NETMASK=255.255.255.0
GATEWAY=192.168.84.254
DNS1=192.168.84.254
PEERDNS=yes
PEERROUTES=yes
IPV6_PEERDNS=yes
IPV6_PEERROUTES=yes
#------------------
```

2.3. 配置主机名称及Gateway

```shell
$ vi /etc/sysconfig/network

#-------------------------- 
NETWORKING=yes
HOSTNAME=hadoop1
GATEWAY=192.168.84.254
#--------------------------
```

2.4. 配置udev

```shell
$ vi /etc/udev/rules.d/70-persistent-ipoib.rules

#---------
ACTION=="add", SUBSYSTEM=="net", DRIVERS=="?*", ATTR{type}=="1", ATTR{address}=="00:0c:29:ca:30:a0", NAME="eth0"
#---------
```

2.5. 配置DNS

```shell
$ vi /etc/resolv.conf

#------
search localdomain
nameserver 192.168.84.254
#------
```

2.5. 重启机器生效



## 配置hosts文件映射

```shell
vi /etc/hosts

#  添加----------
192.168.84.1 hadoop1
192.168.84.2 hadoop2
192.168.84.3 hadoop3
#----------
```

重启网络服务:

```shell
$ /etc/init.d/network restart
# 或者
$ service network restart
```

## 关闭iptables

```shell
chkconfig iptables off
```

## 关闭SELinux

* 1. 使用`getenforce`命令查看是否关闭
* 2. 修改`/etc/selinux/config`文件,将SELINUX=enforcing 改为SELINUX=disabled，修改完成后重启机器生效。


# 配置运行环境

## 更新OpenSSL

```shell
$ yum update openssl
```

## 修改SSH配置文件

* 1. 以root用户使用如下命令打开sshd_config配置文件
```shell
$ vi /etc/ssh/sshd_config
# -----------------------开放三个配置

RSAAuthentication yes
PubkeyAuthentication yes
AuthorizedKeyFile .ssh/authorized_keys

# -----------------------
```

* 2. 配置后重启服务

```shell
$ service sshd restart
```

## 增加hadoop组和用户

使用如下命令增加hadoop用户组和hadoop用户（密码），创建hadoop组件存放目录
```shell
$ groupadd -g 1000 hadoop1
$ useradd -u 2000 -g hadoop hadoop
$ mkdir -p /app/hadoop
$ chown -R hadoop:hadoop /app/hadoop
$ passwd hadoop
```

创建hadoop用户上传文件目录，设置该目录和文件夹为hadoop
```shell
$ mkdir /home/hadoop/upload
$ chown -R hadoop:hadoop /home/hadoop/upload
```


## 安装Java

## 安装Scala

## 安装Maven

## 安装svn

## 编译安装hadoop

## 编译安装Spark




































