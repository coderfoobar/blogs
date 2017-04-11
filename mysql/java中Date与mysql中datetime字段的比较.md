

# Java中Date与MySQL中datetime字段的比较

> Create Time : 2017年4月10日 Author : huduku.io


表结构：

```sql
create table user (
    id bigint,
    createTime datetime,
    name varchar(64)
);
```


可选sql语句
```Java
String sql = "select id , createTime , name from user where createTime > '2017-03-05 00:00:00'";
```

```Java
String sql = "select id , createTime , name from user where createTime > str_to_date('2017-03-05 00:00:00' , '%Y-%m-%d %T') ";
```

```Java
String sql = "select id , createTime , name from user where createTime > ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1,'2017-03-05 00:00:00');
ResultSet rs  = ps.executeQuery();
while(rs.next()){
    User user = new User();
    user.setId(rs.getLong("id"));
    user.setCreateTime(rs.getTime("createTime")); // TODO : check this step
    user.setName(rs.getString("name"));
}
```


