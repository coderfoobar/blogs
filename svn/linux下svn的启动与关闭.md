# linux下svn的启动与关闭

## 启动

1. 通过`find / -name svnserve.conf`查找`svn仓库目录`
2. `svnserve -d -r /var/svn/svnrepos/` 启动`svn`，其中`/var/svn/svnrepos/`是`svn仓库目录`

---

## 开机自启动

```shell  SetupSvnServe

#!/bin/bash
#chkconfig:23456 80 05  --指定在哪几个级别执行，0一般指关机，6指的是重启，其他为正常启动。80为启动的优先级，05为关闭的优先级别  
#description : Setup SVN Server
#filename : SetupSvnServe

svnserve -d -r /var/svn/svnrepos
```

* 将脚本`SetupSvnServe`放入`/etc/init.d`
* `chmod +x /etc/rc.d/init.d/SetupSvnServe`
* `chkconfig --add  SetupSvnServe` 
* `chkconfig --list SetupSvnServe`


---

## 关闭

1. `ps -ef | grep svn` 查找进程`PID`
2. `kill -9 PID`