
# 简版 - hadoop centos虚拟机集群环境搭建

## 样板机

### 安装CentOS虚拟机，并调整优化分辨率等设置

### 更改网卡名称，配置静态IP，更改主机名

### 关闭防火墙及SELinux

### 增加hadoop组和hadoop用户,将hadoop添加到sudoers

### 卸载OpenJDK，安装Oracle JDK，配置环境变量

### 安装Scala，配置环境变量

### 安装Maven，配置环境变量

### 安装svn和git

---

## 集群

### 克隆样板机

* 更改用户`注释性描述`：
/etc/passwd文件每一行表示一个用户，用户的信息用`:`隔开，分别表示:
```
用户名:口令:用户标识号:组标识号:注释性描述:主目录:登录Shell
```
* 更改IP地址

### ssh免密码登录配置


### 编译安装hadoop-**2.7.3**

#### root用户安装 autoconf automake libtool cmake

```shell
sudo yum install autoconf automake libtool cmake
```

#### root用户安装ncurses-devel

```shell
sudo yum install ncurses-devle
```

#### root用户安装openssl-devel

```shell
sudo yum install openssl-devel
```

#### root用户安装gcc*

```shell
sudo yum install gcc*
```

#### root用户安装protobuf-**2.5.0**

> 注意版本号**2.5.0**

```shell
$ ./autogen.sh
$ ./configure
$ make
$ make check
$ make install

# 验证
$ protoc # 输出: Missing input file.
$ protoc # 输出: libprotoc 2.5.0

# 卸载
$ make uninstall
```


#### 编译hadoop-2.7.3

### 编译安装spark

#### R


> R语言  官网: https://cran.rstudio.com/  下载 R-3.3.2.tar.gz

```shell
# root用户安装

## 前置软件安装

yum -y groupinstall 'Development Tools'
yum -y install gfortran
yum install bzip2-libs
yum install bzip2-devel
yum -y install xz-devel.x86_64
yum install libcurl-devel.x86_64
yum install fonts-chinese
yum install mesa-libGLU mesa-libGLU-devel
yum -y install readline-devel tcl tcl-devel tclx tk tk-devel libX11-devel libXtst-devel xorg-x11-xtrans-devel libpng-devel libXt-devel
```

```shell
# 编译&安装 及 环境变量配置

./configure --prefix /usr/R-3.3.2 --enable-R-shlib --with-readline=yes --with-x=yes --with-tcltk=yes --with-cairo=yes --with-libpng=yes --with-jpeglib=yes --with-libtiff=yes --with-aqua=yes --with-ICU=yes --with-libcurl=yes --enable-utf8
make
make install

## 环境变量 
vim /etc/profile  ### 输入
export R_HOME=/usr/R-3.3.2/lib64/R
export PATH=$PATH:$R_HOME/bin
### 使环境变量生效
source /etc/profile
```


```shell
## 验证
R CMD javareconf
### 输出
Java interpreter : /usr/lib/java/jdk1.8.0_111/jre/bin/java
Java version     : 1.8.0_111
Java home path   : /usr/lib/java/jdk1.8.0_111
Java compiler    : /usr/lib/java/jdk1.8.0_111/bin/javac
Java headers gen.: /usr/lib/java/jdk1.8.0_111/bin/javah
Java archive tool: /usr/lib/java/jdk1.8.0_111/bin/jar

trying to compile and link a JNI program 
detected JNI cpp flags    : -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux
detected JNI linker flags : -L$(JAVA_HOME)/jre/lib/amd64/server -ljvm
gcc -std=gnu99 -I/usr/R-3.3.2/lib64/R/include -DNDEBUG -I/usr/lib/java/jdk1.8.0_111/include -I/usr/lib/java/jdk1.8.0_111/include/linux -I/usr/local/include    -fpic  -g -O2  -c conftest.c -o conftest.o
gcc -std=gnu99 -shared -L/usr/R-3.3.2/lib64/R/lib -L/usr/local/lib64 -o conftest.so conftest.o -L/usr/lib/java/jdk1.8.0_111/jre/lib/amd64/server -ljvm -L/usr/R-3.3.2/lib64/R/lib -lR


JAVA_HOME        : /usr/lib/java/jdk1.8.0_111
Java library path: $(JAVA_HOME)/jre/lib/amd64/server
JNI cpp flags    : -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux
JNI linker flags : -L$(JAVA_HOME)/jre/lib/amd64/server -ljvm
Updating Java configuration in /usr/R-3.3.2/lib64/R
Done.
```

### Spark-2.1.0的编译,安装，分发，启动，验证

* 下载Spark源码，解压，编译
```shell
./dev/make-distribution.sh --name 2.1.0 --tgz -Psparkr   -Phadoop-2.7  -Phive -Phive-thriftserver -Pmesos -Pyarn -Pspark-ganglia-lgpl 
```

* 安装

在编译完成后，Spark源码的根目录会构建成功Spark的安装包：`spark-2.1.0-bin-2.1.0.tgz` 。将安装包解压至`/app/hadoop`下，安装目录为：`/app/hadoop/spark-2.1.0`。

进入安装目录`/app/hadoop/spark-2.1.0`，`./conf`目录下的文件是Spark的配置文件。默认情况下，Spark下只有配置文件的模板。执行以下操作，配置Spark集群运行环境。

```shell
# 复制配置文件
cp slaves.template slaves
cp spark-env.sh.template spark-env.sh
```

