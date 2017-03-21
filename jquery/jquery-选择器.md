
# jQuery 选择器

> Create Time : 2017年3月20日 Ref : http://jquery.cuishifeng.cn

## #id

### 概述 
根据给定的ID匹配一个元素。使用任何的元字符（如!"#$%&'()+,./:;<=>`{|}~）作为名称的文本部分，他必须被两个反斜线转移:`\\`。参见示例。
### 参数 

id  String V1.0 
用于搜索的通过元素的id属性中给定的值。

### 返回值
```
Array<Element>
```
### 示例
---
* 描述 - 查找ID为 "myDiv"的元素。

HTML代码
```html
<div id="notMe"><p>id="notMe"</p></div>
<div id="myDiv">id="myDiv"</div>
```
jQuery 代码
```jquery
$("#myDiv")
```
结果
```
[<div id="myDiv">id="myDiv"</div>]
```
---

* 描述 - 查找含有特殊字符的元素

HTML代码
```html
<span id="foo:bar"></span>
<span id="foo[bar]"></span>
<span id="foo.bar"></span>
```
jQuery 代码
```jquery
$("#foo\\[bar\\]");
```
结果
```
[<span id="foo[bar]"></span>]
```

---
---

## element

### 概述
根据给定的元素标签名称匹配所有元素
### 参数
element String V1.0

一个用于搜索的元素。只想DOM系欸但的标签名。
### 返回值
```
Array<Element(s)>
```
### 示例
* 描述 - 查找一个DIV元素。

HTML代码
```html
<div>DIV1</div>
<div>DIV2</div>
<span>SPAN</span>
```

jQuery代码
```jQuery
$("div")
```

结果
```
[<div>DIV1</div>,<div>DIV2</div>]
```