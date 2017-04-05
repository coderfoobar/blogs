
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

---
---

## ancestor descendant

### 概述

在给定的祖先元素下匹配所有的后代元素。

### 参数

ancestor Selector 任何有效的选择器
descendant Selector 用以匹配元素的选择器，并且他是第一个选择器的后代元素。

### 返回值

```
Array<Element(s)>
```

### 示例

* 描述 - 找到表单中所有的input元素。

HTML代码
```html
<form>
    <label>Name:</label>
    <input name="name" />
    <fieldSet>
        <label>Newsletter:</label>
        <input name="newsletter" />
    </fieldSet>
</form>
<input name="none" />
```

jQuery代码
```jQuery
$("form input")
```

结果
```
[<input name="name" />,<input name="newsletter" />]
```

---
---

## parent > child

### 概述

在给定父元素下匹配所有的子元素。

### 参数

parent Selector 任何有效的选择器
child Selector 用以匹配元素的选择器，并且她是第一个选择器的子元素。

### 返回值

```
Array<Element(s)>
```

### 示例

* 描述 - 匹配表单中所有子级的input元素。

HTML代码
```html
<form>
    <label>Name:</label>
    <input name="name" />
    <fieldSet>
        <label>Newsletter:</label>
        <input name="newsletter" />
    </fieldSet>
</form>
<input name="none" />
```

jQuery代码
```jQuery
$("form > input")
```

结果
```
[<input name="name" />]
```

---
---

## prev + next

### 概述

匹配所有紧接在prev元素后的next元素。

### 参数

prev Selector 任何有效的选择器
next Selector 一个有效的选择器并且紧接着第一个选择器。

### 返回值

```
Array<Element(s)>
```

### 示例

* 描述 - 匹配所有跟在labe后面的input元素。

HTML代码
```html
<form>
    <label>Name:</label>
    <input name="name" />
    <fieldSet>
        <label>Newsletter:</label>
        <input name="newsletter" />
    </fieldSet>
</form>
<input name="none" />
```

jQuery代码
```jQuery
$("label + input")
```

结果
```
[<input name="name" />,<input name="newsletter" />]
```
---
---

## prev ~ siblings

### 概述

匹配所有紧接在prev元素后的siblings元素。

### 参数

prev Selector 任何有效的选择器
siblings Selector 并且它作为第一个选择器的同辈。

### 返回值

```
Array<Element(s)>
```

### 示例

* 描述 - 找到所有与表单同辈的input元素。

HTML代码
```html
<form>
    <label>Name:</label>
    <input name="name" />
    <fieldSet>
        <label>Newsletter:</label>
        <input name="newsletter" />
    </fieldSet>
</form>
<input name="none" />
```

jQuery代码
```jQuery
$("form ~ input")
```

结果
```
[<input name="none" />]
```

---
---

## :first

### 概述

获取匹配到的第一个元素。

### 参数


### 返回值

```
Array<Element>
```

### 示例

* 描述 - 获取匹配到的第一个元素。

HTML代码
```html
<ul>
    <li>list item 1</li>
    <li>list item 2</li>
    <li>list item 3</li>
    <li>list item 4</li>
    <li>list item 5</li>
</ul>
```

jQuery代码
```jQuery
$("li:first")
```

结果
```
[<li>list item 1</li>]
```

---
---

## :not(selector)

### 概述

去除所有与给定选择器匹配的元素。
在jQuery 1.3中，已经支持复杂选择器了(例如:not(div a)和:not(div a))

### 参数

selector Selector 用于筛选的选择器

### 返回值

```
Array<Element>
```

### 示例

* 描述 - 查找所有未选中的input元素。

HTML代码
```html
<input name="apple" />
<input name="flower" checked="checked" />
```

jQuery代码
```jQuery
$("input:not(:checked)")
```

结果
```
[<input name="apple" />]
```

---
---


## selector:even

### 概述

匹配所有索引值为偶数的元素，从0开始计数

### 参数

selector Selector 用于筛选的选择器

### 返回值

```
Array<Element(s)>
```

### 示例

* 描述 - 查找表格的1、3、5……行（即索引值0、2、4……）

HTML代码
```html
<table>
    <tr><td>Header 1</td></tr>
    <tr><td>Value 1</td></tr>
    <tr><td>Value 2</td></tr>
</table>
```

jQuery代码
```jQuery
$("tr:even")
```

结果
```
[<tr><td>Header 1</td></tr>,<tr><td>Value 2</td></tr>]
```

---
---


## selector:odd

### 概述

匹配所有索引值为奇数的元素，从0开始计数

### 参数

selector Selector 用于筛选的选择器

### 返回值

```
Array<Element(s)>
```

### 示例

* 描述 - 查找表格的2、4、6……行（即索引值1、3、5……）

HTML代码
```html
<table>
    <tr><td>Header 1</td></tr>
    <tr><td>Value 1</td></tr>
    <tr><td>Value 2</td></tr>
</table>
```

