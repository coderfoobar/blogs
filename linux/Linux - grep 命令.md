

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



* 介绍

Linux系统中grep命令是一种强大的文本搜索工具，它能使用正则表达式搜索文本，并把匹配到的行打印出来。grep全称是Global Regular Expression Print,表示全局正则表达式打印，它的使用权限是所有用户。

* 用法

grep [options] pattern 文件名

* 参数

[options] 主要参数：

    1. `-c` - 只输出匹配行的计数
    2. `-I` - 不区分大小写（只适用于单字符）
    3. `-h` - 查询多文件时不现实文件名
    4. `-l` - 查询多文件时只输出包含匹配字符的文件名
    5. `-n` - 显示匹配行及行号
    6. `-s` - 不显示不存在或无匹配文本的错误信息
    7. `-v` - 显示不包含匹配文本的所有行

pattern 中元字符含义：

    1. `\` - 转义字符。忽略正则表达式中特殊字符的原有含义
    2. `^` - 匹配正则表达式行的开始
    3. `$` - 匹配正则表达式行的结束
    4. `\<` - 从匹配正则表达式的行开始
       `\>` - 到匹配正则表达式的行结束
    5. `[ ]` - 单个字符，如[bA]即b或A符合要求
    6. `[ - ]` - 单个字符范围，如[a-cA-D]即a,b,c,A,B,C,D都符合要求
    7. `.` - 匹配所有字符
    8. `*` - 0个或多个
    9. `+` - 1个或多个
    10. `?` - 0或1个



* 功能

* 安装

* 应用及示例

* 区别



