
# Java锁的种类以及辨析

> Create Time : 2017年2月6日   Link ： http://ifeve.com/java_lock_see/

锁作为并发共享数据，保证一致性的工具，在Java平台有多种实现（如synchronized和ReentranLock等等）。这些已经写好提供的所为我们开发提供了便利，但是锁的具体性质和类型却很少被提及。

## 自旋锁

自旋锁是采用让当前线程不停地在循环体内执行实现的，当循环的条件被其他线程改变时，才能进入临界区。如下:
```Java
public class SpinLock{
    private AtomicReference<Thread> sign = new AtomicReference<>();

    public void lock(){
        Thread current = Thread.currentThread();
        while(!sign.compareAndSet(null , current)){

        }
    }

    public void unlock(){
        Thread current = Thread.currentThread();
        sign.compareAndSet(current , null);
    }
}
```

使用了CAS原子操作，lock()函数将owner设置为当前线程，并且预测原来的值为空。unlock()函数owner设置为null，并且预测值为当前线程。

当有第二个线程调用lock()操作时由于owner值不为空，导致循环一直被执行，直至第一个线程调用unlock()函数将owner设置为null，第二个线程才能进入临界区。

由于自旋锁只是将当前县城不停执行循环体，不进行线程状态的改变，所以响应速度更快，但当线程数不停增加时，性能下降明显，因为每个线程都需要执行，占用CPU时间。如果线程竞争不激烈，并且保持锁的时间段。适合使用自旋锁。

> 注：该例子为非公平锁，获得锁的先后顺序不会按照进入lock的先后顺序执行。


## 自旋锁的其他种类

在自旋锁中，另有三种常见的锁形式：TicketLock ， CLHlock 和 MCSlock

```Java
package com.alipay.titan.dcc.dal.entity;

import java.util.concurrent.atomic.AtomicInteger;

public class TicketLock {

    private AtomicInteger serviceNum = new AtomicInteger();
    private AtomicInteger ticketNum  = new AtomicInteger();

    private static final ThreadLocal<Integer> LOCAL = new ThreadLocal<Integer>();

    public void lock(){
        int myticket = ticketNum.getAndIncrement();
        LOCAL.set(myticket);
        while(myticket != serviceNum.get()){

        }
    }

    public void unlock(){
        int myticket = LOCAL.get();
        serviceNum.compareAndSet(myticket , myticket + 1);
    }
}
```

每次都要查询一个serviceNum服务号，影响性能（必须要到内存读取，并阻止其他CPU修改）。

`CLHLock`和`MCSLock`则是两种类型相似的公平锁，采用链表的形式进行排序：

```Java
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class CLHLock{
    public static class CLHNode{
        private volatile boolean isLocked = true;
    }

    private volatile CLHNode tail;

    private static final ThreadLocal<CLHNode>() LOCAL = new ThreadLocal<CLHNode>();

    private static final AtomicReferenceFieldUpdater<CLHLock,CLHNode> UPDATER = 
            AtomicReferenceFieldUpdater.newUpdater(CLHLock.class,"tail");

    public void lock(){
        CLHNode node = new CLHNode();
        LOCAL.set(node);
        CLHNode preNode = UPDATER.getAndSet(this,node);
        if(null != preNode){
            while(preNode.isLocked){   }
            preNode = null;
            LOCAL.set(node);
        }
    }

    public void unloak(){
        CLHNode node = LOCAL.get();
        if(!UPDATER.compareAndSet(this,node,null)){
            node.isLocked = false;
        }
        node = null;
    }
}
```

CLHlock 是不停的查询前驱变量，导致不适合在NUMA架构下使用（在这种结构下，每个线程分布在不同的物理内存区域）MCSLock则是对本地变量的节点进行循环。不存在CLHLock的问题。

```Java
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class MCSLock {
    public static class MCSNode {
        volatile MCSNode next;
        volatile boolean isLocked = true;
    }

    private static final ThreadLocal<MCSNode> NODE    = new ThreadLocal<MCSNode>();
    @SuppressWarnings("unused")
    private volatile MCSNode                  queue;
    private static final AtomicReferenceFieldUpdater<MCSLock, MCSNode> UPDATER = AtomicReferenceFieldUpdater.newUpdater(MCSLock.class,
                                                                                   MCSNode.class, "queue");

    public void lock() {
        MCSNode currentNode = new MCSNode();
        NODE.set(currentNode);
        MCSNode preNode = UPDATER.getAndSet(this, currentNode);
        if (preNode != null) {
            preNode.next = currentNode;
            while (currentNode.isLocked) {

            }
        }
    }

    public void unlock() {
        MCSNode currentNode = NODE.get();
        if (currentNode.next == null) {
            if (UPDATER.compareAndSet(this, currentNode, null)) {

            } else {
                while (currentNode.next == null) {
                }
            }
        } else {
            currentNode.next.isLocked = false;
            currentNode.next = null;
        }
    }
}
```

从代码上看，CLH要比MCS更简单。CLH的队列是隐式的队列，有真实的后继节点属性。JUC ReentrantLock默认使用的锁是CLH锁（有很多改进的地方，将自旋锁换成了阻塞锁等等）。

## 阻塞锁

> 阻塞锁，与自旋锁不同，改变了线程的运行状态。

在Java中，线程Thread有如下几个状态：
* 新建状态
* 就绪状态
* 运行状态
* 阻塞状态
* 死亡状态

