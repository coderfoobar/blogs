
# MySQL 添加注释

> Create Time : 2017年4月11日 

```sql
ALTER TABLE table_name COMMENT='这是表的注释';  
```

```sql
ALTER table table_name MODIFY `column_name` datetime DEFAULT NULL COMMENT '这是字段的注释' ;
```