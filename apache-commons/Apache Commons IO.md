
# Apache Commons IO


## 概要

> 本文全部引用自apache commons io的官网文档。

### 项目信息

| Field | Value |
| :-- | :-- |
| 项目名称 | Apache Commons IO |
| 项目描述 | Apache Commons IO 库包含了工具类，流的实现， 文件过滤器，文件比较器，endian转换类，还有其他更多的内容。|
| 项目主页 | http://commons.apache.org/proper/commons-io/ |

### 项目组织

| Field | Value |
| :--- | :---|
| 组织名称 | The Apache Software Foundation |
| URL | http://www.apache.org/ | 

### 构建信息

| Field | Value |
| :--- | :---|
| GroupId | commons-io |
| ArtifactId | commons-io |
| Version | 2.5 |
| Type | jar |
| Java Version | 1.6 |


### 依赖信息

#### Apache Maven

```xml
<dependency>
  <groupId>commons-io</groupId>
  <artifactId>commons-io</artifactId>
  <version>2.5</version>
</dependency>
```

#### Apache Buildr

```
'commons-io:commons-io:jar:2.5'
```

#### Apache Ivy

```xml
<dependency org="commons-io" name="commons-io" rev="2.5">
  <artifact name="commons-io" type="jar" />
</dependency>
```

#### Groovy Grape

```
@Grapes(
    @Grab(group='commons-io', module='commons-io', version='2.5')
)
```

#### Grails

```
compile 'commons-io:commons-io:2.5'
```

#### Leiningen

```
[commons-io "2.5"]
```

#### SBT

```
libraryDependencies += "commons-io" % "commons-io" % "2.5"
```

### 项目依赖

* commons-io:commons-io:jar:2.5  
    * junit:junit:jar:4.12 (test)  
        * org.hamcrest:hamcrest-core:jar:1.3 (test) 

### 持续集成

#### Access

 <https://continuum-ci.apache.org/>

#### Notifiers

No notifiers are defined. Please check back at a later date.


### Java Docs

<http://commons.apache.org/proper/commons-io/javadocs/api-1.4/index.html>

***

## 指南

### 工具类

#### IOUtils

[IOUtils](http://commons.apache.org/proper/commons-io/javadocs/api-release/index.html?org/apache/commons/io/IOUtils.html) 包含了用来处理读、写、复制
的工具方法。这些方法基于 InputStream，OutputStream ， Reader ， Writer运行。

例：想象一下，一个从URL读取字节并将其打印的任务，典型的实现代码如下：

```Java
InputStream in  = new URL("http://commons.apache.org").openStream();

try{
    InputStreamReader inR = new InputStreamReader(in);
    BufferedReader buf = new BufferedReader(inR);
    String line;
    while( ( line = buf.readLine() ) != null ){
        System.out.println( line );
    }
}finally{
    in.close();
}

```

而通过IOUtils类，实现如下：
```Java
InputStream in = new URL( "http://commons.apache.org" ).openStream();
try{
    System.out.println( IOUtils.toString( in ) );
}finally{
    IOUtils.closeQuietly( in );
}
```
在特定的应用领域里，这样的IO操作很普遍，并且这个类将会节省很大一部分时间。你更可以依赖这些经过良好测试的代码。

对于像这样的工具代码，弹性和速度是头等重要的。但是你应当意识道这种实现的局限性。使用上述的技术读取一个1GB的文件将会创建一个1GB大小的String对象。

#### FileUtils

[FileUtils](http://commons.apache.org/proper/commons-io/javadocs/api-release/index.html?org/apache/commons/io/FileUtils.html)
包含了与File对象一起工作的工具方法。这些方法包括读、写、复制、比较文件。

比如，一行一行的读取整个文件你可能会这样实现：

```Java
File file = new File("/commons/io/project.properties");
List lines = FileUtils.readLines(file,"UTF-8");
```

#### FilenameUtils

[FilenameUtils](http://commons.apache.org/proper/commons-io/javadocs/api-release/index.html?org/apache/commons/io/FilenameUtils.html)
包含了不使用File对象操作文件名的工具类方法。这个类的主旨是使Unix和Windows一致，为了帮助这些环境之间的过渡（比如说开发环境和生产环境）。

比如为了规范化的去除两个点的路径：
```Java
String filename = "C:/commons/io/../lang/project.xml";
String normalized = FilenameUtils.normalize(filename);
//结果是  "C:/commons/lang/project.xml"
```

#### FileSystemUtils

[FileSystemUtils](http://commons.apache.org/proper/commons-io/javadocs/api-release/index.html?org/apache/commons/io/FileSystemUtils.html)
包含了一系列不被jdk支持的操作文件系统的工具方法。现在，唯一的方法是用来获取磁盘剩余空间。注意这使用的是命令行而不是native代码。

比如：
```Java
long freeSpace = FileSystemUtils.freeSpace("C:/");
```

#### Endian classes
不同的计算机架构包含了不同的byte ordering的习惯。在被称作“Little Endian”的架构（比如Intel）上，
low-order byte被存储在内存的最低地址，并且随后的字节在高位地址。对于“Big Endian”架构（比如Motorola），情况恰好相反。

这个包中有两个类与之相关：
* [EndianUtils](http://commons.apache.org/proper/commons-io/javadocs/api-release/index.html?org/apache/commons/io/EndianUtils.html)
类包含了用于交换Java primitives和streams的Endian-ness的静态方法。
* [SwappedDataInputStream](http://commons.apache.org/proper/commons-io/javadocs/api-release/index.html?org/apache/commons/io/input/SwappedDataInputStream.html)
类是一个`DataInput`接口的实现，通过这个类，用户可以读取文件数据而不需理会Endian-ness。

> 更多信息，请查看<http://www.cs.umass.edu/~verts/cs32/endian.html >

#### Line iterator

`org.apache.commons.io.LineIterator` 类提供了一种弹性的方式来操作line-based的文件。LineIterator的实例可以通过FileUtils或者IOUtils的钢厂方法直接创建。
推荐的使用方式是：
```Java
LineIterator it = FileUtils.lineIterator(file,"UTF-8");
try{
    while( it.hasNext() ){
        String line = it.nextLine();
        //do something with line
    }
}finally{
    LineIterator.closeQuietly(it);
}
``` 

#### File filters

包`org.apache.commons.io.filefilter`定义了一个接口[IOFileFilter](http://commons.apache.org/proper/commons-io/javadocs/api-release/index.html?org/apache/commons/io/filefilter/IOFileFilter.html)
合并了`java.io.FileFilter`和`java.io.FilenameFilter`。并且这个包提供了一系列随时取用的`IOFileFilter`的实现。这些实现允许你合并其他类似的过滤器。
比如，这些过滤器可以用来`list files`或者`FileDialog`

> 更多信息，请查看[filefilter]()包的javadoc。


#### File comparators

包`org.apache.commons.io.comparator`为`java.io.File`提供了`java.util.Comparator`的一些实现。
比如这些比较器可以被用来给文件列表排序。

> 更多信息，请查看[Comparator]()包的javadoc。


#### Streams

包`org.apache.commons.io.input`和`org.apache.commons.io.output packages`包含了各种有用的流的实现。内容如下：

* `Null output stream` - 会静默的接收发送给它的数据
* `Tee output stream` - 发送output数据给两个流而不是一个
* `Byte array output stream` - 这是一个比JDK类更快的版本
* `Counting streams` - 查出经过的字节数
* `Proxy streams` - proxy中的正确方法的delegate
* `Lockable writer` - 使用锁文件提供同步写操作

> 更多信息请查看[input]()和[output]()的javadoc。

***结束***
































