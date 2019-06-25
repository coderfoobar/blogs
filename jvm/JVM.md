# 深入理解 JVM



## 类加载

* 在 Java 代码中 , 类型的加载 、 连接与初始化过程都是在程序运行期间完成的 。
* 提供了更大的灵活性 ， 增加了更多的可能性 。

## 类加载器深入剖析

* Java 虚拟机与程序的生命周期
* 在以下几种情况下 ， Java 虚拟机将结束生命周期 ：
	+ 执行了 System.exit() 方法 ；
	+ 程序正常执行结束
	+ 程序在执行过程中遇到了异常或错误而异常终止
	+ 由于操作系统出现错误而导致 Java 虚拟机进程终止
	
## 类的加载 、 连接与初始化	

* 加载 ： 查找并加载类的二进制数据
* 连接 ：
  + 验证 ： 确保加载的类的正确性
  + 准备 ： 为类的`静态变量`分配内存 ，并将其初始化为`默认值` 
  + 解析 ： `把类中的符号引用转换为直接引用`
* `初始化 ： 为类的静态变量赋予正确的初始值`

## 类的使用和卸载

* 使用
* 卸载

## 类的加载 、 连接与初始化	- 2

* Java 程序对于类的使用方式可分为两种
  + 主动使用 ( 七种 )
    - 创建类的实例
    - 访问某个类或者接口的静态变量 , 或者对该静态变量赋值
    - 调用类的静态方法
    - 反射 ( 如 `Class.forName("com.test.Test")` )
    - 初始化一个类的子类
    - Java 虚拟机启动时被标明为启动类的类 ( 包含 main 方法的类 )
    - JDK1.7 开始提供的动态语言支持 : 
		`java.lang.invoke.MethodHandle` 实例的解析结果 REF_getStatic , REF_putStatic  , REF_invokeStatic 句柄对应的类没有初始化 , 则初始化
  + 被动使用
    - 除了主动使用的七种情况之外,其他使用 Java 类的方式都被看作是对类的 `被动使用` ,都不会导致类的 `初始化` 
* 所有的 Java 虚拟机实现必须在每个类或接口被 Java 程序 " `首次主动使用` " 时才初始化他们


## 类的加载

* 类的加载指的是将类的 .class 文件中的二进制数据读入到内存中 , 将其放在运行时数据区的方法区内 , 然后在内存中创建一个 java.lang.Class 对象 (规范并未说明 Class 对象位于哪里 , HotSpot 虚拟机将期放在了方法区中) ,  用来封装类在方法区内的数据结构 . 
* 加载 .class 文件的方式 
  - 从本地文件系统中直接加载
  - 通过网络下载 .class 文件
  - 从 zip , jar 等归档文件中加载 .class 文件
  - 从专有数据库中提取 .class 文件
  - 将 Java 源码文件动态编译为 .class 文件 ( 比如动态代理 , jsp )

## 类得使用 demo

* demo1 - 继承与首次使用

```Java

/**
 * 1. 对于静态字段来说 , 只有直接定义了该字段的类才被初始化
 * 2. 当一个类在初始化时 , 要求其父类全部都已经初始化完毕 
 * 3. -XX:+TraceClassLoading , 用于追踪类的加载信息并打印出来
 * 
 * -XX:+<option> , 表示开启 option 选项
 * -XX:-<option> , 表示关闭 option 选项
 * -XX:<option>=<value> , 表示 option 选项的值设置为 value
 **/ 
public class MyTest1 {


	// static {
	// 	System.out.println("MyTest1 static block");
	// }

	/**
	 *  两次打印结果
	 * 1. MyChild1 没有被初始化
	 * MyParent1 static block
	 * hello , parent1
	 * 
	 * 2. 父类先初始化 , 直到 Object 为止
	 * MyParent1 static block
	 * MyChild1 static block
	 * hello , child1
	 **/
	public static void main (String[]  args) {
		//1. 
		System.out.println(MyChild1.str1);
		//2. 
		// System.out.println(MyChild1.str2);
	} 


} 


class MyParent1 {
	public static String str1 = "hello , parent1";

	static {
		System.out.println("MyParent1 static block");
	}
}

class MyChild1 extends MyParent1 {
	public static String str2 = "hello , child1";

	static {
		System.out.println("MyChild1 static block");
	}
}
```


