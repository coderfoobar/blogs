
# 通过sed命令截取某段时间内的log

> Create Time : 2018年4月8日 Author : huduku.io


```

sed -n '/2018-03-26 15:08/,/2018-03-26 15:11/'p  test.log > tmp.log

```