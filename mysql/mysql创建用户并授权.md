
# Mysql 创建用户并授权

<!-- TOC -->

- [Mysql 创建用户并授权](#mysql-%E5%88%9B%E5%BB%BA%E7%94%A8%E6%88%B7%E5%B9%B6%E6%8E%88%E6%9D%83)
    - [创建用户](#%E5%88%9B%E5%BB%BA%E7%94%A8%E6%88%B7)
    - [授权](#%E6%8E%88%E6%9D%83)
    - [设置与更改用户密码](#%E8%AE%BE%E7%BD%AE%E4%B8%8E%E6%9B%B4%E6%94%B9%E7%94%A8%E6%88%B7%E5%AF%86%E7%A0%81)
    - [撤销用户权限](#%E6%92%A4%E9%94%80%E7%94%A8%E6%88%B7%E6%9D%83%E9%99%90)
    - [删除用户](#%E5%88%A0%E9%99%A4%E7%94%A8%E6%88%B7)

<!-- /TOC -->

## 创建用户

> 命令 : `CREATE USER 'username'@'host' IDENTIFIED BY 'password'`;

说明：

* `username` - 创建的数据库用户名
* `host` - 指定改用户在哪个主机上可以登录数据库，如果时本地用户，可用localhost，如果想让改用户可以从任意远程主机登录，可以使用通配符`%`。
* `password` - 该用户的登录密码，密码可以为空人，如果为空，则该用户可以不需要密码登录服务器。

例如 ： 

```sql
CREATE USER 'dog'@'localhost' IDENTIFIED BY '123456'; 
CREATE USER 'pig'@'192.168.1.101' IDENDIFIED BY '123456'; 
CREATE USER 'pig'@'%' IDENTIFIED BY '123456'; 
CREATE USER 'pig'@'%' IDENTIFIED BY ''; 
CREATE USER 'pig'@'%'; 
```

## 授权

> 命令 ：`GRANT privileges ON databasename.tablename TO 'username'@'host'`

说明：

* `privileges` - 用户操作权限，如`SELECT,INSERT,UPDATE`等（详细列表见该文最后面），如果要授予所有权限则用`ALL` 
* `databasename` - 数据库名
* `tablename` - 表名，如果要对该用户授予所有数据库和表的权限则可用通配符`*`表示，如`*.*`。

例如 ： 

```sql
GRANT SELECT , INSERT ON test.user TO 'pig'@'%';
GRANT ALL ON *.* TO 'pig'@'%';
```  

注意 ： 
用以上命令授权的用户不能给其他用户授权，如果想让该用户可以授权，用以下命令：
```sql
GRANT privileges ON databasename.tablename TO 'username'@'host' WITH GRANT OPTION;
```

## 设置与更改用户密码

> 命令 ： `SET PASSWORD FOR 'username'@'host' = PASSWORD('newpassword');` 如果是要修改当前登录用户的密码，用`SET PASSWORD = PASSWORD('newpassword');`

例如 ： 

```sql
SET PASSWORD FOR 'pig'@'%' = PASSWORD('123456');

select password('123456');
```

## 撤销用户权限

## 删除用户




