* demo2 - 编译期常量

```Java
/**
 * 常量在编译阶段会存入这个常量的方法所在的常量池中,
 * 本质上 , 调用类并没有直接引用到定义常量的类 , 因此不会触发定义常量的类的初始化
 * 
 * 注意 : 这里指的是将常量 存放到了 MyTest2 的常量池中 , 之后 , MyTest2 与 MyParent2 就没有任何关系了
 * 甚至 , 我们可以将 MyParent2 的 class 文件删除之后执行代码 
 * 
 **/ 
public class MyTest2 {

	public static void main (String[] args) {
		System.out.println(MyParent2.str);
	}
}

class MyParent2 {
	
	/**
	 * 1. 结果
	 * MyParent2 static block
	 * hello , parent2
	 **/
	public static String str = "hello , parent2";
	/**
	 * 2. 结果 - static 语句块没有被调用
	 * hello , parent2
	 **/
	// public static final  String str = "hello , parent2";

	static {
		System.out.println("MyParent2 static block");
	}
}


/**
 * javap -c 反编译
 * 
 * 助记符 : 
 * ldc 表示将 int , float 或者 String 类型的常量值从常量池中推送至栈顶
 * bipush 表示将单字节 (-128 ~ 127) 的常量值推送至栈顶 
 * sipush 表示将一个短整型常量值 (-32768 ~ 32767) 的常量值推送至栈顶
 * iconst 表示将 int 型的数字 1 推送至栈顶 (iconst ~ iconst_5)
 * iconst_m1 表示将 int 型的数字 -1 推送至栈顶
 * 
 **/
```

* demo3 - 运行时常量

```Java
/**
 * 当一个常量的值并非编译起可以确定的 , 那么其值就不会被放到调用类的常量池中 ,
 * 这是在程序运行时 , 会导致主动使用这个常量所在的类 , 显然会导致这个类的初始化 .
 * */
public class MyTest3 {

	public static void main (String[] args) {
		/**
		 * 1. 结果  
		 * MyParent3 static block
		 * UUID 值
		 * 
		 * str 的值在运行期才能确定 , 会导致 MyParent3 初始化
		 **/
		System.out.println(MyParent3.str);
	}
}

class MyParent3 {
	public static final String str = UUID.randomUUID().toString();

	static {
		System.out.println("MyParent3 static block");
	}

}
```


* demo4 - new 与 数组

```Java
/**
 * 当 new 出对象的实例时 , 就会导致类被主动使用 . 
 * 首次主动使用类时 , 会导致类初始化 .
 * 
 * 对于数组实例  , 其类型时由 JVM 在运行期动态生成的 , 表示为 [LMyParent4
 * 
 * 对于数组来说 , JavaDoc 经常将构成数组的元素称为 Component , 
 * 实际上就是数组降低一个维度后的类型 . 
 * 
 * 助记符 : 
 * anewArray 创建一个引用类型的数组 , 并将其引用值压入栈顶
 * newArray 表示创建一个指定的原始类型数组(如 int  , char  , long ) , 并将其引用值压入栈顶 
 **/
public class MyTest4 {

	public static void main (String[] args) {
		/**
		 * 1. 结果 :
		 * MyParent4 static block
		 * */
		MyParent4 myParent4 = new MyParent4 ();

		/**
		 * 2. 结果 : 没有导致 MyParent4 初始化
		 * class [LMyParent4
		 **/
		MyParent4[] myParent4cls = new MyParent4[1];
		System.out.println(myParent4cls.getClass());

		/**
		 * 3. 结果 :
		 * class [[LMyParent4
		 **/
		MyParent4[][] myParent4cls2Arr = new MyParent4[1][1];
		System.out.println(myParent4cls2Arr.getClass());

		/**
		 * 4. 结果 
		 * class [I
		 * class java.lang.Object
		 **/
		int[] iArr = new int[1];
		System.out.println(iArr.getClass());
		System.out.println(iArr.getClass().getSuperClass());

		/**
		 * 助记符 : [C
		 * */
		char[] cArr = new char[1];
		System.out.println(cArr.getClass());

		/**
		 * 助记符 : [Z
		 **/
		boolean[] bArr = new boolean[1];
		System.out.println(bArr.getClass());

		/**
		 * 助记符 : [S
		 **/
		short[] sArr = new short[1];
		System.out.println(sArr.getClass());

		/**
		 * 助记符 : [B
		 **/
		byte[] byteArr = new byte[1];
		System.out.println(byteArr.getClass());
	}

}

class MyParent4 {

	static {
		System.out.println("MyParent4 static block");
	}
}
```

