
# MySQL 5.7 压缩包安装

> Create Time : 2017年1月1日

MySQL5.7 64位windows版只有压缩包安装的方式。（32位有windows installer与压缩包安装两种方式）。

* 1. 官网下载

```url
http://dev.mysql.com/downloads/mysql/
```

* 2. 解压到安装目录

```url
C:/Program Files
# -->
C:/Program Files/mysql-5.7.13-winx64
```

* 3. 在安装目录下创建data文件夹

```url
C:/Program Files/mysql-5.7.13-winx64/data
```

* 4. 复制一份my-default.ini，命名为my.ini，修改配置文件（已删除部分注释）

```cfg

[mysqld]

# basedir 为安装文件解压后的目录 ｜ basedir和datadir 可以使用相对路径
# basedir=./..
basedir=C:/Program Files/mysql-5.7.13-winx64
# datadir 为用来存放数据的目录
# datadir=./../data
datadir=C:/Program Files/mysql-5.7.13-winx64/data
# port 为端口号
port=3306
# mar_connections为最大连接数
max_connections=20
character_set_server=utf8

explicit_defaults_for_timestamp=true
sql_mode=NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES
```

* 5. 初始化data目录

```MySQL
mysqld --defaults-file="D:\victor.min\software\mysql-5.7.11-winx64\my.ini" --initialize-insecure
```

* 6. 安装MySQL服务

```MySQL
mysqld -install
```

* 7. 启动服务

```MySQL
net start mysql
```

> 在上述步骤中如果`出错`,运行命令`mysqld remove`，删除MySQL服务，并删除`第3步`中创建的`data`目录，从`第1步`重新来过。

* 8. 通过`root`用户登录

```MySQL
mysql -uroot -p
```


---

> 安装完成后，可能用到的创建用户及授权语句:

```sql
alter user 'root'@'localhost' identified by '123';
create database victor;
create user 'victor'@'localhost' identified by 'victor@123';
flush privileges;
creaet user 'victorremote'@'%' identified by 'victorremote@123';
flush privileges;
grant all on victor.* to 'victor'@'localhost' identified by 'victor@123';
flush privileges;
grant all on victor.* to 'victorremote'@'%' identified by 'victorremote@123';
flush privileges;

# =============================================================

show variables like 'collation_%';
show variables like 'character_set_%';
set collation_connection=utf8_general_ci;
set character_set_client=utf8;
set character_set_connection=utf8;
set character_set_results=utf8;
```
