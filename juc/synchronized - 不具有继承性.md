
# synchronized - 不具有继承性

> Crreate Time : 2017年4月6日  Author : huduku.io

父类为`AbstractService`，子类为`CustService`，如果父类中的方法`upsert`带有修饰符`synchronized`，子类继承该方法，但是去掉了`synchronized`关键字，那么父类的`upsert`方法是同步的，子类的`upsert`方法是不同步的。