* demo5 - 接口与接口继承 , 编译器 、 运行期常量

```Java

/**
 * 当一个接口初始化时 , 并不要求其父接口都完成了初始化
 * 
 * 只有在真正的使用到父接口 ( 如引用接口中所定义的常量时 ) , 才会初始化
 * 
 * */
public class MyTest5 {

	public static void main (String[] args) {
		System.out.println(MyChild5.b);
	}
} 

interface MyParent5 {

	int a = 5;
}

interface MyChild5 extends MyParent5 {
	int b = 6;
}
```


* demo6 

```Java
public class MyTest6 {

	public static void main (String[] args) {
		Singleton singleton =  Singleton.getInstance();

		System.out.println("counter1 = " + Singleton.counter1); // 1
		System.out.println("counter2 = " + Singleton.counter2); // 0
	} 

}

class Singleton {

	public static int counter1;

	

	private static Singleton = new Singleton() ;

	private Singleton () {
		counter1 ++;
		counter2 ++;
		// 准备阶段
		System.out.println(counter1); // 1
		System.out.println(counter2); // 1
	}

	// 初始化阶段
	public static int counter2 = 0;

	public static Singleton getInstance () {
		return singleton;
	} 
}
```


## 类的加载

* 类的加载的最终产品是位于内存中的 Class 对象
* Class 对象封装了类在方法区内的数据结构 , 并且向 Java 程序员提供了访问方法区内的数据结构的接口 .
* 由两种类型的类加载器 
  + Java 虚拟机自带的加载器
    - 根类加载器 ( Bootstrap )
    - 扩展类加载器 ( Extention )
    - 系统 ( 应用 ) 类加载器 ( System )
  + 用户自定义的类加载器
    - java.lang.ClassLoader 的子类
    - 用户可以定制类的加载方式
* 类加载器并不需要某个类被 " 首次主动使用 " 时再加载它
* JVM 规范允许类加载器再预料某个类将要被使用时就预先加载它 , 如果在预先加载的过程中遇到了 .class 文件缺失或存在错误 , 类加载器必须在`程序首次主动`使用该类时才报告错误 ( `LinkageError` 错误 )
* 如果这个类一直没有被程序主动使用 , 那么`类加载器就不会报告错误`
* 类被加载后 , 就进入连接阶段 . 连接就是将已经读入内存的类的二进制数据合并到虚拟机的运行时环境中去 .
* 类的验证
  - 类文件的结构检查
  - 语义检查
  - 字节码验证
  - 二进制兼容性验证
  - 魔数检查

## 类的准备

在准备阶段 , Java 虚拟机为类的静态变量分配内存 , 并设置默认的初始值 . 例如 , 对于以下 Sample 类 , 在准备阶段 , 将为 int 类型的静态变量 a 分配 4 个字节的内存空间 , 并且赋予默认值 0 , 为 long 类型的静态变量 b 分配 8 个字节的内存空间 , 并且赋予默认值 0 .

```Java
public class Sample {
	private static int a = 1;
	public static long b;

	static {
		b = 2;
	}

	//... ...
}
```

## 类的初始化

