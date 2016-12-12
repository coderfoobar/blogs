
# html背景图片自适应屏幕 

```css
body{
    background: url('${pageContext.request.contextPath }/common/img/bg.jpg') no-repeat center;  
    background-size: 100% 100% ;  
    background-attachment:fixed
}
```

***`其中 :`***

* `url` - 用来指定图片绝对路径地址
* `no-repeat` - 不重复填充图片，对应`css`属性名字为`background-size`,其可取值有：
    * `repeat` - 默认。图像将在垂直方向和水平方向重复。
    * `repeat-x` - 背景图像将在水平方向重复。
    * `repeat-y` - 背景图像将在垂直方向重复。
    * `no-repeat` - 背景图像仅显示一次。
    * `inherit` - 规定应该从父元素继承`background-repeat`。
* `center` - 图片位置,对应的`css`属性名字为`background-position`,其可取值有：
    * `top left`
    * `top center`
    * `top right`
    * `center left`
    * `center center`
    * `center right`
    * `bottom left`
    * `bottom center`
    * `bottom right`
        * 如果您仅规定了一个关键词，那么第二个值将是`center`。默认值：`0% 0%`。
    * `x% y%`
        * 第一个值是水平位置，第二个值是垂直位置。左上角是 `0% 0%`。右下角是 `100% 100%`。如果仅规定了一个值，则第二个值是 `50%`。
    * `xpos ypos`
        * 第一个值是水平位置，第二个值是垂直位置。左上角是`0 0` ， 单位是像素，或者任何其他`css`单位。如果仅规定了一个值，则第二个值是 `50%`。可以混用`%`和`position`的值。
* `background-size` - 指定背景图大小,可取值有：
    * *`length`* - 设置图像的宽度和高度。第一个值设置宽度，第二个值设置高度。如果只设置一个值，则第二个值会被设置为`auto` ;
    * *`percentage`* - 以父元素的百分比来设置图片的宽度和高度。第一个值设置宽度，第二个值设置高度。如果只设置一个值，第二个值会被设置为`auto` ;
    * `cover` - 背景图片宽高等比例扩大，使图片完全显示在父元素上。图片的某些部分可能不会显示在屏幕上。
    * `contain` - 背景图片宽高等比例扩大，使整个片完全显示在父元素上，父元素可能会有部分不被背景图片覆盖。
* `background-attachment` - 可取值有：
    * `scroll` - 默认值。当网页超过一屏时，背景图片会随着网页其余部分的滚动而移动。
    * `fixed` - 当网页超过一屏时，背景图片不会随着网页其余部分的滚动而移动。
    * `inherit` - 规定应该从父元素继承 `background-attachment` 属性的设置。