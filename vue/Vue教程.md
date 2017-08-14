
# Vue教程

> Create Time : 2017年8月14日 Ref : 腾讯课堂 https://ke.qq.com/webcourse/index.html#course_id=180464&term_id=100213223&taid=1130315133403376&vid=w1415c3altq

# 第一章 Vue.js是什么

## 1.1 Vue.js的目的

Vue.js的产生是为了解决如下三个问题：

1. 解决数据绑定的问题；
2. Vue.js框架产生的主要目的是为了开发大型单页面应用（SPA：Single Page Application）
   Angular.js中对PC端支持的比较良好，但是对移动端支持就一般。而Vue.js主要支持移动端，也支持PC端。
3. Vue.js还支持组件化，也就是可以将页面封装成若干个组件，采用积木式编程，这样使页面的复用度达到最高（支持组件化）。

## 1.2 Vue.js特性

1. MVVM模式

M：Model业务模型，用处：处理数据，提供数据

V：用户界面、用户视图

MV：业务模型Model中的数据发生改变的时候，用户视图View也随之变化。VM：用户视图view改变的时候，业务模型Model中的数据也可以发生改变。

2. 组件化
3. 指令系统
4. Vue2.0 开始支持`虚拟DOM`（Vue.js 1.0 是操作的真实DOM，而不是虚拟DOM）。

虚拟DOM可以提升页面的刷新速度。


# 第二章 Vue.js入门

使用Vue的步骤：

1. 在页面中引入vue.js : <script src="vue.js"></script>
2. Vue.js提供了一个Vue，我们需要创建一个对象。 
```JavaScript
<script>
    var app = new Vue({
        el:'#demo', //el表示声明Vue.js管理的边界
        data:{
            name:'Andy'
        } //data核心作用是存放显示在页面中的数据,需要是一个对象
    });
</script>
```
3. 在用户界面view中，通过{{}}形式将data中的数据显示在页面中个。

在用户界面中，{{}}代码中绑定的data的key，而在页面中显示的是该key的value。

Vue.js对获取的data与页面上显示的{{}}会产生一种映射关系，渲染之后，{{}}中会显示对应于data中相同键名的键值。

app变量会代理vue中data的数据，所以我们访问data中数据的时候，直接用app.name就可以了。在浏览器中打开F12，通过app.name='test'可以来改变页面上的显示。

这样，如果我们要实现前后台交互，只要将从后台得到的数据，放在data中，页面就会自动绑定，这样就实现了从model->view映射。

# 第三章 Vue.js指令

指令，其实指的就是vue的v-开头的自定义属性，每个不同的属性都有各自不同的意义和功能。

指令的语法：`v-指令名称="表达式判断或者是业务模型中属性名或者事件名"`

## 3.1 v-text 

作用： 操作元素中的纯文本。

快捷方式： {{}}

## 3.2 v-html  

作用：用于控制html元素


## 3.3 v-bind

作用：v-bind：绑定页面中的元素属性。例如，a的href属性，img的src、alt、title等。

语法：`v-bind:href`

在view模板中可以使用简单的JS表达式，例如`<p>{{num==1?'test1':'test3'}}</p>`。

## 3.4 v-show

作用：通过判断，是否显示该内容。如果值为true，那么就显示，否则就隐藏(该元素是依然存在的)。

语法： v-show="判断表达式"

特点： 


## 3.5 v-if

作用：判断是否加载固定的内容。如果为真，则加载，为假时，不加载。

用途：用在权限管理，页面条件加载。

语法：v-if="判断表达式"

v-if与v-show的区别：
一般来说，v-if有更高的安全性，有更高的切换消耗。而v-show有更多的初始化渲染消耗。
因此，如果需要频繁切换，而对安全性无要求，就使用v-show，如果在运行时，条件不可能改变，则使用v-if较好。

## 3.6 v-else

v-else必须紧跟v-if之后，否则，不能被识别。表示，当v-if条件不成立的时候执行。