* 在初始化阶段 , Java 虚拟机执行类的初始化语句 , 为类的静态变量赋予初始值 .
* 在程序中 , 静态变量的初始化有两种途径 : 
  1. 在静态变量的生命处进行初始化
  2. 在静态代码块中进行初始化 , 例如在以下代码中 , 静态变量 a 和 b 都被显式初始化 , 而静态变量 c 没有被显式初始化 , 它将保持默认值 0 .

```Java
public class Sample {

	private static int a = 1;
	public static long b ;
	public static long c ;

	static {
		b = 2;
	}

	// ... ...
}
```

* 静态变量的声明语句 , 以及静态代码块都被看做类的初始化语句 , Java 虚拟机会按照初始化语句在类文件中的先后顺序来依次执行它们 . 例如当以下 Sample 类被初始化后 , 它的静态变量 a 的取值为 4 .

```Java
public class Sample {

	static int a = 1;
	static { a = 2 ; }
	static { a = 4 ; }

	public static void main (String[] args) {
		System.out.println("a = " + a); // 4
	} 
}
```

## 类初始化的步骤

* 假如这个类还没有被加载和连接 , 那就先进行加载和连接
* 假如类存在直接父类 , 并且这个父类还没有被初始化 , 那就先初始化直接父类
* 假如类中存在初始化语句 , 那就依次执行这些初始化语句

## 类初始化的时机

* 主动使用 (七种 , `重要`)
* 当 Java 虚拟机初始化一个类时 , 要求它的所有父类都已经被初始化 , 但是这条规则不适用于接口 
  - 在初始化一个类时 , 并不会初始化它所实现的接口
  - 在初始化一个接口时 , 并不会先初始化它的父接口

> 因此 , 一个父接口并不会因为它的子接口或者实现类的初始化而初始化 . 只有当程序首次使用特定接口的静态变量时 , 才会导致该接口的初始化 .

* 只有当程序访问的静态变量或静态方法确实在被调用类或接口中定义时 , 才可以认为是对类或者接口的主动使用 .


## 类加载器

类加载器用来把类加载到 Java 虚拟机中 , 从 JDK1.2 版本开始 , 类的加载过程采用双亲委派机制 , 这种机制更好的保证 Java 平台的安全 . 在此委托机制中 , 除了 Java 虚拟机自带的根类加载器外 , 其余的类加载器都有且只有一个父加载器 . 当 Java 程序请求加载器 loader 加载 Sample 类时, loader1 首先委托自己的父加载器去加载 Sample 类 , 若父加载器能加载 , 则由父加载器完成加载任务 , 否则才由加载器 loader1 本身加载 Sample 类.

Java 虚拟机自带了以下几种加载器 :

* 根 (Bootstrap) 类加载器 - 该加载器没有父加载器 , 它负责加载虚拟机核心类库 , 如 java.lang.* 等 . 根加载器从 sun.boot.class.path 所指定的目录中加载类库 . 根类加载器的实现依赖于底层操作系统给. 属于虚拟机实现的一部分 . 它并没有实现 java.lang.ClassLoader .
* 扩展类加载器 (Extention) - 它的父加载器为根类加载器 , 它从 java.ext.dirs 系统属性所指定的目录中加载类库 , 或者从 JDK 的安装目录 jre/lib/ext 子目录(扩展目录) 下加载类库 , 如果把用户创建的 JAR 文件放在这个目录下 , 也会自动由扩展类加载器加载 , 扩展类加载器是纯 Java 类 , 是 java.class.ClassLoader 类的子类
*  系统 (System) 类加载器 - 也成为应用类加载器 , 它的父加载器为扩展类加载器 . 它从环境变量 CLASSPATH 或者系统属性 java.class.path 所指定的目录中加载类 . 它是用户自定义的类加载器的默认父加载器 , 系统类加载器是纯 Java 类 , 是 java.class.ClassLoader 类的子类 .
* 除了以上虚拟机自带的类加载器外 , 用户还可以自定义自己的类加载器 , `Java 提供了抽象类 java.class.ClassLoader , 所有用户自定义的类加载器都应该继承该类` .














































































