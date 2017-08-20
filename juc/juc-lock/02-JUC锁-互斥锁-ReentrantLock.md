
# JUC锁 - 互斥锁 ReentrantLock

> Create Time : 2017年8月20日 Ref : http://www.cnblogs.com/skywang12345/p/3496101.html

## ReentrantLock介绍

> ReentrantLock是一个可重入的互斥锁，又称为“独占锁”。

顾名思义，ReentrantLock锁在`同一个时间点，只能被一个线程锁持有`；可重入的意思是，ReentrantLock锁，可以被`单个线程多次获取`。

ReentrantLock分为“公平锁”和“非公平所”。它们的区别体现在获取锁的机制上是否公平。

“锁”是为了保护竞争资源，防止多个线程同时操作线程而出错，ReentrantLock在同一时间点只能被一个线程获取（当某个线程获取到“锁”时，其它线程就必须等待）；ReentrantLock是通过一个FIFO的等待队列来管理获取该锁所有线程的。在“公平锁”的机制下，线程依次排队获取锁；而“非公平锁”在锁是可获取状态时，不管自己是不是在队列的开头都会获取锁。

## ReentrantLock函数列表

```Java
//创建一个ReentrantLock，默认是“非公平锁”
ReentrantLock()
//创建策略是fair的ReentrantLock，fair为true表示是公平锁，fair为false表示是非公平所。
ReentrantLock(boolean fair)

//查询当前线程保持此锁的次数。
int getHoldCound()
//返回目前拥有此锁的线程，如果此锁不被任何线程拥有，则返回null
protected Thread getOwner()
//返回一个collection，它包含可能正在等待获取此锁的线程
protected Collection<Thread> getQueuedThreads()
//返回正等待获取此锁的线程估计数
int getQueueLength()
//返回一个collection，它包含可能正在等待与此锁相关给定条件的那些线程。
protected Collection<Thread> getWaitingThreads(Condition condition)
//返回等待与此锁相关给定条件的线程估计数
int getWaitQueueLength(Condition condition)
//查询给定线程是否正在等待获取此锁
boolean hasQueuedThread(Thread thread)
//查询是否有些线程正在等待获取此锁
boolean hasQueuedThreads()
//查询是否有些线程正在等待与此锁有关的给定条件
boolean hasWaiters(Condition condition)
//如果是“公平锁”返回true，否则返回false
boolean isFair()
//查询当前线程是否保持此锁
boolean isHeldByCurrentThread()
//查询此锁是否由任意线程保持
boolean isLocked()
//获取锁
void lock()
//如果当前线程未被中断，则获取锁
void lockInterruptibly()
//返回用来与此Lock实例一起使用的Condition实例
Condition newCondition()
//仅在调用时锁未被另一个线程保持的情况下，才获取该锁
boolean tryLock()
//如果锁在给定等待时间内没有被另一个线程保持，且当前线程未被终端，则获取该锁
boolean tryLock(long timeout,TimeUnit unit)
//试图释放此锁
void unlock()
```

## ReentrantLock示例

通过对比“示例1”和“示例2”，我们能够清晰的认识lock和unlock的作用

> 示例1

```Java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//仓库
class Depot {
    private int size;//仓库的实际数量
    private Lock lock;//独占锁

    public Depot() {
        this.size = 0;
        this.lock = new ReentrantLock();
    }

    public void produce(int val) {
        lock.lock();
        try{
            size += val;
            System.out.printf("%s produce (%d) --> size=%d\n",Thread.currentThread().getName(),val,size);
        }finally{
            lock.unlock();
        }
    }

    public void consume(int val) {
        lock.lock();
        try{
            size -= val;
            System.out.printf("%s consume(%d) <-- size=%d\n",Thread.currentThread().getName,val,size);
        }finally{
            lock.unlock();
        }
    }
}

//生产者
class Producer {
    private Depot depot;

    public Producer(Depot depot) {
        this.depot = depot;
    }

    //生产产品，新建一个线程向仓库中生产产品
    public void produce(final int val) {
        new Thread() {
            public void run() {
                depot.produce(val);
            }
        }.start();
    }
}

class Consumer {
    private Depot depot ;

    public Consumer (Depot depot) {
        this.depot = depot;
    }

    //消费产品：新建一个线程从仓库中消费产品
    public void consumer (final int val) {
        new Thread() {
            public void run() {
                depot.consume(val);
            }
        }.start();
    }
}

public class LockTest1{
    public static void main(String[] args) {
        Depot mDepot = new Depot();
        Producer mPro = new Producer(mDepot);
        Consumer mCon = new Consumer(mDepot);

        mPro.produce(60);
        mPro.produce(120);
        mCon.consume(90);
        mCon.consume(150);
        mPro.produce(110);
    }
}
```

