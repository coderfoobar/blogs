

# Centos - 克隆hadoop样板机集群

> Create Time : 2017年1月17日

## 克隆

VMWare Workstation：

选中hadoop1虚拟机 --> 管理 --> 克隆。

克隆出两份虚拟机，命名为`hadoop2`和`hadoop3`.

## 网络配置 

分别在`hadoop1`,`hadoop2`,`hadoop3`中做如下配置：

1. 修改相应的ifcfg-eth0网卡的配置
2. 修改相应的/etc/hosts文件
3. 重启`service sshd restart` 和 `service network restart`

## 免密码ssh登录配置

基于样板机中`/etc/ssh/sshd_config`中的配置:
```shell
RSAAuthentication yes
PubkeyAuthentication yes
AuthorizedKeyFile .ssh/authorized_keys
```

1. 在`hadoop1`,`hadoop2`,`hadoop3`中执行如下shell命令：

```shell
$ cd ~/.ssh
$ ls
# 如果下面有文件执行下面三行
$ rm -rf * 
$ service sshd restart
$ service network restart
# 生成
$ ssh-keygen -t rsa   #什么都不要填写，一直按回车键，直到结束
$ ssh-add id_rsa
# 在三台机器中均执行相同的代码（作用是将id_rsa.pub中的内容均追加至authorized_keys问价），如下：
$ ssh-copy-id hadoop@hadoop1

```

2. 将`hadoop1`中的`authorized_keys`文件传送至`hadoop2`及`hadoop3`

```shell
$ scp ~/.ssh/authorized_keys hadoop@hadoop2:/home/hadoop/.ssh/
$ scp ~/.ssh/authorized_keys hadoop@hadoop3:/home/hadoop/.ssh/
```

3. 在三台机器中均重启`sshd`服务

```shell
$ service sshd restart
```

4. 验证

```shell
# 如在hadoop1中，发现不需要密码即可登录
$ ssh hadoop@hadoop2

```

---

# 问题

1. Agent admitted failure to sign using the key

解答 ： 执行`ssh-add id_rsa`.

2. 踩了一天的坑，`hadoop1`(`192.168.84.1`) 能`ssh`登录到 `hadoop2`(`192.168.84.2`)和 `hadoop3`(`192.168.84.3`) 上，但是`hadoop2`和`hadoop3`死活不能`ssh`到`hadoop1`上，还爆出`Permission denied, Please try again.`,之后`ping`都`ping`不通（在`hadoop2` `ssh` 登录到`hadoop1`之前，`hadoop2`能`ping`通`hadoop1`）。

解答: `hadoop1`(`192.168.84.1`) 的`IP`与`VMWare`的虚拟网卡`VMnet8`的`IP`不能相同。
在Windows下通过`ipconfig`查看`VMnet8`的`IP`：
```bat
以太网适配器 VMnet8:

   连接特定的 DNS 后缀 . . . . . . . :
   本地链接 IPv6 地址. . . . . . . . : fe80::7d1b:511:ec0d:be54%16
   IPv4 地址 . . . . . . . . . . . . : 192.168.84.1
   子网掩码  . . . . . . . . . . . . : 255.255.255.0
   默认网关. . . . . . . . . . . . . :
```

如此，有两套解决方案：

* 在`Windows`上修改`VMnet8`的`IP`,需要`禁用/重启`。
* 在所有虚拟机上修改`/etc/hosts`文件中`hadoop1`对应的`IP`，以及`hadoop1`中`eth0`网卡的`IPADDR`(修改文件 `/etc/sysconfig/network-scripts/ifcfg-eth0`)。

显然，第一种方式比较简单。
