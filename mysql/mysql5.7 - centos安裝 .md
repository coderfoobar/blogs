


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

---

```
yum -y install yum-utils
```

Step1: 检测系统是否自带安装MySQL

#yum list installed | grep mysql
Step2: 删除系统自带的mysql及其依赖 命令：

# yum -y remove mysql-libs.x86_64
Step3: 给CentOS添加rpm源，并且选择较新的源 命令：

#wget dev.mysql.com/get/mysql-community-release-el6-5.noarch.rpm
#yum localinstall mysql-community-release-el6-5.noarch.rpm
# yum repolist all | grep mysql
# yum-config-manager --disable mysql55-community
# yum-config-manager --disable mysql56-community
# yum-config-manager --enable mysql57-community-dmr
# yum repolist enabled | grep mysql
Step4:安装mysql 服务器 命令：

# yum install mysql-community-server
Step5: 启动mysql 命令:

#service mysqld start
Step6: 查看mysql是否自启动,并且设置开启自启动 命令:

# chkconfig --list | grep mysqld
# chkconfig mysqld on
Step7: mysql安全设置 命令：

# mysql_secure_installation
参考相关文档地址: http://www.rackspace.com/knowledge_center/article/installing-mysql-server-on-centoshttp://dev.mysql.com/doc/refman/5.7/en/linux-installation-yum-repo.htmlhttp://www.cnblogs.com/xiaoluo501395377/archive/2013/04/07/3003278.html

---