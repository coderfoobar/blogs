
# MySQL - show profiles 命令

> Create Time : 2017年4月13日 Author : huduku.io

> `show profiles` 是` MySQL 5.0.37`之后添加的。作用是`查看SQL性能指标`,`分析SQL性能瓶颈`。

## 查看数据库版本:

```sql
mysql> select version();
#----------------
+-----------+
| version() |
+-----------+
| 5.7.17    |
+-----------+
1 row in set (0.00 sec)
#----------------
```

## 查看状态

```sql
SELECT @@profiling;  
```

## 性能跟踪诊断

```sql
mysql> show profiles;
#----------------------------
Empty set (0.00 sec)
#----------------------------
```

上边的结果说明profiles功能默认是关闭的。


### 开启

```sql
mysql> set profiling = 1;
#----------------------------
Query OK, 0 rows affected (0.00 sec)
#----------------------------
```

### 查询测试

```sql
mysql> select hashCode , count(1) , indexCode 
     > from ( select hashCode,indexCode from tbl_line_slice_distinct_task group by hashCode,hashId,indexCode) A 
     > group by A.hashCode,indexCode;

#------------------------
#.... 查询结果
#------------------------
```

### 显示所有性能信息

```sql
mysql> show profiles;
#------------------------
+----------+------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| Query_ID | Duration   | Query                                                                                                                                                                         |
+----------+------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
|        1 | 0.00064925 | select hashCode , count(1) , indexCode from ( select hashCode,indexCode from tbl_line_slice_distinct_task group by hashCode,hashId,indexCode) A group by A.hashCode,indexCode |
|        2 | 0.00010875 | shor profiles                                                                                                                                                                 |
+----------+------------+-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
2 rows in set, 1 warning (0.00 sec)
#------------------------
```

### 单独显示query_id=1的性能信息

```sql

# 单独查看query 1的详细信息
mysql> show profile for query 1;
#-----------------------------------------------------
+----------------------+----------+
| Status               | Duration |
+----------------------+----------+
| starting             | 0.000112 |
| checking permissions | 0.000010 |
| Opening tables       | 0.000031 |
| init                 | 0.000065 |
| System lock          | 0.000010 |
| optimizing           | 0.000005 |
| optimizing           | 0.000004 |
| statistics           | 0.000017 |
| preparing            | 0.000013 |
| Creating tmp table   | 0.000021 |
| Sorting result       | 0.000005 |
| statistics           | 0.000007 |
| preparing            | 0.000006 |
| Creating tmp table   | 0.000016 |
| Sorting result       | 0.000004 |
| executing            | 0.000020 |
| Sending data         | 0.000010 |
| executing            | 0.000003 |
| Sending data         | 0.000079 |
| Creating sort index  | 0.000069 |
| Creating sort index  | 0.000024 |
| end                  | 0.000004 |
| query end            | 0.000007 |
| removing tmp table   | 0.000006 |
| query end            | 0.000005 |
| removing tmp table   | 0.000004 |
| query end            | 0.000002 |
| closing tables       | 0.000003 |
| removing tmp table   | 0.000004 |
| closing tables       | 0.000007 |
| freeing items        | 0.000064 |
| cleaning up          | 0.000015 |
+----------------------+----------+
32 rows in set, 1 warning (0.00 sec)
#-----------------------------------------------------

```

### 单独查看query_id=1的CPU占用，IO等性能指标信息

```sql
mysql>  show profile block io,cpu for query 1;
#---------------------------------------------------------------------------------------------------------
+----------------------+----------+----------+------------+--------------+---------------+
| Status               | Duration | CPU_user | CPU_system | Block_ops_in | Block_ops_out |
+----------------------+----------+----------+------------+--------------+---------------+
| starting             | 0.000112 | 0.000000 |   0.000000 |            0 |             0 |
| checking permissions | 0.000010 | 0.000000 |   0.000000 |            0 |             0 |
| Opening tables       | 0.000031 | 0.000000 |   0.000000 |            0 |             0 |
| init                 | 0.000065 | 0.000000 |   0.000000 |            0 |             0 |
| System lock          | 0.000010 | 0.000000 |   0.000000 |            0 |             0 |
| optimizing           | 0.000005 | 0.000000 |   0.000000 |            0 |             0 |
| optimizing           | 0.000004 | 0.000000 |   0.000000 |            0 |             0 |
| statistics           | 0.000017 | 0.000000 |   0.000000 |            0 |             0 |
| preparing            | 0.000013 | 0.000000 |   0.000000 |            0 |             0 |
| Creating tmp table   | 0.000021 | 0.000000 |   0.000000 |            0 |             0 |
| Sorting result       | 0.000005 | 0.000000 |   0.000000 |            0 |             0 |
| statistics           | 0.000007 | 0.000000 |   0.000000 |            0 |             0 |
| preparing            | 0.000006 | 0.000000 |   0.000000 |            0 |             0 |
| Creating tmp table   | 0.000016 | 0.000000 |   0.000000 |            0 |             0 |
| Sorting result       | 0.000004 | 0.000000 |   0.000000 |            0 |             0 |
| executing            | 0.000020 | 0.000000 |   0.000000 |            0 |             0 |
| Sending data         | 0.000010 | 0.000000 |   0.000000 |            0 |             0 |
| executing            | 0.000003 | 0.000000 |   0.000000 |            0 |             0 |
| Sending data         | 0.000079 | 0.000000 |   0.000000 |            0 |             0 |
| Creating sort index  | 0.000069 | 0.000000 |   0.000000 |            0 |             0 |
| Creating sort index  | 0.000024 | 0.000000 |   0.000000 |            0 |             0 |
| end                  | 0.000004 | 0.000000 |   0.000000 |            0 |             0 |
| query end            | 0.000007 | 0.000000 |   0.000000 |            0 |             0 |
| removing tmp table   | 0.000006 | 0.000000 |   0.000000 |            0 |             0 |
| query end            | 0.000005 | 0.000000 |   0.000000 |            0 |             0 |
| removing tmp table   | 0.000004 | 0.000000 |   0.000000 |            0 |             0 |
| query end            | 0.000002 | 0.000000 |   0.000000 |            0 |             0 |
| closing tables       | 0.000003 | 0.000000 |   0.000000 |            0 |             0 |
| removing tmp table   | 0.000004 | 0.000000 |   0.000000 |            0 |             0 |
| closing tables       | 0.000007 | 0.000000 |   0.000000 |            0 |             0 |
| freeing items        | 0.000064 | 0.000000 |   0.000000 |            0 |             0 |
| cleaning up          | 0.000015 | 0.000000 |   0.000000 |            0 |             0 |
+----------------------+----------+----------+------------+--------------+---------------+
32 rows in set, 1 warning (0.02 sec)
#---------------------------------------------------------------------------------------------------------
```

## 语法规则

```
SHOW PROFILE [type [, type] … ]  
    [FOR QUERY n]  
    [LIMIT row_count [OFFSET offset]]  
  
type:  
    ALL  
  | BLOCK IO  
  | CONTEXT SWITCHES  
  | CPU  
  | IPC  
  | MEMORY  
  | PAGE FAULTS  
  | SOURCE  
  | SWAPS  
```