

# Java - 普通for循环和foreach的null值判断

> Create Time : 2017年4月21日 Author ： huduku.io

在Java中普通for循环和foreach是需要进行null值判断的，否则会爆出`NullPointerException`异常：

```Java
Exception in thread "main" java.lang.NullPointerException
```