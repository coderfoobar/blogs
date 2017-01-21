

# Java - 环境变量

> Create Time : 2017年1月3日

## 配置方法

> 在 Win10 中，配置JDK环境变量，不要使用反斜线 "\", 使用正斜线 "/"。

> 在 linux 中，配置PATH与CLASSPATH，要用`:`作为路径分割符，不要使用`;`，否则在`source /etc/profile`使环境变量生效时，会报错，。

```Java
JAVA_HOME
C:/Program Files/Java/jdk1.8.0_111

PATH： 
%JAVA_HOME%/bin;
%JAVA_HOME%/jre/bin

CLASSPATH
.;%JAVA_HOME%/lib/dt.jar;%JAVA_HOME%/lib/tools.jar; 
```

## 环境变量的意义

> 想要理解环境变量的意义，就必须要理解什么是`PATH`与`CLASSPATH`。

1. 什么是`PATH`?

按`Windows徽标+R`快捷键，输入`calc`,会打开`计算器`；输入`notepad`，会打开`记事本`。
原因是`计算器`与`记事本`两个程序均在目录`C:/Windows/System32`下（`Win10`下的`计算器`会略有不同）。如
```bat
C:/Windows/System32/notepad.exe
```
而目录`C:/Windows/System32`已经配置在`PATH`中。

> 对于在`PATH`中所配置目录下的可执行程序，在`Windows徽标+R`中输入相应的程序名即可执行。或者在`cmd`命令行中，`cd`到任意目录，输入程序名，即可执行。

> 对于没有配置`PATH`环境变量的程序，只有输入程序的`绝对路径`(路径名+程序名),才能执行。


2. 什么是`CLASSPATH`?





