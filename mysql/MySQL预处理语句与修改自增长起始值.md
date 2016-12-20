
# MySQL预处理语句与修改自增长起始值

> 创建时间 : 2016年12月19日    最后更新时间 : 2016年12月19日

利用重定向管道符 `<` 执行sql脚本:

root@centos-1 $ mysql -uroot -p123456 < ttt.sql

ttt.sql 文件内容:

```sql
use db_name;

set @var=0;
select max(id) + 1 from table_name into @var;
set @aisql=concat('alter table table_name auto_increment=',@var);

prepare pstmt from @aisql;
execute pstmt;

```