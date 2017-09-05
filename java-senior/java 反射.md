
# Java 反射

> Create Time : 2017年6月21日 Ref : http://www.cnblogs.com/zhaoyanjun/p/6074887.html

## 反射机制是什么

反射机制是在运行状态中，对任意一个类，都能够知道这个类的所有属性和方法，对于任意一个对象，都能够调用它的任意一个方法和属性；这种动态获取的信息以及动态调用对象方法的功能称为Java的反射机制。

## 反射机制能做什么

反射机制主要提供了以下功能：

* 在运行时判断任意一个对象所属的类
* 在运行时构造任意一个类的对象
* 在运行时判断任意一个类所具有的成员变量和方法
* 在运行时调用任意一个对象的方法
* 生成动态代理

## 反射机制API

Interface接口

```Java
package com.app;
public class Interface {
    void read();
}
```

Person类:

```Java
package com.app;

public class Person implents Interface {
    private String id;
    private String name;
    public String age;

    public Person(){}

    public Person(String id){
        this.id = id;
    }

    public Person(String id,String name){
        this.id = id;
        this.name = name;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge(){
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    /**
     * 静态方法
     **/
    public static void update(){

    }

    /**
     * 实例方法
     **/
     @Override
     public void read(){
         
     }
}
```

### 获取Class （3种方法）

```Java
package com.app;
public class T1 {
    public static void main(String[] args) {
        //第一种方法 Class.forName
        try{
            Class<?> clazz = Class.forName("com.app.Person");
            System.out.println(clazz);
        }catch (ClassNotFoundException e) {
            e.printStackTrade();
        }

        //第二种方法 class
        Class<?> clazz2 = Person.class;
        System.out.println(clazz2);

        //第三种方法getClass()
        Person person = new Person();
        Class<?> clazz3 = person.getClass();
        System.out.println(clazz3);
    }
}

//运行结果
// class com.app.Person
// class com.app.Person
// class com.app.Person
```

### 获取所有方法: getMethods()

```Java
package com.app;
imort java.lang.reflect.Method;

public class T2 {
    public static void main(String[] args) {
        try{
            //创建类
            Class<?> clazz1 = Class.forName("com.app.Person");
            //获取所有public的方法
            Method[] methods = clazz1.getMethods();
            for(Method method : methods) {
                System.out.println(method);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
```

运行结果

```Java
//自定义方法
public static void com.app.Person.update()
public java.lang.String com.app.Person.getName()
public void com.app.Person.read()
public java.lang.String com.app.Person.getId()
public void com.app.Person.setName(java.lang.String)
public void com.app.Person.setId(java.lang.String)

//父类Object类方法
public final void java.lang.Object.wait() throws java.lang.InterruptedException
public final void java.lang.Object.wait(long,int) throws java.lang.InterruptedException
public final native void java.lang.Object.wait(long) throws java.lang.InterruptedException
public boolean java.lang.Object.equals(java.lang.Object)
public java.lang.String java.lang.Object.toString()
public native int java.lang.Object.hashCode()
public final native java.lang.Class java.lang.Object.getClass()
public final native void java.lang.Object.notify()
public final native void java.lang.Object.notifyAll()
```

### 获取所有实现的接口：  getInterfaces()

```Java
package com.app;

public class T3 {
    public static void main(String[] args) {
        try{
            //创建类
            Class<?> clazz1 = Class.forName("com.app.Person");
            //获取实现的接口
            Class[] inters = class.getInterfaces();
            for(Class<?> inter : inters) {
                System.out.println(inter);
            }
        }catch(ClassNotFountException e){
            e.printStackTrace();
        }
    }
}
```


运行结果

```
interface com.app.InterFace
```

### 获取父类: getSuperclass()

```Java
package com.app;
public class T4{
    public static void main(String[] args) {
        try{
            // 创建类
            Class<?> clazz = Class.forName("com.app.Person");
            //获取父类
            Class<?> superClazz = clazz.getSuperclass();
            System.out.println(superClazz);
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
```

运行结果

```
class java.lang.Object
```

### 获取所有的构造方法: getConstructors()

