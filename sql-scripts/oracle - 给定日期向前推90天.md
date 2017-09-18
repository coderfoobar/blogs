
# Oracle -  给定日期，向前推90天

> Create Time : 2017年9月18日   Ref : 

```sql
select to_char(date'2017-09-18' -rownum + 1,'yyyy-MM-dd') create_time 
from  dual connect by rownum <= 90
```