
# JS - 秘密花园

> Create Time : 2017年4月9日 Ref : http://bonsaiden.github.io/JavaScript-Garden/zh/

# 简介

## 关于作者

这篇文章的作者是两位StackOverflow用户，伊沃·维特泽尔Ivo Wetzel(写作)和张易江（设计）。

## 贡献者

* 贡献者 https://github.com/BonsaiDen/JavaScript-Garden/graphs/contributors
* 中文翻译 - 三生石上

此中文翻译由三生石上独立完成，博客园首发，转载请注明出处。

## 许可

JavaScript秘密花园在MIT license许可协议下发布，并存放在GitHub开源社区。如果你发现错误或者打字错误，请新建一个任务单或者发一个抓取请求。你也可以在Stack Overflow的Java聊天室找到我们。

# 对象

## 对象使用和属性

JavaScript中所有变量都可以当作对象使用，除了两个例外`null`和`undefined`。

```JavaScript
false.toString();//'false'
[1,2,3].toString();//'1,2,3'

function Foo(){}
Foo.bar=1;
Foo.bar;//1
```

一个常见的误解是数字的字面值（literal）不能当对象使用。这是因为Javascript解析器的一个错误，它试图将***点操作符***解析为浮点数字面值的一部分。

```JavaScript
2.toString();  // 出错： SyntaxError
```

有很多变通方法可以让数字的字面值看起来像对象。

```JavaScript
2..toString(); //第二个点号可以正常解析
2 .toString(); //注意点号前的空格
(2).toString(); // 2先被计算
```

### 对象作为数据类型

JavaScript的对象可以作为`哈希表`使用，主要用来保存命名的键与值的对应关系。

使用对象的字面语法 - `{}` - 可以创建一个简单对象。这个新创建的对象从`Object.prototype` `继承`下面，没有任何`自定义属性`。

```JavaScript
var foo = {} ; //一个空对象
// 一个新对象，拥有一个值为12的自定义属性`test`
var bar = {test : 12};
```

### 访问属性

有两种方式来访问对象的属性，点操作符或者中括号操作符。

```JavaScript
var foo = {name:'kitten'}
foo.name; // kitten
foo['name']; // kitten

var get = 'name';
foo[get]; // kitten

foo.1234; // SyntaxError
foo['1234']; // works
```

两种语法是等价的，但是中括号操作符在下面两种情况下依然有效。

* 动态设置属性
* 属性名不是一个有效的变量名（**译者注：**比如属性名中包含空格，或者属性名是JS关键词 ）

### 删除属性

删除属性的唯一方法是使用`delete`操作符；设置属性为`undefined`或者`null`并不能真正的删除属性，而仅仅是移除了属性和值的关联。

```JavaScript
var obj = {
    bar : 1,
    foo : 2,
    baz : 3
};

obj.bar = undefined;
obj.foo = null;
delete obj.baz;

for (var i in obj) {
    if (obj.hasOwnProperty(i)) {
        console.log(i,' ' + obj[i]);
    }
}
```

上面的输出结果有`bar  undefined`和`foo  null` - 只有baz被真正的删除了，所以从输出结果中消失。

## 属性名的语法

```JavaScript
var test = {
    'case' : 'I am a keyword so I must be notated as a string',
    delete : 'I am a keywork too so me' //出错：SyntaxError
};
```

对象的属性名可以使用字符串或者普通字符声明。但是由于JavaScript解析器的另一个错误设计，上面的第二种声明方式在EcmaScript 5之前会抛出`SyntaxError`的错误。

这个错误的原因是`delete`是JavaScript语言的一个关键词；因此为了在更低版本的JavaScript引擎下也能正常运行，必须使用***字符串字面值***的声明方式。

---

## 原型

JavaScript不包含传统的类继承模型，而是使用`prototype`原型模型。

虽然这经常被当作是JavaScript的缺点被提及，其实基于原型的集成模型比传统的类继承还要强大。实现传统的类继承模型很简单，但是实现JavaScript中的原型继承则要困难的多。（It is for example fairly trivial to build a classic model on top of it, while the other way around is a far more difficult task.）

由于JavaScript是唯一一个被广泛使用基于原型继承的语言，所以理解两种继承模式的差异是需要一定时间的。

第一个不同之处在于JavaScript使用`原型链`的继承方式。

