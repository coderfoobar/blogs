
# git ignore 

> Create Time : 2017年9月5日 Ref : http://blog.csdn.net/benkaoya/article/details/7932370

1. 在git项目的根目录下，创建.gitignore文件
2. 配置内容以忽略特定的文件或者目录

---

配置方式：

1. 忽略后缀名为iml的文件 

```
*.iml
```

2. 忽略*.b和*.B文件，my.b除外 

```
*.[bB]
!my.b 
```

3.  忽略dbg文件和dbg目录

```
dbg
```


4.  只忽略dbg目录，不忽略dbg文件

```
dbg/
```

5. 只忽略dbg文件，不忽略dbg目录

```
dbg
!dbg/
```

6. 只忽略当前目录下的dbg文件和目录，子目录的dbg不在忽略范围内

```
/dbg
```