
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

---
---


## .class

### 概述
根绝给定的class类名匹配元素。
### 参数
class String V1.0

一个用于以搜索的类。一个元素可以有多个类，只要有一个符合就能被匹配到。
### 返回值
```
Array<Element(s)>
```
### 示例
* 描述 - 查找所有类是“myClass”的元素。

HTML代码
```html
<div class="notMe">div class="notMe"</div>
<div class="myClass">div class="myClass"</div>
<span class="myClass">span class="myClass"</span>
```

jQuery代码
```jQuery
$(".myClass")
```

结果
```
[<div class="myClass">div class="myClass"</div>,<span class="myClass">span class="myClass"</span>]
```

---
---

## *

### 概述

匹配所有元素。多用于结合上下文来搜索。

### 返回值

```
Array<Element(s)>
```

### 示例

* 描述 - 找到每一个元素

HTML代码
```html
<div>DIV</div>
<span>SPAN</span>
<p>P</p>
```

jQuery代码
```jQuery
$("*")
```

结果
```
[<div>DIV</div>,<span>SPAN</span>,<p>P</p>]
```

---
---

## selector1,selector2,selectorN

### 概述

将每一个选择器匹配到的元素一起返回。

你可以制定任意多个选择器，并将匹配到的元素合并到一个结果内。

### 参数

selector1 Selector 一个有效的选择器
selector2 Selector 另一个有效的选择器
selectorN Selector 任意多个有效的选择器

### 返回值

```
Array<Element(s)>
```

### 示例

* 描述 - 找到匹配任意一个选择器的元素

HTML代码
```html
<div>div</div>
<p class="myClass">p class="myClass"</p>
<span>span</span>
<p lcass="notMyClass">p class="notMyClass"</p>
```

jQuery代码
```jQuery
$("div,span,p.myCass")
```

结果
```
[<div>div</div>,<p class="myClass">p class="myClass"</p>,<span>span</span>]
```