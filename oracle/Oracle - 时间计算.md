
# Oracle - 时间计算

> Create Time : 2018年6月4日14:41:04 Ref : https://blog.csdn.net/redarmy_chen/article/details/7351410

1. 计算相差了几年

```sql
select EXTRACT(year FROM to_date('2009-05-01','yyyy-mm-dd')) - EXTRACT(year FROM to_date('2008-04-30','yyyy-mm-dd')) years from dual; 
```

2. 计算相差了几个月

```sql
select months_between(sysdate , to_date('2017-07-01','yyyy-MM-dd')) + 1 from dual
```

或者:

```sql
select (EXTRACT(year FROM to_date('2009-05-01','yyyy-mm-dd')) - EXTRACT(year FROM to_date('2008-04-30','yyyy-mm-dd'))) * 12 + 
       EXTRACT(month FROM to_date('2008-05-01','yyyy-mm-dd')) - EXTRACT(month FROM to_date('2008-04-30','yyyy-mm-dd')) months 
from dual; 
```

3. 计算相差了几天

```sql
select trunc(to_date('2009-05-01','yyyy-mm-dd')) - trunc(to_date('2008-04-30','yyyy-mm-dd')) from dual; 
```


4. 计算相差的小时数

```sql
select ceil((To_date('2008-05-02 00:00:00' , 'yyyy-mm-dd hh24-mi-ss') - To_date('2008-04-30 23:59:59' , 'yyyy-mm-dd hh24-mi-ss')) * 24)  hour_minus FROM DUAL; 
```


5. 相差分钟数

```sql
select ceil(((To_date('2008-05-02 00:00:00' , 'yyyy-mm-dd hh24-mi-ss') - To_date('2008-04-30 23:59:59' , 'yyyy-mm-dd hh24-mi-ss'))) * 24 * 60)  min_minus FROM DUAL; 
```

6. 计算相差秒数

```sql
select ceil((To_date('2008-05-02 00:00:00' , 'yyyy-mm-dd hh24-mi-ss') - To_date('2008-04-30 23:59:59' , 'yyyy-mm-dd hh24-mi-ss')) * 24 * 60 * 60) sec_minus FROM DUAL; 
```

7. 计算相差的毫秒数

```sql
select ceil((To_date('2008-05-02 00:00:00' , 'yyyy-mm-dd hh24-mi-ss') - To_date('2008-04-30 23:59:59' , 'yyyy-mm-dd hh24-mi-ss')) * 24 * 60 * 60 * 1000) millisec_minus FROM DUAL; 
```


8. 时间推算相关

```sql
select sysdate,add_months(sysdate,12) from dual;  --加1年 
select sysdate,add_months(sysdate,1) from dual;   --加1月 
select sysdate,TO_CHAR(sysdate+7,'yyyy-mm-dd HH24:MI:SS') from dual;  --加1星期 
select sysdate,TO_CHAR(sysdate+1,'yyyy-mm-dd HH24:MI:SS') from dual;  --加1天 
select sysdate,TO_CHAR(sysdate+1/24,'yyyy-mm-dd HH24:MI:SS') from dual;  --加1小时 
select sysdate,TO_CHAR(sysdate+1/24/60,'yyyy-mm-dd HH23:MI:SS') from dual;  --加1分钟 
select sysdate,TO_CHAR(sysdate+1/24/60/60,'yyyy-mm-dd HH23:MI:SS') from dual;  --加1秒 
select   sysdate+7   from   dual;                     --加7天
```



