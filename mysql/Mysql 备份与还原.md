
# MySQL 备份与还原

> Create Time : 2017年6月7日 Author : huduku.io

备份：
```
mysqldump -h 10.168.192.36 -ufengkong -pOrAcle2016 anti_money > C:/tmp/anti_money.sql
```

还原：

```
# 登录
> mysql -u root -p

# source sql file path
```