jQuery代码
```jQuery
$("tr:odd")
```

结果
```
[<tr><td>Value 1</td></tr>]
```
---
---

## :eq(index)

### 概述

匹配一个给定索引值的元素

### 参数

index Number 从0开始计数

### 返回值

```
Array<Element>
```

### 示例

* 描述 - 查找第二行

HTML代码
```html
<table>
    <tr><td>Header 1</td></tr>
    <tr><td>Value 1</td></tr>
    <tr><td>Value 2</td></tr>
</table>
```

jQuery代码
```jQuery
$("tr:eq(1)")
```

结果
```
[<tr><td>Value 1</td></tr>]
```
---
---

## :gt(index)

### 概述

匹配所有大于给定所有给定索引值的元素

### 参数

index Number 从0开始计数

### 返回值

```
Array<Element(s)>
```

### 示例

* 描述 - 查找第二行,第三行，即索引值是1和2，也就是比0大

HTML代码
```html
<table>
    <tr><td>Header 1</td></tr>
    <tr><td>Value 1</td></tr>
    <tr><td>Value 2</td></tr>
</table>
```

jQuery代码
```jQuery
$("tr:gt(0)")
```

结果
```
[<tr><td>Value 1</td></tr>,<tr><td>Value 2</td></tr>]
```

---
---

## :lang(lang)

### 概述

选择指定语言的所有元素。

:lang选择器，匹配有一个语言值等于所提供的语言代码，或以提供的语言代码开始，后面马上跟一个“-”的元素。例如，选择器$("div:lang(en)")将匹配<div lang="en"> and <div lang="en-us" >(和他们的后代<div>) ，但不包括<div lang="fr">

对于HTML元素，语言值有lang属性决定，也可能由来自meta元素或HTTP头信息决定。

这种用法的进一步讨论可以在W3C CSS规范中找到。

### 参数

language 语言代码

### 返回值

```
jQuery
```

### 示例

* 描述 - 选择所有<p>的语言属性

jQuery代码
```jQuery
$("p:(lang(it))")
```
---
---

## :header

### 概述

匹配如h1，h2，h3之类的标题元素

### 返回值

```
jQuery
```

### 示例

* 描述 - 给页面内所有标题加上背景色

HTML代码
```html
<h1>Header 1</h1>
<p>Contents 1</p>
<h2>Header 2</h2>
<p>Contents 2</p>
```

jQuery代码
```jQuery
$(":header").css("background","#EEE");
```

结果
```
[<h1 style="background:#EEE;">Header 1</h1>,<h2 style="background:#EEE;">Header 2</h2>]
```

---
---

## :animated

### 概述

匹配所有正在执行动画效果的元素

### 返回值

```
Array<Element(s)>
```

### 示例

* 描述 - 只有对不在执行动画效果的元素执行一个动画特效

HTML代码
```html
<button id="run" >Run<button>
<div></div>
```

jQuery代码
```jQuery
$("#run").click(function(){
    $("div:not(:animated)").animate({left:"+=20"},1000);
});
```

---
---

## :focus

### 概述

匹配当前获取焦点的元素

如同其他伪类选择器（那些以“:”开始），建议:focus前面用标记名称或其他选择；否则，通用选择("*")是不言而喻的。换句话说，$(":focus")等同为$("*:focus")。如果你正在寻找当前的焦点元素，$(document.activeElement)将检索，而不必搜索整个DOM树。

### 返回值

```
jQuery
```

### 示例

* 描述 - 添加一个“focused”的类名给那些有focus方法的元素

CSS代码
```css
.focused {
    background : #ABCDEF;
}
```

HTML代码
```html
<div id="content">
    <input tabIndex="1" />
    <input tabIndex="2" />
    <select tabIndex="3">
        <option>select menu</option>
    </select>
    <div tabIndex="4"> 
    </div>
</div>
```

jQuery代码
```jQuery
$("#content").delegate("*","focus blur",function(event){
    var elem = $(this);
    setTimeout(function(){
        elem.toggleClass("focused",elem.is(":focus"));
    },0);
});
```
---
---

## :root

### 概述

选择该文档的根元素。

在HTML中，文档的根元素，和$(":root")选择的元素一样，永远是<html>元素。

### 返回值

```
jQuery
```

### 示例

* 描述 - 设置<html>背景颜色为黄色

jQuery代码
```jQuery
$(":root").css("background-color","yellow");
```
---
---

## :target

### 概述

选择由文档URI的格式化识别码表示的目标元素。

如果文档的URI包含一个格式化的标识符，或hash（哈希），然后:target选择器将匹配ID和标识符相匹配的元素。如，给定的URI http://example.com#foo , $("p:target") ，将选择`<p id="foo" >`元素。

这个不寻常的用法，可进一步讨论中找到W3C CSS specification.

### 返回值

```
jQuery
```
