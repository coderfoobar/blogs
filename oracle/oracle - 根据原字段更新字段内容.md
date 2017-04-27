
# Oracle - 根据原字段内容更新字段内容

> Create Time : 2017年4月27日  Author : huduku.io

比如：现在表`tbl`中有字段`field`,`varchar2(64)`型字段`field`中的值都具有特定的形式：开头、中间或者末尾带有`#`;比如: `#123#3345#sdf#`。

现在想要将其中的`#`去除。方法是：

---

# 正确的方法

1. 首先在表`tbl`中新建中间字段`field_tmp` :

```sql
alter table tbl add field_tmp varchar2(64);
```

2. 先将去除`#`后的值存在中间字段`field`：

```sql
update tbl set field_tmp = replace(field , '#' , '');
```

3. 再将中间字段`field_tmp`的值赋给原始字段`field`:

```sql
update tbl set field = field_tmp;
```

4. 删除中间字段`field_tmp`:

```sql
alter table tbl drop column field_tmp;
```

---

# 错误的方法

不通过中间字段，直接更新原始字段的值：

```sql
update tbl set field = replace(field , '#' , '');
```








