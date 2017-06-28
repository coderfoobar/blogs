
# @import url

> Create Time : 2017年3月25日 Author : huduku.io

## 语法

@import url("URL");
@import "URL";

## 可能的值

sUrl

一个字符串，用于指定引用其他级联样式表的URL。

## 注解

该规则没有默认值。

语法中的分号是必需的。如果省略分号，则不会正确导入样式表并会生成错误消息。url()参数是可选的，因为@import 后面始终具有一个URL。

与link元素类似，@import规则可将外部样式表链接到文档。这有助于网站作者




在使用一些资源文件时，需要对其进行导入，这样才能正常使用，但有时资源文件比较多，一个个写比较麻烦，这样可以用@import url()；这种写法引入。

例如：想要对skins目录下的3个css文件进行引入，那么新建一个skins.css文件，文件中通过@import url()；将skins下的3个css文件引入，这样在外面只要引入一个

@import url () 机制是不同于link的，link是在加载页面前把css加载完毕，而@import url() 则是读取完文件后再加载，所以在网速慢的情况下，会出现一开始没有css样式，闪烁一下出现样式后的页面。

@import 是css2里面的，所以古老的IE5不支持。

当使用javascript控制dom去改变样式的时候，只能使用link标签，因为@import不是dom可以控制的。

link除了能加载css外，还能定义RSS，定义rel连接属性，@import只能加载css。

@import url(xxx.css)有最大次数的限制，IE6最大次数是31次，第32个import及以后的都不能生效。虽然最多只能import 31次，但不会影响css里面的其他规则，如body{}的定义还能正常显示。FireFox没有发现import的最大值。

虽然横向import有最大次数限制，却可以通过垂直import来继续扩展。