* 运行结果:

```
Thread-0 produce(60) --> size=60
Thread-1 produce(120) --> size=180
Thread-3 consume(150) <-- size=30
Thread-2 consume(90) <-- size=-60
Thread-4 produce(110) --> size=50
```

* 结果分析：
    1. Depot是个仓库。通过produce()能往仓库中生产货物，通过consumer()能消费仓库中的货物。通过独占锁lock实现对仓库的互斥访问：在操作（生产/消费）仓库中货品前，会先通过lock()锁住仓库，操作逻辑放在try块中，操作完之后，再在finally中通过unlock()解锁。
    2. Producer是生产者类。调用Producer中produce()函数可以新建一个线程往仓库中生产产品。
    3. Consumer是消费者类。调用Customer中consumer()函数可以新建一个线程消费仓库中的产品。
    4. 在主线程main中，我们会新建1个生产者mPro，同时新建1个消费者mCon。他们分别向仓库中生产/消费产品。

    根据main中的生产/消费数量，仓库最终剩余的产品应该是50。运行结果是符合我们预期的！

这个模型存在两个问题：
1. 现实中，仓库的容量不可能为负数。但是，此模型中的仓库容量为负数，这与现实相矛盾！
2. 现实中，仓库的容量是有限制的。但是，此模型中的容量确实没有限制的。

这两个问题，我们稍微会讲解到如何解决。现在，先看个简单的示例2，通过对比示例1和示例2，我们能更清晰的认识lock(),unlock()的用途。

> 示例2

```Java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//仓库
class Depot {
    private int size; //仓库的实际数量
    private Lock lock;

    public Depot () {
        this.size = 0;
        this.lock = new ReentrantLock();
    }

    public void produce(int val) {
        //lock.lock();
        //try{
            size += val;
            System.out.printf("%s produce (%d) --> size=%d",Thread.currentThread.getName(),val,size);
        //}catch(InterruptedException e){
        //
        //}finally{
        //    lock.unlock();
        //}
    }

    public void consume(int val) {
//        lock.lock();
//        try{
            size -= val;
            System.out.printf("%s consume(%d) <-- size=%d\n",Thread.currentThread().getName,val,size);
//        }finally{
//            lock.unlock();
//        }
    }
}

//生产者
class Producer {
    private Depot depot;

    public Producer(Depot depot) {
        this.depot = depot;
    }

    //生产产品，新建一个线程向仓库中生产产品
    public void produce(final int val) {
        new Thread() {
            public void run() {
                depot.produce(val);
            }
        }.start();
    }
}

//消费者
class Consumer {
    private Depot depot ;

    public Consumer (Depot depot) {
        this.depot = depot;
    }

    //消费产品：新建一个线程从仓库中消费产品
    public void consumer (final int val) {
        new Thread() {
            public void run() {
                depot.consume(val);
            }
        }.start();
    }
}

public class LockTest2{
    public static void main(String[] args) {
        Depot mDepot = new Depot();
        Producer mPro = new Producer(mDepot);
        Consumer mCon = new Consumer(mDepot);

        mPro.produce(60);
        mPro.produce(120);
        mCon.consume(90);
        mCon.consume(150);
        mPro.produce(110);
    }
}
```

* (某一次)运行结果：

```
Thread-0 produce(60) --> size=-60
Thread-4 produce(110) --> size=50
Thread-2 consume(90) <-- size=-60
Thread-1 produce(120) --> size=-60
Thread-3 consume(150) <-- size=-60
```

* 结果说明

示例2在示例1的基础上去掉了lock锁，在示例2中，仓库中最终剩余的产品是-60，而不是我们期望的50.原因是我们没有实现对仓库的互斥访问。

> 示例3

在示例3中，我们通过Condition去解决示例1中的两个问题：“仓库的容量不可能成为负数”以及“仓库的容量有限制”。

解决该问题是通过Condition。Condition是需要和Lock联合使用的。通过Codnition的await()方法，能让线程阻塞[类似于wait()]。通过Condition的signal()方法，能唤醒线程[类似于notify()]。

