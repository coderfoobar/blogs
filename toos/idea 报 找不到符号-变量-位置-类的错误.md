
# idea 报 找不到符号-变量-位置-类.md

> Create Time : 2017年11月27日 author: huduku.io

idea突然报类似于下面的异常：

```
找不到符号
    变量:
    位置: 类
```

可是该类是我自己建的实体类，为什么就找不到该字段呢？

具体原因没找到，但是通过一番查看：发现`settings -> Build,Execution,Deployment ->  Compiler -> Java Compiler`
发现`Project bytecode version`里边没有设定版本，下边的`Per-moudle bytecode version` 里每个`maven module`里都使用的是`1.6`，于是将`Project bytecode version`设定为`1.6`。

重新`Build -> Build Project`，通过。
