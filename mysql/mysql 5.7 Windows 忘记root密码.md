
# mysql 5.7 Windows 忘记root密码

> Create Time : 2017年4月6日   Ref : 

1. 打开一个`DOS窗口A`，关闭mysql服务，之后执行命令，用于跳过权限表验证

```bat
> net stop mysql
MySQL 服务正在停止.
MySQL 服务已成功停止。

> mysqld --skip-grant-tables 

# --skip-grant-tables的意思是启动mysql服务的时候跳过权限表验证
# 此处DOS窗口会定住，不能动

```

2. 再打开一个`DOS窗口B`,直接输入 `mysql` ，进入mysql数据库

```sql
> mysql

# ......
# 使用mysql数据库
mysql> use mysql;
# 更新root密码
mysql> update user set authentication_string=password('123456') where user='root';
# 刷新权限
mysql> flush privileges;
mysql> exit;
```

3. 因为`DOS窗口A`卡住，即便关掉`DOS窗口A`，也可以不通过输入密码就进入数据库，所以此步`重启机器`

机器重启完成后，再打开DOS窗口:
```
> mysql -u root -p
Enter Password:
# 此处输入刚才设置的密码`123456`即可进入数据库