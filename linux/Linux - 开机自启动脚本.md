
# Linux开机自启动脚本

## root权限编辑`/etc/rc.local`

```shell
#!/bin/sh
#
# This script will be executed *after* all the other init scripts.
# You can put your own initialization stuff in here if you don't
# want to do the full Sys V style init stuff.

# add custom goals to be executed

```

> Linux在启动时，会自动执行`/etc/rc.d`目录下的所有初始化程序，`rc.local`是在完成所有初始化之后执行的

---

## 编辑执行脚本，放在`/etc/init.d`下作为服务启动

> `/etc/init.d`下都是可执行脚本，实际上，它们都是服务脚本，按照一定的格式编写，Linux在启动时会自动执行，类似与Windows下的服务。

* 1. 编写脚本SetupOnStart

```shell
#!/bin/bash
#chkconfig:23456 80 05 --指定在哪几个级别执行，0一般指关机，6指的是重启，其他为正常启动。80为启动的优先级，05为关闭的优先级别  
#description: Setup on Start

# Users goals to be executed.

```

* 2. 执行以下命令： 

```shell
$ chmod +x SetupOnStart
$ chkconfig --add SetupOnStart
$ chkconfig --list SetupOnStart
```


---