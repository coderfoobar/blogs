
# MySQL锁的用法之表级锁

> Create Time : 2017年7月3日 Ref : http://www.linuxidc.com/Linux/2012-09/69760.htm

锁机制是数据库有别于文件系统的一个重要的特点，也是用来管理并发访问的一个有效的方式。MySQL的锁分为表级锁、页级锁与行级锁。表级锁是MySQL中粒度最大的一种锁，它实现简单，资源消耗较少，被大部分MySQL引擎支持。最常使用的MYISAM与INNODB都支持表级锁定。


表级锁定分为两类，读锁与写锁。读锁是预期将对数据表进行读取操作，锁定期间保证表不能被修改。写锁是预期会对数据表更新操作，锁定期间保证表不能被其他线程更新或读取。

## 读锁

> 用法： `LOCK TABLE table_name [ AS alias_name ] READ`

> 说明： 指定数据表，LOCK类型为READ即可，AS别名是可选参数，如果指定别名，使用时也要指定别名才可

> 申请读锁前提：当前没有线程对该数据表使用写锁，否则申请会阻塞。

> 操作限制：其他线程可以对锁定表使用读锁；其他线程不可以对锁定表使用写锁

| | 写操作 | 读操作 |
| --- | --- | --- |
| 使用读锁线程 |	否（报错） |	能 |
| 不使用读锁线程 |	否（阻塞） |	能 | 

对于使用读锁的MySQL线程，由于读锁不允许任何线程对锁定表进行修改，在释放锁资源前，该线程对表操作只能进行读操作，写操作时会提示非法操作。而对于其他没使用锁的MySQL线程，对锁定表进行读操作可以正常进行，但是进行写操作时，线程会等待读锁的释放，当锁定表的所有读锁都释放时，线程才会响应写操作。

## 写锁

> 用法： `LOCK TABLE table_name [AS alias_name] [ LOW_PRIORITY ] WRITE`

> 说明： 别名用法与读锁一样，写锁增加了指定优先级的功能，加入LOW_PRIORITY可以指定写锁为低优先级。

> 申请写锁前提： 当没有线程对该数据表使用写锁与读锁，否则申请回阻塞。

> 操作限制：其他MySQL线程不可以对锁表使用写锁、读锁

| | 写操作 | 读操作 |
| --- | --- | --- |
| 使用写锁线程 |	能 |	能 |
| 不使用写锁线程 |	否（阻塞） |	能（阻塞） | 

 对于使用写锁的MySQL线程，其可以对锁定表进行读写操作。但是对于其他线程，对指定表读写操作都是非法的，需要等待直到写锁释放。

## 锁分配优先级

> 对于锁分配的优先级，是： `LOW_PRIORITY WRITE < READ < WRITE`

1. 当多个线程申请锁，会优先分配给WRITE锁，不存在WRITE锁时，才分配READ锁，LOW_PRIORITY WRITE需要等到WRITE锁与READ都释放后，才有机会分配到资源。
2. 对于相同优先级的锁申请，分配原则为谁先申请，谁先分配。

## 注意事项

1. 不能操作（查询或更新）没有被锁定的表。

 例如当只申请table1的读锁，SQL语句中包含对table2的操作是非法的。例如：

```sql
mysql> LOCK TABLE test READ;  
Query OK, 0 rows affected (0.00 sec)  
  
mysql> SELECT * FROM test_myisam;  
ERROR 1100 (HY000): Table 'test_myisam' was not locked with LOCK TABLES  
```

2. 不能在一个SQL中使用两次表（除非使用别名） 

当SQL语句中多次使用一张表时，系统会报错。例如：

```sql
mysql> LOCK TABLE test READ;  
Query OK, 0 rows affected (0.00 sec)  
  
mysql> SELECT * FROM test WHERE id IN (SELECT id FROM test );  
ERROR 1100 (HY000): Table 'test' was not locked with LOCK TABLES  
```

解决这个问题的方法是使用别名，如果多次使用到一个表，需要声明多个别名。

```sql
mysql> LOCK TABLE test AS t1 READ, test AS t2 READ;  
Query OK, 0 rows affected (0.00 sec)  
  
mysql>  SELECT * FROM test AS t1 WHERE id IN (SELECT id FROM test AS t2);  
+----+-----------+   
| id | content   |  
+----+-----------+   
|  1 | tt_1      |  
|  3 | new_3     |  
|  4 | new_4     |  
|  5 | content_5 |  
+----+-----------+   
4 rows in set (0.00 sec)  
```

3.  申请锁时使用别名，使用锁定表时必须加上别名。
