
# CountDownLatch - 的介绍和使用

> Create Time : 2017年4月4日 Ref : http://www.itzhai.com/the-introduction-and-use-of-a-countdownlatch.html#read-more

## 类介绍

一个同步辅助类，在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程在一直等待。

用给定的计数，初始化`CountDownLatch`。由于调用了`countDown()`方法，所以在当前计数到达零之前，`await`方法会一直受阻。之后，会释放所有等待的线程，`await`的所有后续调用都将立即返回。这种现象只出现一次 -- 计数无法被重置。如果需要重置计数，请考虑使用`CyclicBarrier`。

## 使用场景

在一些应用场合中，需要等待某个条件达到要求后，才能做后面的事情。同时当线程都完成后也会触发事件，以便进行后面的操作。这个时候就可以使用`CountDownLatch`。`CountDownLatch`最重要的方法是`countDown()`和`await()`，前者主要是倒数一次，后者是等待倒数到0，如果没有到达0，就只有阻塞等待了。

## 方法说明

### countDown

```Java
public void countDown()
```

递减锁寸器的计数，如果计数到达零，则释放所有等待的线程。如果当前计数大于零，则将计数减少，如果新的计数为零，出于线程调度目的，将重新启用所有的等待线程。

如果当前计数等于零，则不发生任何操作。

### await

```Java
public boolean await(long timeOut,TimeUnit unit) throws InterruptedException
```

使当前线程在锁存器倒计数至零之前一直等待，除非线程被中断或超出类指定的等待时间。如果当前计数为零，则方法立刻返回`true`值。

如果当前计数大于零，则出于线程调度目的，将禁用当前线程，且在发生下列三种情况之一前，该线程将一直处于休眠状态：
* 由于调用`countDown()`方法，计数到达零
* 其他某个线程中断当前线程
* 已超出制定的等待时间
如果计数器到达零，则该方法返回`true`值，

如果当前线程：
* 在进入此方法时已经设置了该线程的中断状态
* 在等待时被中断
则抛出`InterruptedException`,并且清除当前线程的已中断状态。如果超出了制定的等待时间，则返回值为`false`，如果改时间小于等于零，则此方法根本不会等待。

***参数：***
* `timeOut` - 要等待的最长时间
* `unit` - `timeOunt`参数的时间单位

***返回：***
如果计数到达零，则返回`true`；如果在计数到达零之前超过了等待时间，则返回`false`。

***抛出：***
* `InterruptedException` - 如果当前线程在等待时被中断

## 相关实例

```Java
public class CountDownLatchTest {
    public static void main(String[] args) throws InterruptedException 
    {
        final CountDownLatch begin = new CountDownLatch(1);

        final CountDownLatch end = new CountDownLatch(10);

        final ExecutorService exec = Executors.newFixedThreadPool(10);

        for (int i = 0 ; i < 10 ; i++ ) {
            final int NUM = i + 1 ; 

            Runnable run = new Runnable () {
                @Override
                public void run () {
                    try {
                        begin.await();
                        Thread.sleep((long)(Math.random() * 10000));
                        System.out.println("No." + NUM + " arrived");
                    } catch ( InterruptedException e ) {

                    } finally {
                        end.countDown();
                    }
                }
            };
            exe.submit ( run ) ;
        }
        System.out.println("Game Start");
        begin.countDown();
        end.await();
        System.out.println("Game Over");
        exe.shutdown();
    }
}
```



















