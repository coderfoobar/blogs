
# MySQL alter用法

* 1. 删除列
```sql
alter table 【表名字】 drop 【列名字】;
```

* 2. 增加列
```sql
alter table 【表名字】 add 【列名字】 【列类型】 not null comment '注释说明';
```

* 3. 修改列的类型信息
```sql
alter table 【表名字】 change 【列 旧名字】 【列 新名字（此处可以和原来的名字相同）】 varchar(64) not null comment '注释说明';
```

* 4. 重命名列
```sql
alter table 【表名字】 change 【列名字】 【新列名字】 varchar(64) not null comment '注释说明';
```

* 5. 重命名表
```sql
alter table 【表名字】 rename 【新表名】;
```

* 6. 删除表中主键
```sql
alter table 【表名字】 drop primary key ;
```

* 7. 添加主键
```sql
alter table 【表名字】 add constraint pk 【主键名字】 primary key (【列名1】,【列名2】......)
```

* 8. 添加索引
```sql
alter table 【表名字】 add index index_name(【列名】);
```

* 9. 添加唯一限制条件索引
```sql
alter table 【表名字】 add unique index_name(【列名】);
```

* 10. 删除索引
```sql
alter table 【表名字】 drop index index_name;
```

* 11. 更改主键自增起始值
```sql
alter table table_name auto_increment=1000;
```