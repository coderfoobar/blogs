
# oracle - trunc日期

> Create Time : 2017年9月18日 Ref : https://jingyan.baidu.com/article/c275f6ba2d2347e33c756753.html

```sql
--获取今天
select trunc(sysdate) from dual;
-- 2017-09-18


--获取今年第一天
select trunc(sysdate,'year') from dual;
-- 2017-09-18

--获取本月第一天
select trunc(sysdate,'month') from dual;

--获取本季度第一天
select trunc(sysdate,'q') from dual;

--获取到日（当日的零点零分零秒）
select to_char(trunc(sysdate),'yyyy-MM-dd hh24:mi:ss') from dual;

--获取本周一
select trunc(sysdate,'iw') from dual;

-- ??? 获取离当前时间最近的周四，若当天为周四则返回当天，否则返回上周四
select trunc(sysdate,'ww') from dual;

--获取本周第一天
select trunc(sysdate,'day') from dual; 

--获取到当前小时 (零分零秒)
select trunc(sysdate,'hh24') from dual;  

--获取到当前分 (零秒)
select trunc(sysdate,'mi') from dual;  


```