```JavaScript
function Foo() {
    this.value = 42;
}

Foo.prototype = {
    method : function(){} 
}

function Bar(){ }

//设置Bar的prototype属性为Foo的实例对象
Bar.prototype = new Foo();
Bar.prototype.foo = 'hello world';

// 修正Bar.prototype.constructor为Bar本身
Bar.prototype.constructor = Bar;

var test = new Bar(); //创建一个Bar的新实例

//原型链
test[Bar的实例]
    Bar.prototype [Foo的实例]
        {foo : 'hello world'}
        Foo.prototype
            {method : ...};
            Object.prototype
                {toString:.... /* etc.*/ }
```

上面的例子中，`test`对象从`Bar.prototype`和`Foo.prototype`继承下来；因此，它能访问`Foo`的原型方法`method`。同时，它也能够访问那个定义在原型上的`Foo`实例属性`value`。需要注意的是`new Bar()`不会创造一个新的`Foo`实例，而是重复使用它原型上的那个实例；因此，所有的`Bar`实例都会共享相同的`value`属性。

### 属性查找

当查找一个对象的属性时，JavaScript会向上遍历原型链，直到找到给定名称的属性为止。

到查找到原型链的顶部 - 也就是`Object.prototype` - 但是仍然没有找到指定的属性，就会返回`undefined`。

### 原型属性

当原型属性用来创建原型链时，可以把任何类型的值赋给他（prototype），然而将原子类型赋给prototype的操作将会被忽略。

```JavaScript
function Foo () { }

Foo.prototype  = 1;//无效
```

而将对象属性赋给prototype，正如上面的例子所示，将会动态的创建原型链。

### 性能

如果一个属性在原型链的上端，则对于查找时间将带来不利影响。特别的，试图获取一个不存在的属性将会遍历整个原型链。

并且，当时用`for in`循环便利对象属性时，原型链上的所有属性都将被访问。

### 扩展内置类型的原型

一个错误特性经常使用，那就是扩展`Object.prototype`或者其他内置类型的原型对象。

这种技术被称之为`monkey patching`并且会破坏封装。虽然它被广泛应用到一些JavaScript类库中，比如`Prototype`，但是我仍然不认为为内置类型添加一些***非标准***的函数是个好主意。

扩展内置类型的唯一理由是为了和新的JavaScript报纸一致，比如`Array.forEach`。

### 总结

在写复杂的JavaScript应用之前，充分理解原型链集成的工作方式是每个JavaScript程序员必修的功课。要提防原型链过长带来的性能问题，并指导如何通过锁单原型链来提高性能。更进一步，绝对不要扩展内置类型的原型，除非是为了和新的JavaScript引擎兼容。

---

## `hasOwnProperty`函数

为了判断一个对象是否包含自定义属性而不是`原型链`上的属性，我们需要使用集成子`Object.prototype`的`hasOwnProperty`方法。

`hasOwnProperty`是JavaScript中唯一一个处理属性但是不查找原型链的函数。

```JavaScript
//修改Object.prototype.bar = 1;
var foo = {goo : undefined};

foo.bar; // 1
'bar' in foo; //true

foo.hasOwnProperty('bar');//false
foo.hasOwnProperty('goo');//true
```

### 结论

当检查对象上某个属性是否存在时，`hasOwnProperty`是唯一可用的方法，同时在使用`for in loop`遍历对象时，推荐总是使用`hasOwnProperty`方法，这将会避免`原型`对象扩展带来的干扰。

---

## `for in`循环

和`in`操作符一样，`for in`循环同样在查找对象属性时遍历原型链上的所有属性。

```JavaScript
//修改Object.prototype
Object.prototype.bar = 1;

var foo = {moo:2};
for (var i in foo) {
    console.log(i); //输出两个属性： bar 和 moo
}
```

由于不太可能改变 `for in`自身的行为，因此有必要过滤输出那些不希望出现在循环体中的属性，这可以通过`Object.prototype`原型上的`hasOwnProperty`函数来完成。

### 使用`hasOwnProperty`过滤

```
//foo 变量是上例中的
for (var i in foo) {
    if(foo.hasOwnProperty(i)) {
        console.log(i);
    }
}
```

这个版本的代码是唯一正确的写法。由于我们使用了`hasOwnProperty`，所以这次只输出`moo`。如果不适用`hasOwnProperty`，则这段代码在原生对象原型（比如`Object.prototype`）被扩展时，可能会出错。

一个广泛使用的类库`Prototype`就扩展了原生的JavaScript对象。因此，当这个类库被包含在页面中时，不使用`hasOwnProperty`过滤的`for in`循环难免会出现问题。

### 结论

推荐总是使用`hasOwnProperty`。不要对代码运行的环境做任何假设，不要假定原生对象是否已经被扩展了。



---
---




# 函数



