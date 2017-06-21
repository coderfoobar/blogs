
# UML 类图

> Create Time : 2017年6月22日 Author : huduku.io  Ref : http://blog.csdn.net/tianhai110/article/details/6339565

在UML类图中，常见的关系有以下集中： 泛化（Generalization），实现（Realization），关联（Association），聚合（Aggregation），组合（Composition），依赖（Dependency）。

## 泛化（Generalization）

`泛化关系` - 是一种继承关系，表示一般与特殊的关系，它指定了子类如何特化父类的所有特征和行为。例如：老虎是动物的一种，即有老虎的特性也有动物的共性。

`箭头指向` - 带空心三角箭头的实线，箭头指向父类。

![Generalization](./uml-class-imgs/generalization.gif)

## 实现 （Realization）

`实现关系` - 是一种类与接口的关系，表示类是接口所有特征和行为的实现。

`箭头指向` - 带空心三角箭头的虚线，箭头指向接口。

![Realization](./uml-class-imgs/realization.gif)

## 关联 （Association）

`关联关系` - 是一种拥有关系，它使一个类知道另一个类的属性和方法：如老师和学生，丈夫和妻子关联可以是双向的，也可以是单向的。双向关联可以有两个箭头或者没有箭头，单项关联只有一个箭头。

`代码体现` - 成员变量

`箭头及指向` - 带普通箭头的实线，指向被拥有者

![Association -1](./uml-class-imgs/association-1.gif)

上图中，老师与学生是双向关联，老师有多名学生，学生也可能有多名老师，但学生与某课程间的关系为单向关联，一名学生可能要上多门课程，课程是个抽象的东西，不拥有学生。

下图为自身关联：

![Association -2](./uml-class-imgs/association-2.gif)

## 聚合 （Aggregation）

`聚合关系` - 是整体和部分的关系，且部分可以离开整体而单独存在。如车和轮胎是整体和部分的关系，轮胎离开车仍然可以存在。

聚合关系是关联关系的一种，是强的关联关系；关联和聚合在语法上没有区分，必须考察具体的逻辑关系来区分。

`代码体现` - 成员变量

`箭头指向` - 带空心菱形的实线，菱形指向整体。

![Aggregation](./uml-class-imgs/aggregation.gif)

## 组合（Composition）

`组合关系` - 是整体和部分的关系，但部分不能离开整体而单独存在。如公司和部门是整体和部分的关系，没有公司就不存在部门。

组合关系是关联关系的一种，是比聚合还要强的关联关系，它要求普通的聚合关系中代表整体的对象负责代表部分的对象的生命周期。

`代码体现` - 成员变量

`箭头指向` - 带实心菱形的实线，菱形指向整体

![Composition](./uml-class-imgs/composition.gif)


## 依赖 （Dependency）

`依赖关系` - 是一种使用的关系，即一个类的实现需要另一个类的协助，所以要尽量不适用双向的互相依赖。

`代码体现` - 局部变量、方法参数、或者对静态方法的调用

`箭头指向` - 带普通箭头的虚线，指向被使用者

![Dependency](./uml-class-imgs/dependency.gif)

各种关系的强弱顺序：

泛化 = 实现 > 组合 > 聚合 > 关联 > 依赖 

下面这张图，比较形象的展示了各种类图关系：


![UML-CLASS](./uml-class-imgs/uml-class.png)
