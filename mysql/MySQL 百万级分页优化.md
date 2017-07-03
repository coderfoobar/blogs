
# MySQL 百万级分页优化

> Create Time : 2017年7月3日 Ref : http://www.jb51.net/article/31868.htm

一般刚开始学SQL的时候，会这样写
```sql
SELECT * FROM table ORDER BY id LIMIT 1000, 10; 
```

但在数据达到百万级的时候，这样写会慢死 
```sql
SELECT * FROM table ORDER BY id LIMIT 1000000, 10; 
```
也许耗费几十秒 

网上很多优化的方法是这样的 
```sql
SELECT * FROM table WHERE id >= (SELECT id FROM table LIMIT 1000000, 1) LIMIT 10; 
```
是的，速度提升到0.x秒了，看样子还行了 

可是，还不是完美的！ 

以下这句才是完美的！ 

```sql
SELECT * FROM table WHERE id BETWEEN 1000000 AND 1000010; 
```

比上面那句，还要再快5至10倍 

另外，如果需要查询 id 不是连续的一段，最佳的方法就是先找出 id ，然后用 in 查询 

```sql
SELECT * FROM table WHERE id IN(10000, 100000, 1000000...); 
```


再分享一点 :

查询字段一较长字符串的时候，表设计时要为该字段多加一个字段,如，存储网址的字段 

查询的时候，不要直接查询字符串，效率低下，应该查诡该字串的crc32或md5 


有一次设计mysql索引的时候，无意中发现索引名字可以任取，可以选择几个字段进来，这有什么用呢？开始的`select id from collect order by id limit 90000,10`; 这么快就是因为走了索引，可是如果加了where 就不走索引了。抱着试试看的想法加了 search(vtype,id) 这样的索引。然后测试`select id from collect where vtype=1 limit 90000,10;` 非常快！0.04秒完成！ 

如果对于有`where`条件，又想走索引用`limit`的，必须设计一个索引，将`where`放第一位，`limit`用到的主键放第2位，而且只能`select`主键！ 

