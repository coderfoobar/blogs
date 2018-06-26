
# Oracle - 取余函数

> Create Time : 2018年6月26日16:14:27   Author : huduku.io 

```sql
select mod(5,2) from dual;
```

对于保留两位小数的数字23.33，查询是否有小数位：

```sql
select 'contains float nums' from dual
where mod(23.33*100 , 100) > 0;
```