```Java
package com.app;

import java.lang.reflect.Constructor;

public class T4{
    public static void main(String[] args) {
        try{
            //创建类
            Class<?> clazz = Class.forName("com.app.Person");
            //获取所有的构造方法
            Constructor<?>[] constructors = clazz.getConstructors();
            for(Constructor construct : constructors) {
                System.out.println(construct);
            }
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
```

运行结果

```
public com.app.Person(java.lang.String,java.lang.String)
public com.app.Person(java.lang.String)
public com.app.Person()
```

### 获取所有属性: getDeclaredFields()

 ```Java
 package com.app;
 import java.lang.reflect.Constructor;
 import java.lang.reflect.Field;

 public class T5{
     public static void main(String[] args) {
         try{
            //创建类
            Class<?> clazz = Class.forName("com.app.Person");
            //取得本类的全部属性
            Field[] fields = clazz.getDeclaredFields();
            for(Field field : fields) {
                System.out.println(field);
            }
         }catch(ClassNotFoundException e) {
            e.printStackTrace();
         }
     }
 }
 ```

 运行结果

 ```
 private java.lang.String com.app.Person.id
 private java.lang.String com.app.Person.name
 private java.lang.String com.app.Person.age
 ```

 可以看出属性的修饰符是： private ，数据类型是： String， 名字 ： id/name/age

 ### 创建实例 ： newInstance()

 ```Java
 package com.app;

 publci class T6{
     public static void main(String[] args){
         try{
            //创建类
            Class<?> clazz = Class.forName("com.app.Person");
            //创建实例化,相当于new了一个对象
            Object object = clazz.newInstance();
            //向下转型
            Person person = (Person)object;
         }catch(ClassNotFoundException e){
             e.printStackTrace();
         }catch(InterfaceException e){
             e.printStackTrace();
         }catch(IllegalAccessException e){
             e.printStackTrace();
         }
     }
 }
 ```


## getDeclaredFields 和 getFields的区别 

> getDeclaredFields() 获得某个类的所有声明的字段，即包括public，private，和protected，但是不包括父类的声明字段。getFields() 获得某个累的所有public的字段，包括父类。

sample：

```Java
package com.app;

import java.lang.reflect.Field;

public class T7{
    public static void main(String[] args){
        try{
            //创建类
            Class<?> clazz = Clazz.forName("com.app.Person");
            //获得所有的字段属性，包括private protected和包级的字段
            Field[] declaredFields = clazz.getDeclaredFields();
            //获取public属性（包含父类）
            Field[] fields = clazz.getFields();
            for(Field field : declaredFields) {
                System.out.println("de-- " + field);
            }
            for(Field field : fields) {
                System.out.println("de-- " + field);
            }
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
```

运行结果

```
de--  private java.lang.String com.app.Person.id
de--  private java.lang.String com.app.Person.name
de--  public java.lang.String com.app.Person.age
fields--  public java.lang.String com.app.Person.age
```


---


# 实战1： 通过反射，获取对象实例，并且操作对象的方法

```Java
package com.app;
public class T8 {
    public static void main(String[] args){
        try{
            //创建类
            Class<?> clazz = Class.forName("com.app.Person");
            //创建实例化，相当于new了一个对象
            Object obj = clazz.newInstance();
            //向下转型
            Person person = (Person)obj;
            person.setId("100");
            person.setName("jack");
            System.out.println(person.getId + " : " + person.getName());
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch(InstantiationException e){
            e.printStackTrace();
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }
    }
}
//运行结果
100 : jack
```

# 实战2：通过反射获取对象字段属性，并且赋值

```Java
package com.app;

import java.lang.reflect.Field;

public class T9{
    public static void main(String[] args){
        try{
            //创建类
            Class clazz = Class.forName("com.app.Person");
            Person person = clazz.newInstance();
            //获得id属性
            Field id = clazz.getDeclaredField("id");
            //  idField.setAccessible( true );
            //给id属性赋值
            id.set(person,"100");
            //打印id的值
            System.out.println(id.get(person));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
```

运行结果

