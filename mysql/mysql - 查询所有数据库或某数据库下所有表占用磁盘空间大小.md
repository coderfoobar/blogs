
# mysql - 查询所有数据库或某数据库下所有表占用磁盘空间大小

* 查询所有数据库下所有表占用磁盘空间大小

```sql
select TABLE_SCHEMA, 
       concat(truncate(sum(data_length)/1024/1024,2),' MB') as data_size,
       concat(truncate(sum(index_length)/1024/1024,2),'MB') as index_size
from information_schema.tables
group by TABLE_SCHEMA
order by data_length desc;
```

* 查询某数据库`Test`下所有表占用磁盘空间大小

```sql

select TABLE_NAME, 
       concat(truncate(data_length/1024/1024,2),' MB') as data_size,
       concat(truncate(index_length/1024/1024,2),' MB') as index_size
from information_schema.tables 
where TABLE_SCHEMA = 'Test'
group by TABLE_NAME
order by data_length desc;
```