

# Linux - grep 命令

> Create Time : 2017年3月10日 Author : huduku.io  Last Update Time : 2017年3月10日
```
grep -n exception app.log | head -1
#显示出第一次出现exception时的行及行号
grep -n exception app.log | head -1
#显示出最后一次出现exception时的行及行号
```

```
grep -C 10 exception app.log
exception 出现行前后10行内容
grep -A 10
#exception 出现行前10行
grep -B 10
#exception 出现行后10行
```