```
java.lang.IllegalAccessException: Class com.app.T1 can not access a member of class com.app.Person with modifiers "private"
    at sun.reflect.Reflection.ensureMemberAccess(Unknown Source)
    at java.lang.reflect.AccessibleObject.slowCheckMemberAccess(Unknown Source)
    at java.lang.reflect.AccessibleObject.checkAccess(Unknown Source)
    at java.lang.reflect.Field.set(Unknown Source)
    at com.app.T1.main(T1.java:20)
```


解锁上边代码中的`idField.setAccessible( true );`

结果

```
100
```

# 实战3：综合训练，反射操作属性和方法

```Java
package com.app;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class T10{
    public static void main(String[] args){
        try{
            //创建类
            Class clazz = Class.forName("com.app.Person");
            Person person = clazz.newInstance();
            //获得id属性
            Field id = clazz.getDeclaredField("id");
            idField.setAccessible( true );
            //给id属性赋值
            id.set(person,"100");
            //获取setName方法
            Method setName = clazz.getDeclaredMethod("setName",String.class);
            setName.setAccessible(true);
            setName.invoke(person,"jack");

            Field nameField = clazz.getDeclaredField("name");
            nameField.setAccessible(true);

             //打印 person 的 id 属性值
            String id_ = (String) id.get( person ) ;
            System.out.println( "id: " + id_ );

            //打印 person 的 name 属性值
            String name_ = ( String)nameField.get( person ) ;
            System.out.println( "name: " + name_ );
            
            //获取 getName 方法
            Method getName = clazz.getDeclaredMethod( "getName" ) ;
            //打破封装 
            getName.setAccessible( true );
            
            //执行getName方法，并且接收返回值
            String name_2 = (String) getName.invoke( person  ) ;
            System.out.println( "name2: " + name_2 );
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
```

运行结果

```
id: 100
name: jack
name2: jack
```

# 实战4： 静态属性、静态方法的调用

定义Util类

```Java
package com.app;

public class Util {
    public static String name = "json" ;
    /**
        * 没有返回值，没有参数
        */
    public static void getTips(){
        System.out.println( "执行了---------1111");
    }

    /**
        * 有返回值，没有参数
        */
    public static String getTip(){
        System.out.println( "执行了---------2222");
        return "tip2" ;
    }

    /**
        * 没有返回值，有参数
        * @param name
        */
    public static void getTip( String name ){
        System.out.println( "执行了---------3333 参数： " + name );
    }

    /**
        * 有返回值，有参数
        * @param id
        * @return
        */
    public static String getTip( int id ){
        System.out.println( "执行了---------4444 参数： " + id );
        if ( id == 0 ){
            return "tip1 444 --1 " ;
        }else{
            return "tip1 444 --2" ;
        }
    }

}
```

完整小例子：

```Java
package com.app;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class T11 {

    public static void main(String[] args) {

        try {
            //创建类
            Class<?> class1 = Class.forName("com.app.Util");

            //获取 nameField 属性
            Field nameField = class1.getDeclaredField( "name" ) ;
            //获取 nameField 的值
            String name_ = (String) nameField.get( nameField ) ;
            //输出值
            System.out.println( name_ );


            //没有返回值，没有参数
            Method getTipMethod1 = class1.getDeclaredMethod( "getTips"  ) ; 
            getTipMethod1.invoke( null  ) ;
            
            //有返回值，没有参数
            Method getTipMethod2 = class1.getDeclaredMethod( "getTip"  ) ; 
            String result_2 = (String) getTipMethod2.invoke( null  ) ;
            System.out.println( "返回值： "+ result_2 );
            
            //没有返回值，有参数
            Method getTipMethod3 = class1.getDeclaredMethod( "getTip" , String.class  ) ; 
            String result_3 = (String) getTipMethod3.invoke( null , "第三个方法"  ) ;
            System.out.println( "返回值： "+ result_3 );
            
            //有返回值，有参数
            Method getTipMethod4 = class1.getDeclaredMethod( "getTip" , int.class ) ; 
            String result_4 = (String) getTipMethod4.invoke( null  , 1 ) ;
            System.out.println( "返回值： "+ result_4 );
            
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace() ;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
```

