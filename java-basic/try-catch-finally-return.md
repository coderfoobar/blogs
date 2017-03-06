
# try-catch-finally-return

> Create Time : 2017年3月3日  Author : huduku.io

## `try` `catch` `finally` 以及 `finally` 之后四个代码块中，均含有`return`语句，则最终返回的是哪一个值？


1、不管有木有出现异常，`finally`块中代码都会执行；
2、当`try`和`catch`中有`return`时，`finally`仍然会执行；
3、`finally`是在`return`后面的表达式运算后执行的（此时并没有返回运算后的值，而是先把要返回的值保存起来，管`finally`中的代码怎么样，返回的值都不会改变，任然是之前保存的值），所以函数返回值是在`finally`执行前确定的；
4、`finally`中最好不要包含`return`，否则程序会提前退出，返回值不是`try`或`catch`中保存的返回值。


## 最佳实践 ： 在何处使用`return`

答案是：`try`和`catch`。上边已经说明，`finally`中使用`return`可能会导致程序提前退出。

如果`finally`后仍有逻辑需要执行，在必要的逻辑执行完之后才加上`return`才合适。(`try`块中的`return`就不再适宜了)。
