
Mybatis jdbcType作用

> Create Time : 2018年1月26日 Ref : http://qdpurple.iteye.com/blog/1890719

1. 传入java对象中某个属性为`java.util.Date`类型时， 如果`jdbcType=DATE`则会只存年月日；如果`jdbcType=TIMESTAMP`则会存入年月日时分秒毫秒

2. 传入java对象中某个属性为`null`时，`jdbcType`不能为空。


```
The JDBC type is only required for nullable columns upon insert, update or delete. This is a JDBC requirement, not a MyBatis one. So even if you were coding JDBC directly, you'd need to specify this type – but only for nullable values.
```