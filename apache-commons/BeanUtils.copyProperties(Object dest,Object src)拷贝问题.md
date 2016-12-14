
# BeanUtils.copyProperties(Object dest,Object src)中的拷贝不成功问题

> 导读 ： 今天用`apache`的`BeanUtils.copyProperties`拷贝属性值时，做了一个测试类，但是爆出一个很奇怪的错误。源对象的属性值无论如何copy不到目标对象中去。

测试代码如下：(分别建立下边三个文件Stu,Tea,Test)

```Java
//注意此处
class Stu{ 
	private String name;

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}
}
```
```Java
//注意此处
class Tea{
	private String name;

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}
}
```

```Java
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

public class Test {

	public static void main(String[] args) {

		Stu stu = new Stu();
		stu.setName("asdf");
		Tea tea = new Tea();

		try {
			BeanUtils.copyProperties(tea,stu);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		System.out.println(tea.getName());
	}
}
```

结果打印出来的`tea`对象下的`name`属性是始终为`null`。于是debug，发现在执行`BeanUtilsBean`以下代码的时候，`stu`对象的`name`属性竟然是不可读的。
```Java
// BeanUtilsBean.java
if (getPropertyUtils().isReadable(orig, name) &&
                    getPropertyUtils().isWriteable(dest, name))
```

仔细查看了下代码，发现`class Stu` 与 `class Tea`的访问权限为`包级`，导致`name`属性不可读，也不可写。

* 解决办法 1  - 将`class Stu` 与 `class Tea`改为`public class Stu` 与 `public class Tea`。

* 解决办法 2  - 不适用`apache`的`BeanUtils.copyProperties`，改用`spring`的`BeanUtils.copyProperties(Object src,Object dest)`。注意`apache`与`spring`的参数顺序。

