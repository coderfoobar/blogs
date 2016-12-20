
# Ubuntu - 防火墙配置

> create time : 2016年12月20日

> http://blog.163.com/harry.hh@126/blog/static/4625486820123256516805/


* 1. 安装

```shell
$ sudo apt-get install ufw
```

* 2. 禁用/启用

```shell
$ sudo ufw disable
$ sudo ufw enable
```

* 3. 开启/禁用 端口/协议

```shell
sudo ufw allow | deny [service]
```

```shell
$ sudo ufw allow smtp # <==> sudo ufw allow 25/tcp
$ sudo ufw allow 22/tcp
$ sudo ufw allow 53  # 53 tcp/udp
$ sudo ufw allow from 192.168.1.100 #允许此IP访问所有的本机端口
$ sudo ufw allow proto udp 192.168.0.1 port 53 to 192.168.0.2 port 53
$ sudo ufw deny smtp # 禁止外部访问smtp服务
$ sudo ufw delete allow smtp # 删除上面建立的某条规则
```

* 4. 查看防火墙状态

```shell
$ sudo ufw status
```

* 5. 转换日志状态

```shell
$ sudo ufw logging on | off
```
