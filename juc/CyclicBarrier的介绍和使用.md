
# CyclicBarrier的介绍和使用

> Create Time : 2017年4月4日 Ref : http://www.itzhai.com/the-introduction-and-use-of-cyclicbarrier.html

## 类说明

一个同步辅助类，它允许一组线程相互等待，知道到达某个公共屏障点(Common Barrier Point)。在涉及一组固定大小的线程的程序中，这些线程必须不时地互相等待，此时CyclicBarrier很有用。因为该barrier在释放等待线程后可以重用，所以称它为循环的barrier。

## 使用场景

需要所有的子任务都完成时，才执行主任务，这个时候就可以选择使用CyclicBarrier。

## 常用方法

### await

```
public int await() throws InterruptException, BrokenBarrierException
```

在所有参与者都已经在此barrier上调用`await`方法之前，将一直等待。如果当前线程不是将到达的最后一个线程，处于调度目的，将禁用它，且在发生以下情况之一前，该线程将一直处于休眠状态：

* 最后一个线程到达
* 其他某个线程中断当前线程
* 其他某个线程中断另一个等待线程
* 其他某个线程在等待barrier时超时
* 其他某个线程在此barrier上调用`reset()`。

如果当前线程：

* 在进入此方法时已经设置了该线程的终端状态，或者
* 在等待时被终端

则抛出`InterruptedException`,并且清除当前线程的已中断状态。如果在线程处于等待状态时barrier被`reset()`，或者在调用`await`时barrier被损坏，抑或任意一个线程正处于等待状态，则抛出`BrokenBarrierException`异常。

如果任何线程在等待时被中断，则其他所有等待线程都将抛出`BrokenBarrierException`异常，并将barrier置于损坏状态。

如果当前线程是最后一个将要到达的线程，并且构造方法中提供了一个非空屏障操作，则在允许其他线程继续运行之前，当前线程将运行该操作。如果在执行屏障操作过程中发生异常，则该一场将传播到当前线程中，并barrier置于损坏状态。

### 返回

到达当前线程的索引，其中，索引`getParties() - `指示将到达的第一个线程，零指示最后一个到达的线程

### 抛出

* `InterruptedException` - 如果当前线程在等待时被中断 
* `BrokenBarrierException` - 如果另一个线程在当前线程等待时被中断或超时，或者重置了barrier，或者在调用`await`时barrier被损坏，抑或由于一场而导致屏障操作（如果存在）失败。

## 相关实例

赛跑时，等待所有人都准备好时，才起跑：

```Java
public class CyclicBarrierTest{
    public static void main(String[] args) {
        CylicBarrier barrier =  new CyclicBarrier(3);

        ExecutorService executer = Executers.newFixedThreadPool(3);
        executor.submit(new Thread(new Runner(barrier,'1号选手')));
        executor.submit(new Thread(new Runner(barrier,'2号选手')));
        executor.submit(new Thread(new Runner(barrier,'3号选手')));

        executor.shutdown();
    }

    class Runner implements Runnable {
        private CyclicBarrier barrier ; 
        private String name;

        public Runner(CylicBarrier barrier , String name) {
            super();
            this.barrier = barrier;
            this.name = name;
        }

        @Override
        public void run() {
            try {
                Thread.sleep( 1000 * (new Random()).nextInt(8));
                System.out.println(name + "准备好了......");
                //barrier的await方法，在所有参与者都已经在此barrier上调用await方法之前，将一直等待。
                barrier.await();
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            } catch ( BrokenBarrierException e ) {
                e.printStackTrace();
            }

            System.out.println(name = "起跑!");
        }
    }
}
```

注意 ： `ExecutorService`中的`submit`和`execute`方法的区别。