阻塞锁，可以说是让进程进入阻塞状态进行等待，当获得相应的信号（唤醒，时间）时，才可以进入线程的准备就绪状态，准备就绪状态的所有线程，通过竞争，进入运行状态。

Java中，能够进入/退出、阻塞状态或包含阻塞锁的方法有，synchronized关键字(其中的重量锁)， ReentrantLock，Object.wait()/notify()，LockSupport.park()/unpark()(j.u.c经常使用);

下面是一个Java阻塞锁的实例：

```Java
package lock;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.LockSupport;

public class CLHLock1 {
    public static class CLHNode {
        private volatile Thread isLocked;
    }

    @SuppressWarnings("unused")
    private volatile CLHNode                    tail;
    private static final ThreadLocal<CLHNode>   LOCAL   = new ThreadLocal<CLHNode>();
    private static final AtomicReferenceFieldUpdater<CLHLock1, CLHNode> UPDATER 
            = AtomicReferenceFieldUpdater.newUpdater(CLHLock1.class,CLHNode.class, "tail");

    public void lock() {
        CLHNode node = new CLHNode();
        LOCAL.set(node);
        CLHNode preNode = UPDATER.getAndSet(this, node);
        if (preNode != null) {
            preNode.isLocked = Thread.currentThread();
            LockSupport.park(this);
            preNode = null;
            LOCAL.set(node);
        }
    }

    public void unlock() {
        CLHNode node = LOCAL.get();
        if (!UPDATER.compareAndSet(this, node, null)) {
            System.out.println("unlock\t" + node.isLocked.getName());
            LockSupport.unpark(node.isLocked);
        }
        node = null;
    }
}
```

在这里，我们使用了LockSuppert.unpark()的阻塞锁。钙离子是将CLH锁修改而成。

阻塞所的优势在于，阻塞的线程不会占用CPU时间，不会导致CPU占用率过高，但进入时间以及恢复时间都要比自旋锁略慢。

在竞争激烈的情况下，阻塞所的性能要明显高于自旋锁。理想的情况是：在线程竞争不激烈的情况下，使用自旋锁；在竞争激烈的情况下，使用阻塞锁。

## 可重入锁

本文讲的是广义上的可重入锁，而不是单指Java下的ReentrantLock。

> 可重入锁，也叫递归锁，指的是同一线程 外层函数获得锁之后，内层递归函数仍然能获取该锁的代码，但不受影响。

在Java环境下ReentrantLock和synchronized都是可重入锁。

下面使用实例：
```Java
public class Test implements Runnable{
	public synchronized void get(){
		System.out.println(Thread.currentThread().getId());
		set();
	}

	public synchronized void set(){
		System.out.println(Thread.currentThread().getId());
	}

	@Override
	public void run() {
		get();
	}
	public static void main(String[] args) {
		Test ss=new Test();
		new Thread(ss).start();
		new Thread(ss).start();
		new Thread(ss).start();
	}
}
```

```Java
import java.util.concurrent.locks.ReentrantLock;

public class Test implements Runnable {
	ReentrantLock lock = new ReentrantLock();
	public void get() {
		lock.lock();
		System.out.println(Thread.currentThread().getId());
		set();
		lock.unlock();
	}

	public void set() {
		lock.lock();
		System.out.println(Thread.currentThread().getId());
		lock.unlock();
	}

	@Override
	public void run() {
		get();
	}

	public static void main(String[] args) {
		Test ss = new Test();
		new Thread(ss).start();
		new Thread(ss).start();
		new Thread(ss).start();
	}
}
```

两个例子的输出结果都是正确的，即 同一个线程id 被连续输出两次。

结果如下：
```txt
11
11
13
13
12
12
```

可重入锁最大的作用是避免死锁。我们可以 以自旋锁作为例子：
```Java
public class SpinLock{
    private AtomicReference<Thread> owner = new AtomicReference<Thread>();
    public void lock(){
        Thread current = Thread.currentThread();
        while(!owner.compareAndSet(null,current)){     }
    }

    public void unlock(){
        Thread current = Thread.currentThread();
        owner.compareAndSet(current,null);
    }
}
```

对于自旋锁来说：
1. 若有同一线程两次调用lock()，会导致第二次调用lock位置进行自旋，产生了死锁。说明则个锁并不是可重入的。（在lock函数内，应验证线程是否为已经获得锁的线程。）
2. 若1的问题已经解决，当unlock函数第一次调用时，就已经将锁释放了，而实际上不应该释放锁。（采用计数次进行统计）修改之后，如下：
```Java
public class SpinLock1 {
	private AtomicReference<Thread> owner =new AtomicReference<>();
	private int count =0;
	public void lock(){
		Thread current = Thread.currentThread();
		if(current==owner.get()) {
			count++;
			return ;
		}

		while(!owner.compareAndSet(null, current)){

		}
	}
	public void unlock(){
		Thread current = Thread.currentThread();
		if(current==owner.get()){
			if(count!=0){
				count--;
			}else{
				owner.compareAndSet(current, null);
			}

		}

	}
}
```

该自旋锁即位可重入锁。



## 互斥锁

## 悲观锁

## 公平锁

## 非公平锁

## 偏向锁

## 对象锁

## 线程锁

## 锁粗化

## 轻量级锁

## 锁消除

## 锁膨胀

## 信号量























