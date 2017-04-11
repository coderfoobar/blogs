
# MySQL 存储过程

> Create Time : 2017年4月11日 

> 来源于刘道成老师的MySQL教学视频

## 概述

概念类似于 函数，就是把一段代码封装起来，当要执行一段代码的时候，可以通过调用该存储过程来实现。

在封装的语句体里，可以用if/else , case ,while 等控制结构，可以进行sql编程。

> 基础表g:

| id | name | num |
| -- | ---- | --- |
| 1  | 猪   | 21  |
| 2  | 牛   | 7   |
| 3  | 羊   | 13  |
| 4  | 狗   | 14  |
| 5  | 猫   | 55  |


## 操作

* 查看现有存储过程

```sql
show procedure status
# 或者
show procedure status \G
```

* 删除存储过程

```sql
drop procedure 存错过程名 ;
```

* 创建存储过程

```sql
# 修改分隔符
delimiter $
# 创建存储过程p1
create procedure p1()
begin
    select * from g;
end$
```

* 调用存储过程

```sql
# call 存储过程名()$
# 以 $ 结尾，因为上边创建存储过程p1 之前，已经通过delimiter $，将分隔符修改为$
call p1()$ 
```

* 传参

```sql
create procedure p2(n int)
begin
    select * from g where num > n;
end$
```

```sql
call p2(10)$
```

结果: 

| id | name | num |
| -- | ---- | --- |
| 1  | 猪   | 21  |
| 3  | 羊   | 13  |
| 4  | 狗   | 14  |
| 5  | 猫   | 55  |

* 控制结构 if - else

```sql
create procedure p3(n int, oper char(2))
begin
    if oper = 'gt'
        select * from g where num > n;
    else if oper = 'le'
        select * from g where num <= n;
    end if;
end$
```

```sql
call p2(10,'le')$
```

结果: 

| id | name | num |
| -- | ---- | --- |
| 2  | 牛   | 7   |


* 控制结构 while

计算1 --> n 的和
```sql
create procedure p4(n smallint)
begin
    declare i int;
    declare s int;
    
    set i = 1;
    set s = 0;

    while i <= n d0
        set s = s + i;
        set i = i + 1;
    end while;

    select s;
end$
```

```sql
call p2(10)$
```

结果: 

55

## MySQL 中 存储过程与函数的区别

* 名称不同
* 存储过程没有返回值

