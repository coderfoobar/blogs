
# Flex 布局

## Flex布局是什么？

Flex是Flexible box的缩写，意为“弹性布局”，用来为盒状模型提供最大的灵活性。

任何一个容器都可指定为Flex布局。

```css
.box{
    display : flex;
}

.in-box{
    display : inline-flex;
}

webkit-box{
    display : -webkit-flex;/*safari*/
    display : flex;
}

```

注意，设为Flex布局之后，子元素的`float,clear`和`vertical-align`属性将会失效。

## 基本概念

采用Flex布局的元素，称为Flex容器（`flex container`），简称容器。它的所有子元素自动称为容器成员，称为Flex项目（`flex item`），简称项目。

容器默认存在两根轴：水平的主轴（`main axis`）和垂直的交叉轴（`cross axis`）。主轴的开始位置（与边框的交叉点）焦作`main start`,结束位置叫做`main end`;交叉轴的开始位置叫做`cross start`,结束位置叫做`cross end`。

项目默认沿着主轴排列。单个项目占据的主轴空间叫做`main size`，占据的交叉轴空间叫做`cross size`。

## 容器的属性

以下六个属性设置在容器上：
* flex-direction
* flex-wrap
* flex-wrap
* justify-content
* align-items
* align-content

### flex-direction

决定着主轴的方向（即项目的排列方式）
```css
.box{
    flex-direction : row | row-reverse | column | column-reverse;
}
```
它可能有4个值：
* `row` - （默认值）主轴为水平方向，起点在左端
* `row-reverse` - 主轴为水平方向，起点在右端
* `column` - 主轴为垂直方向，起点在上沿
* `column-reverse` - 主轴在垂直方向，起点在下沿

### flex-wrap

默认情况下，项目都排列在一条线上（又称为“轴线”）上，`flex-wrap`属性定义，如果一条轴线排列不下，如何换行。
```css
.box{
    flex-wrap:nowrap | wrap | wrap-reverse;
}
```
它可能有三个取值：
* `nowrap` - （默认值）不换行
* `wrap` - 换行，第一行在上方
* `wrap-reverse` - 换行，第一行在下方

### flex-flow

`flex-flow`属性是`flex-direction`属性和`flex-wrap`属性的简写形式，默认值为`row nowrap`。
```css
.box{
    flex-flow : <flex-direction> || <flex-wrap>;
}
```
### justify-content
`justify-content` 属性定义了项目在主轴上的对齐方式。
```css
.box{
    justify-content : flex-start | flex-end | center | space-between | space-around;
}
```
它有五个可能的取值：
* `flex-start` - （默认值）左对齐
* `flex-end` - 右对齐
* `center` - 居中
* `space-between` - 两端对齐
* `space-around` - 每个项目两侧的间隔相等。所以，项目之间的间隔比项目与边框的间隔大一倍。

### align-items属性
`align-items`属性定义项目在交叉轴上如何对齐。
```css
.box{
    align-items : flex-start | flex-end | center | baseline | stretch;
}
```
它有五个可能的取值，具体的对齐方式与交叉轴的方向有关，下面假设交叉轴从上到下。
* `flex-start` - 交叉轴的起点对齐
* `flex-end` - 交叉轴的终点对齐
* `center` - 交叉轴的中点对齐
* `baseline` - 项目点额第一行文字的基线对齐
* `stretch` - （默认值） 如果项目未设置高度或者设置为auto，将占满整个容器的高度。


### align-content
`align-content`属性定义了多根轴线的对齐方式。如果项目只有一根轴线，该属性不起作用。
```css
.box{
    align-content : flex-start | flex-end | center | space-between | space-around | stretch;
}
```
该属性可能取六个值：
* `flex-start` - 与交叉轴的起点对齐
* `flex-end` - 与交叉轴的终点对齐
* `center` - 与交叉轴的中点对齐
* `space-between` - 与交叉轴两端对齐，轴线之间的间隔平均分布
* `space-around` - 没跟轴线两侧的间隔相等。所以，轴线之间的间隔比轴线与边框的间隔大一倍。
* `stretch` - （默认值）轴线占满整个交叉轴 


## 项目的属性

以下六个属性设置在项目上：
* `order`
* `flex-grow`
* `flex-shrink`
* `flex-basis`
* `flex`
* `align-self`


### order属性

`order`属性定义项目的排列顺序。数值越小，排列越靠前，默认值为0 (数值可以相同)。 
```css
.item{
    order : <integer>;
}
```

### flex-grow属性

`flex-grow` 属性定义项目点额放大比利，默认值为0，即如果存在剩余空间，也不放大。
```css
.item{
    flex-grow ； <number>;/* default 0*/
}
```
如果所有项目的`flex-grow`属性都为1，则他们将等分剩余空间（如果有的话）。如果一个项目的`flex-grow`属性为2，其他项目都为1，则前者占据的剩余空间将比其他项目多一倍。

### flex-shrink属性

`flex-shrink`属性定义了项目的缩小比例，默认为1，即如果空间不足，该项目将缩小。
```css
.item{
    flex-shrink : <number> ;/*default 1*/
}
```
如果所有项目的`flex-shrink`属性都为1，当空间不足时，都将等比例缩小。如果一个项目的`flex-shrink`属性为0，其他项目都为1，则空间不足时，前者不缩小。

负值对该属性无效。

### flex-basis 属性

`flex-basis`属性定义了在分配多余空间之前，项目占据的主轴空间(`main size`)。浏览器根据这个属性，集散主轴是否有多余空间。它的默认值为`auto`，即项目的本来大小。
```css
.item{
    flex-basis : <length> | auto;    /*default auto*/
}
```
它可以设为跟`width`或`height`属性一样的值（比如350px），则项目将占据固定空间。

### flex属性
`flex`属性时`flex-grow`,`flex-shrink`和`flex-basis`的简写，默认值为`0 1 auto`。后两个属性可选。
```css
.item{
    flex : none | [<'flex-grow'><'flex-shrink'>? || <'flex-basis'>]
}
```
该属性值有两个快捷值: `auto` (`1 1 auto`)和`none`(`0 0 auto`)。
建议优先使用这个属性，而不是单独写三个分离的属性，因为浏览器会推算相关值。

### align-self属性
`align-self`属性允许单个项目有与其他项目不一样的对齐方式，可覆盖`align-items`属性，默认值为`auto`，表示继承父元素的`align-items`属性，如果没有父元素，则等同于`stretch`。
```css
.item{
    align-self : auto | flex-start | flex-end | center | baseline | stretch ; 
}
```
该属性可能取六个值，除了`auto`,其他都与`align-items`属性完全一致。


