```shell
# 编辑 slaves 与 spark-env.sh
# vi slaves，添加以下内容（注意删除localhost）：
hadoop1
hadoop2
hadoop3

# vi spark-env.sh，添加以下配置：
export JAVA_HOME=/usr/lib/java/jdk1.8.0_111
export SPARK_HOME=/app/hadoop/spark-2.1.0
export SPARK_MASTER_IP=hadoop1

```

* 分发到集群的其他节点上

```shell
scp -r /app/hadoop/spark-2.1.0 hadoop@hadoop2:/app/hadoop
scp -r /app/hadoop/spark-2.1.0 hadoop@hadoop3:/app/hadoop
```

* 启动

```shell
source /app/hadoop/spark-2.1.0/conf/spark-env.sh
sh /app/hadoop/spark-2.1.0/sbin/start-all.sh
```

> 注意： 启动spark之前，需要关闭防火墙: `systemctl stop firewalld.service`。

防火墙相关命令一览：
```shell
# 查看防火墙状态
systemctl status firewalld.service

# 关闭防火墙
systemctl stop firewalld.service

# 开启防火墙
systemctl start firewalld.service

# 开机时不启动防火墙
systemctl disable firewalld.service

# 开机时启动防火墙
systemctl enable firewalld.service
```

* 验证

在浏览器中输入`http://hadoop1:8080` ，打开集群管理页面，看到三个worker节点已经启动。如果在管理页面只看到一个worker（master机器）启动，需要查看是不是未关闭防火墙，或者查看相应节点的log，获取更详细的启动信息。

#### Spark Shell



### Hive 

> MySQL : https://dev.mysql.com/downloads/mysql/

* 卸载mariadb

通过命令`rpm -qa | grep -i mariadb`查看默认CentOS系统安装的mariadb数据库，如果有输出结果，则卸载；
```shell
# 卸载
sudo rpm -ev mariadb-libs-5.5.44-2.el7.centos.x86_64
# 如果报错如下，就强制卸载
#  libmysqlclient.so.18()(64bit) 被 (已安裝) postfix-2:2.10.1-6.el7.x86_64 需要
#  libmysqlclient.so.18(libmysqlclient_18)(64bit) 被 (已安裝) postfix-2:2.10.1-6.el7.x86_64
# 强制卸载
sudo rpm -e --nodeps  mariadb-libs-5.5.44-2.el7.centos.x86_64
```

* 卸载旧的MySQL

通过命令`rpm -qa | grep -i mysql`查看默认CentOS系统安装的MySQL数据库，如果有输出结果，则卸载；否则安装Oracle-MySQL官网下载的版本。

* 安装新的MySQL

分别有common, lib , client ,  devel , server 五个部分。
```shell
sudo rpm -ivh mysql-community-common-5.7.17-1.el7.x86_64.rpm 
sudo rpm -ivh mysql-community-libs-5.7.17-1.el7.x86_64.rpm 
sudo rpm -ivh  mysql-community-client-5.7.17-1.el7.x86_64.rpm
sudo rpm -ivh  mysql-community-devel-5.7.17-1.el7.x86_64.rpm
sudo rpm -ivh  mysql-community-server-5.7.17-1.el7.x86_64.rpm
```

```shell
# mysql服务相关操作
sudo systemctl status mysqld   # 查看mysql服务状态
sudo systemctl start mysqld    # 启动mysql服务
sudo systemctl stop mysqld     # 停止mysql服务
sudo systemctl enable mysqld   # 开机自启动mysql服务
sudo systemctl disable msyqld  # 开机不启动mysql服务
```

* 配置MySQL

```shell
# 停止mysql服务
sudo systemctl stop mysqld
# 配置默认编码为utf-8
vi /etc/my.cnf
#------------
[mysqld]
charater_set_server=utf8
init_connect='SET NAMES utf8'
#------------
```

* 启动MySQL

```shell
# 启动mysql服务
sudo systemctl enable msyqld 
sudo systemctl start mysqld
# 查看临时密码
grep 'temporary password' /var/log/mysqld.log
#-----------
A temporary password is generated for root@localhost: W._AjVgyy8p5
#-----------
# 登录
mysql -u root -p:
# 输入上边得到的临时密码

# mysql console start------------
mysql> alter user 'root'@'localhost' identified by '!QAZxsw2';
# mysql5.7中默认有密码安全策略，如果密码过于简单会报错,更改密码安全策略:
mysql > show variables like '%password%';
mysql > set global validate_password_length=1;
mysql > set global validate_password_policy=0;
mysql> exit
# mysql console end -------------
```

> Hive : https://mirrors.tuna.tsinghua.edu.cn/apache/hive/stable-2/

* 创建Hive用户,授权,创建Hive数据库
```shell
# root登录，创建hive用户，并授权
$ mysql -u root -p
# mysql console start -----------------
mysql> create user 'hive'@'%' identified by 'hive';
mysql> grant all privileges on *.* to 'hive'@'%' with grant option;
mysql> flush privileges;
mysql> quit;  
# mysql console end  ------------------

# hive用户登录,创建数据库
$ mysql -u hive -p
# mysql console start -----------------
mysql > create database hive;
mysql > exit;
# mysql console end  ------------------
```

* 下载,解压，安装，配置Hive

```shell
tar -zxvf apache-hive-2.1.1-bin.tar.gz
mv apache-hive-2.1.1-bin /app/hadoop/hive-2.1.1
# 将mysql连接驱动jar包放在/app/hadoop/hive-2.1.1

```

#### 安装MySQL

#### 配置Hive

#### Hive Console

### Spark SQL

#### Spark SQL CLI

#### Spark Thrift Server


