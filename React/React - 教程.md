# React - 教程

> Create Time ： 2017年4月9日 Ref : http://www.runoob.com/react/react-tutorial.html

React 是一个用于构建用户界面的JavaScript库。

React主要用于构建UI，很多人认为React是MVC中的V（视图）。

React 起源于Facebook的内部项目，用来假设Instagram的网站，并于2013年5月开源。

React拥有较高的性能，代码逻辑非常简单，越来越多的人已开始关注和使用它。

## React 特点

* **声明式设计** - React采用声明范式，可以轻松描述应用。
* **高效** - React通过对DOM的模拟，最大限度地减少与DOM的交互。
* **灵活** - React可以与已知的库或框架很好地配合。
* **JSX** - JSX是JavaScript语法的扩展。React开发不一定使用JSX，但我们建议使用它。
* **组件** - 通过React构建组件，使代码更加容易得到复用，能够很好的应用在大项目的开发中。
* **单向的响应数据流** - React实现了单向响应的数据流，从而减少了重复代码，这也是它为什么比传统数据绑定更简单。

## 阅读本教程前，你需要了解的知识

* HTML5
* CSS
* JavaScript

## React第一个实例

在每个章节中，您可以在线编辑实例，然后点击按钮查看结果。

本教程使用React的版本为15.4.2，你可以在官网 http://facebook.github.io/react/ 下载最新版。

React实例
```html
<div id="example"></div>
<script type="text/babel" >
    ReactDOM.render(
        <h1>hello,world!<h1>,
        document.getElementById('example');
    );
</script>
```