```Java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

//仓库
class Depot {
    private int capacity ; //仓库容量
    private int size;//仓库的实际数量
    private Lock lock; //独占锁
    private Condition fullCondition; //生产条件
    private Condition emptyCondition;//消费条件

    public Depot (int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.lock = new ReentrantLock();
        this.fullCondition = lock.newCondition();
        this.emptyCondition = lock.newCondition();
    }

    public void produce(int val) {
        lock.lock();
        try {
            //left 表示“想要生产的数量”（有可能生产量太多，需多此生产）
            int left = val;
            while (left > 0) {
                // 库存已满时，等待“消费者”消费产品。
                while (size >= capacity) {
                    fullCondition.await();
                }

                //获取“实际生产数量”（即库存中新增的数量）
                //如果“库存”+“想要生产的数量”>“总的容量”，则“实际容量”=“总的容量”-“当//前容量”(此时仓库已满)；否则“实际增量”=“想要生产的数量”
                int inc = (size + left) > capacity ?(capacity - size) : left;
                size += inc;
                left -= inc;
                System.out.printf("%s produce (%3d) --> left=%3d,inc=%3d,size=%3d\n",Thread.currentThread().getName(),val,left,inc,size);
                //通知“消费者”可以消费了
                emptyCondition.signal();
            }
        }catch(InterruptedException e){
        }finally {
            lock.unlock();
        }
    }

    public void consume(int val) {
        lock.lock();
        try {
            //left 表示“客户消费税护粮”（有可能消费量太大，库存不够，需多次消费）
            int left = val;
            while (left > 0){
                while (size < 0) {
                    emptyCondition.await();
                }
                //获取“实际消费的数量”（即库存中实际减少的数量）
                //如果“库存”<“客户要消费的数量”，则“实际消费量”=“库存”
                //否则，“实际消费量”=“客户要消费的数量”
                int dec = (size < left) ? size : left;
                size -= dec;
                left -= dec;
                System.out.printf("%s consume(%3d) <-- left=%3d, dec=%3d, size=%3d\n",Thread.currentThread().getName(),val,left,dec,size);
                fullCondition.signal();
            }
        }catch(InterruptedException e){
        }finally {
            lock.unloc();
        }
    }

    public String toString(){
        return "capacity:" + capacity + ", actual size:" + size;
    }
}

//生产者
class Producer {
    private Depot depot;
    public Producer (Depot depot) {
        this.depot = depot;
    }

    //生产产品:新建一个线程向仓库中生产产品
    public void produce(final int val) {
        new Thread() {
            public void run() {
                depot.produce(val);
            }
        }.start();
    }
}

//消费者
class Consumer {
    private Depot depot;

    public Consumer(Depot depot) {
        this.depot = depot;
    }

    //消费产品：新建一个线程从仓库中消费产品
    public void consume(final int val) {
        new Thread() {
            public void run() {
                depot.consume(val);
            }
        }.start();
    }
}

public class LockTest3 {
    public static void main(String[] args) {
        Depot mDepot = new Depot(100);
        Producer mPro = new Producer(mDepot);
        Consumer mCon = new Consumer(mDepot);

        mPro.produce(60);
        mPro.produce(120);
        mCon.consume(90);
        mCon.consume(150);
        mPro.produce(110);
    }
}
```

* (某一次)运行结果：

```
Thread-0 produce( 60) --> left=  0, inc= 60, size= 60
Thread-1 produce(120) --> left= 80, inc= 40, size=100
Thread-2 consume( 90) <-- left=  0, dec= 90, size= 10
Thread-3 consume(150) <-- left=140, dec= 10, size=  0
Thread-4 produce(110) --> left= 10, inc=100, size=100
Thread-3 consume(150) <-- left= 40, dec=100, size=  0
Thread-4 produce(110) --> left=  0, inc= 10, size= 10
Thread-3 consume(150) <-- left= 30, dec= 10, size=  0
Thread-1 produce(120) --> left=  0, inc= 80, size= 80
Thread-3 consume(150) <-- left=  0, dec= 30, size= 50
```

代码中已经包含了很消息的注释，这里就不再说明了。
更多“生产者/消费者模型”的内容，可以参考：[Java多线程系列--“基础篇”11之 生产消费者问题](http://www.cnblogs.com/skywang12345/p/3480016.html)

而关于Condition的内容，在后面我们会详细介绍。







