
# Ubuntu 开启IPV6

> create time ： 2016年12月20日

> http://network.51cto.com/art/201312/424926.htm

* 1. 安装miredo

```shell
$ sudo apt-get install miredo
``` 

* 2. 修改防火墙IPv6配置

以`root`权限打开ufw配置文件`/etc/default/ufw` ，找到`IPV6=no`这一行，将其改为`IPV6=yes`。

最后禁用/启用防火墙来使刚刚的改动生效：

```shell
$ sudo ufw disable
$ sudo ufw enable
$ sudo ufw allow 22/tcp
```

* 3. 重启网络

```shell
$ sudo invoke-rc.d networking restart
```

* 4. 测试是否设置成功

```shell
$ ping6 ipv6.google.com
ping : sendmsg : Operation not permitted
ping : sendmsg : Operation not permitted
```

* 5. 设置ip6tables

```shell
$ sudo ip6tables -L
$ sudo ip6tables -t filter -A OUTPUT -j ACCEPT
```


