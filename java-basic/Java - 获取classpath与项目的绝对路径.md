
# Java - 获取classpath与项目的绝对路径

> Create Time : 2016年12月24日

`ClassLoader` 提供了两个方法勇于从装在的类路径中取得资源。

```Java
public URL getResource(String name);
public InputStream getResourceAsStream(String name);
```

这里`name`是资源的类路径，它是相对于“`/`”根路径下的位置。`getResource`得到的是一个URL对象来定位资源，而`getResourceAsStream`取得该资源输入流的引用保证程序可以从正确的位置抽取数据。

但是真正使用的不是`ClassLoader`的这两个方法，而是`Class`的`getResource`和`getResourceAsStream`方法，因为`Class`对象可以从你的类得到（比如`YourClass.class`或`yourClassInstance.getClass()`），而`ClassLoader`则需要再调用一次`YourClass.getClassLoader()`方法，不过根据JDK文档的说法，`Class`对象的这两个方法其实是“委托”（`delegate`）给它的`ClassLoader`来做的，所以只需要使用`Class`对象的这两个方法就可以了。

因此,直接使用`this.getClass().getResourceAsStream(String name)`获取流，静态方法中使用`ClassLoacer.getSystemResourceAsStream(String name)`。

下面是一些得到`classpath`和当前类的绝对路径的一些方法。你可能需要使用其中的一些方法来得到你需要的资源的绝对路径。

* 1. `this.getClass().getResource("")`

    * 得到的是当前类`class`文件的`URI`目录。不包括自己！

* 2. `this.getClass().getResource("/")`

    * 得到的是当前classpath的绝对`URI`路径。

* 3. `this.getClass().getClassLoader().getResource("")`

    * 同`2`
    
* 4. `ClassLoader.getSystemResource("")`

    * 同`2`

* 5. `Thread.currentThread().getContextClassLoader().getResource("")`

    * 同`2`

* 6. `ServletActionContext.getServletContext.getRealPath("/")`

    * Web程序中，得到Web应用程序的根目录的绝对路径，这样，我们只需要提供相对于Web应用程序根目录的路径，就可以构建出定位资源的绝对路径。

**注意：**

* 1. 尽量不要使用个相对于`System.getProperty("user.dir")`当前用户目录的相对路径。这是一颗定时炸弹，随时可能要你的命。

* 2. 尽量个使用`URI`形式的绝对路径资源。他可以很容易的转变为`URI`，`URL`，`File`对象。

* 3. 尽量使用相对`classpath`的相对路径，不要使用绝对路径。

* 4. 绝对不要使用硬编码的绝对路径，因为我们可以使用`ClassLoader.getSystemResource("")`方法得到当前classpath的绝对路径。

//获取当前jar路径
MyClass.class.getProtectionDomain().getCodeSource().getLocation();


