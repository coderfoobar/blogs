
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

#### Spark 编译安装

* 下载Spark源码，解压，编译
```shell
./dev/make-distribution.sh --name 2.1.0 --tgz -Psparkr   -Phadoop-2.7  -Phive -Phive-thriftserver -Pmesos -Pyarn -Pspark-ganglia-lgpl 
```

* 安装



### Hive 

#### 安装MySQL

#### 配置Hive

#### Hive Console

### Spark SQL

#### Spark SQL CLI

#### Spark Thrift Server

#### Spark Shell
