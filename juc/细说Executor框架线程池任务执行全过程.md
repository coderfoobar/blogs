
# 细说Executor框架线程池任务执行全过程(上)

> Create Time : 2017年2月19日  Ref ： http://www.infoq.com/cn/articles/executor-framework-thread-pool-task-execution-part-01/

## 前言

JDK5之后引入的Executor框架的最大有点是把任务的提交和执行解耦。要执行的人只需要 把Task描述清楚，然后提交即可。这个Task是怎么被执行的，被谁执行的，什么时候执行，提交的人就不用关心了。具体点讲，提交一个`Callable`对象给`ExecutorService`（如最常用的线程池`ThreadPoolExecutor`）,将得到一个`Future`对象，调用`Future`对象的`get`方法等待执行结果好了。

经过这样的封装，对于使用者老说，提交任务获取结果的过程大大讲话，调用者直接从提交的地方就可以等待获取执行结果。而封装最大的效果是使得真正执行任务的线程们变得不为人知。有没有觉得这个场景似曾相似？我们工作中当老大的老大（且称作LD^2）把一个任务交给我们老大（LD）的时候，到底是LD自己干，还是转过身来拉一帮苦逼的兄弟加班加点干，那LD^2是不管的。LD^2只用把人描述清楚提及给LD，然后喝着咖啡等着收LD的report即可。等LD一封邮件非常优雅的报告LD^2 report结果时，实际操作中是码农A和码农B干了一个月，还是码农ABCDE加班干了一个礼拜，大多是不用体现的。这套机制的优点就是LD^2找个合适的LD来提交任务即可，接口友好有效，不用为具体怎么干费神费力。

## 一个最简单的例子

看上去这个执行过程是这个样子，调用这段代码的是老大的老大了，他所需要干的所有事情就是找到一个合适的老大（如下面例子中的laodaA就荣幸的被选中了），提交任务就好了。

```Java
//一个有7个作业线程的线程池，老大的老大找到一个管7个人的小团队的老大
ExecutorService laodaA = Executors.newFixedThreadPool(7);
//提交作业给老大，作业内容封装在Callable中，约定好了输出类型是String
String outputs = laoda.submit(
    new Callable<String>(){
        public String call() throws Exception {
            return "I'm a task , which submited by the so called laoda , and run by those anonymous workers";
        }
        //提交后就等着结果吧，到底是手下7个作业中谁领到任务了，老大是不关心的。
    }
).get();
System.out.println(outputs);
```

使用上非常简单，其实只有两行语句来完成所有功能；创建一个线程池，提交任务并等待获取执行结果。

例子中，生成线程池采用了工具类`Executors`的静态方法，除了`newFixedThreadPool`可以生成固定大小的线程池，`newCachedThreadPool`可以生成一个无界、可以自动回收的线程池；`newSingleThreadScheduledExecutor`可以生成一个单个线程的线程池；`newScheduledThreadPool`还可以生成支持周期任务的线程池。 一般用户场景下各种不同设置要求的线程池可以这样生成，不用自己new一个线程池出来。

## 代码剖析

这套机制怎么用，上面两句语句就做到了，非常方便和友好。但是`submit`的`task`是怎么被执行的？是谁执行的？如何做到在调用的时候只有等待执行结束才能`get`到结果。这些都是JDK5之后`Executor`接口下的线程池、`Future`接口下的可获得执行结果的任务，配合AQS和原有的`Runnable`来做到的。在下文中我们尝试通过剖析每部分源代码来了解`Task`提交，`Task`执行，获取`Task`执行结果的主要步骤。为了控制篇幅，突出主要逻辑，文章中是以最常用的`ThreadPoolExecutor`线程池来举例。其实`ExecutorService`接口下定义了很多功能丰富的其他类型，有各自的特点，但风格类似。本文重点是介绍任务提交的过程，过程中设计的`ExecutorService`、`ThreadPoolExecutor`、`AQS`、`Future`、`FutureTask`等只会介绍该过程中用到的内容，不会对每个类都详细展开。

### 任务提交

从类图上可以看到，接口`ExecutorService`继承自`Executor`，不像`Executor`中只定义了一个方法来执行任务，在`ExecutorService`中，正如其名字暗示的一样，定义了一个服务，定义了完整的线程池的行为，可以接受提交任务、执行任务、关闭服务。抽象类`AbstractExecutorService`类实现了`ExecutorService`接口，也实现了接口定义的默认行为。

![](Executor-Class-UML.jpg)

`AbstractExecutorService`任务提交的`submit`方法有三个实现。第一个接收一个`Runnable`的`Task`，没有执行结果；第二个有两个参数：一个任务，一个执行结果；第三个一个`Callable`，本身包含执行任务内容和执行结果。`submit`方法的返回结果是`Future`类型，调用该接口定义的`get`方法即可获得执行结果。`V get() 方法的返回值类型V是在提交任务时就约定好了的`。

除了`submit`任务的方法外，作为对服务的管理，在`ExecutorService`接口中还定义了服务的关闭方法:`shutdown`和`shutdownNow`方法。，可以平缓或立即关闭执行服务，实现该方法的子类根据自身特征支持该定义。在`ThreadPoolExecutor`中，维护了`RUNNING`、`SHUTDOWN`、`STOP`、`TERMINATED`四种状态来实现对线程池的管理。线程池的完整运行机制不是本文的重点，重点还是关注`submit`过程中的逻辑。

1. 看`AbstractExecutorService`中代码提交部分，构造好了一个`FutureTask`对象，调用`execute()`方法执行任务。我们知道这个方法是顶级接口Executor中定义的最重要的方法。`FutureTask`类型实现了`Runnable`接口，因此满足`Executor`中`execute()`方法的约定。同时比较有意思的是，该对象在`execute`执行后，就又作为`submit`方法的返回值返回，因为`FutureTask`同时又实现了`Future`接口，满足`Future`接口的约定。

```Java
    public <T> Future<T> submit(Callable<T> task){
        if(null == task) 
            throw new NullPointerException();
        RunnableFuture<T> ftask = newTaskFor(task);
        execute(ftask);
        return ftask;
    }
```

2. `submit`传入参数都被封装成了`FutureTask`类型来`execute`的，对应前面三个不同参数类型都会封装成`FutureTask`。

```Java
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable){
        return new FutureTask<T>(callable);
    }
```

3. `Executor`接口中定义的`execute`方法的作用就是执行提交的任务，该方法在抽象类`AbstractExecutorService`中没有实现，留到子类中实现。我们观察下子类`ThreadPoolExecutor`，使用最广泛的线程池如何来`execute`那些`submit`的任务的。这个方法看起来比较简单，但是线程池`什么时候创建新的作业线程来处理任务`，`什么时候只接收任务不创建作业线程`，另外`什么时候拒绝任务`。线程池的`接收任务`，`维护工作线程的策略`都要在其中体现。

作为必要的预备只是，先补充下`ThreadPoolExecutor`有两个最重要的集合属性，分别是`存储接收任务的任务队列`和`用来干活的作业集合`。

```Java
    //任务队列
    private final BlockingQueue<Runnable> workQueue;
    //作业线程集合
    private final HashSet<Worker> workers = new HashSet<Worker>();
```

其中阻塞队列`workQueue`是用来存储待执行任务的，在构造线程池可以选择满足该`BlockingQueue`接口定义的`SynchronousQueue`、`LinkedBlockingQueue`或者`DelayedWorkQueue`等不同阻塞对垒来实现不同特征的线程池。

关注下`executor(Runnable command)`方法中调用的`addIfUnderCorePoolSize`,`workQueue.offer(command)`,`ensureQueuedTaskHandled(command)`,`addIfUnderMaximumPoolSize(command)`这几个操作。尤其是几个名字较长的`private`方法，把方法名的驼峰式的单词分开，加上对方法上下文的了解就能理解其功能。

因为前面说到的几个方法里面就是操作，又返回了一个布尔值，影响后面的逻辑，所以不大方便在方法体中为每天语句加注释来说明，需要大致关联起来看。所以首先需要把`execute`方法的主要逻辑说明下，再看其中各自方法的作用。

* 如果线程池的状态是`RUNNING`，线程池的大小小于配置的核心线程数，说明还可以创建新线程，则启动新的线程来执行这个任务。
* 如果线程池的状态是`RUNNING`，线程池的大小小于配置的最大线程数，并且任务队列已经满了，说明现有线程已经不能支持当前任务了，并且线程池还有继续扩充的空间，就可以创建一个新的线程来处理提交的任务。
* 如果线程池的状态是`RUNNING`，当前线程池的大小等于配置的核心线程数，说明根据配置当前的线程数已经够用，不用创建新线程，只需要把任务加入任务队列即可。如果任务队列不满，则提交的任务在任务队列中等待处理；如果任务队列满了，则需要考虑是否要扩展线程池的容量。
* 当线程池已经关闭或者上面的条件都不能满足时，则进行拒绝策略，拒绝策略在`RejectedExecutionHandler`接口中定义，可以有多种不同的实现。

上面其实也是对最主要思路的解析，详细展开可能还会更复杂。简单梳理下思路：构建线程池时定义了一个额定大小，当线程池内工作线程数小于额定大小，有新任务进来就创建新工作线程，如果超过该阈值，则一般就不创建了，只是把接收任务加到任务队列里面，但是如果任务队列里的任务实在太多了，那还是要申请额外的工作线程来帮忙。如果还是不够用，就拒绝服务。

这个场景其实也是每天我们工作中会碰到的场景。我们管人的老大，手里都有一定`HC ( Head Count )`，当上面有活分下来，手里人不够，但是不超过HC，我们就自己招人。如果超过了还是忙不过来，那就向上面老大申请借调人手来帮忙；如果还是干不完，那就没办法了，新任务咱就不接了。

```Java
public void execute(Runnable command){
    if(null == command){
        throw new NullPointerException();
    }

    if(poolSize >= corePoolSize || !addIfUnderCorePoolSize(command)){
        if(runState != RUNNING || poolSize == 0){
            ensureQueuedTaskHandled(command);
        }else if(!addIfUnderMaximumPoolSize(command)){
            reject(command); // is shutdown or saturated
        }
    }
}
```

4. `addIfUnderCorePoolSize`方法检查如果当前线程池的大小小于配置的核心线程数，说明还可以创建新线程，则启动新的线程执行这个任务。

```Java
private boolean addIfUnderCorePoolSize(Runnable firstTask){
    Thread t = null;
    //如果当前线程池的大小小于配置的核心线程数，说明还可以创建新线程
    if(poolSize < corePoolSize && runState == RUNNING){
        //启动新的线程执行这个任务
        t.addThread(firstTask);
    }
    return t != null;
}
```

5. 和上一个方法类似，`addIfUnderMaximumPoolSize`检查如果线程池的代销小于配置的最大线程数，并且任务队列已经满了（就是`execute`方法试图把当前线程加入任务队列时不成功），说明现有线程已经不能支持当前的任务了，但是线程池还有继续扩充的空间，就可以创建一个新的线程来处理提交的任务。

```Java
private boolean addIfUnderMaximumPoolSize(Runnable firstTask){
    //如果线程池的大小小于配置的最大线程数，并且任务队列已经满了（就是execute方法中试图把当前线程加入当前任务队列
    //workQueue.offer(command)）时候不成功，说明现有线程已经不能支持当前的任务了，但线程池还有继续扩充的空间。
    if(poolSize < maximumPoolSize && runState == RUNNING){
        //就可以创建一个新的线程来处理提交的任务
        t = addThread(firstTask);
    }

    return t != null;
}
```

6. 在`ensureQueuedTaskHandled`方法中，判断如果当前状态不是`RUNNING`，则当前任务不加入到任务队列中，判断如果状态是停止，线程数小于允许的最大数，切任务队列还不空，则加入一个新的工作线程到线程池来帮助还未处理完的任务。

```Java
private void ensureQueuedTaskHandled(Runnable command){
    //如果当前状态不是RUNNING，则当前任务不加入到任务队列中，判断如果状态是停止，线程数小于允许的最大书，且任务队列还不空
    if(state < STOP && poolSize < Math.max(corePoolSize,1) && !workQueue.isEmpty()){
        //则加入一个新的工作线程到线程池来帮助处理还未处理完的任务
        t = addThread(null);
    }
    if(reject){
        reject(command);
    }
}
```

7. 在前面的方法中都会调用`addThread`方法创建一个工作线程，差异是创建的有些工作线程上面关联接收到的任务`firstTask`，有些没有。该方法为当前接收到的任务`firstTask`创建`Worker`，并将`Worker`添加到作业集合`HashSet<Worker> workers`中，并启动作业。

```Java
private Thread addThread(Runnable firstTask){
    //为当前接收到的任务firstTask创建Worker
    Worker w = new Worker(firstTask);
    Thread t = threadFactory.newThread(w);
    w.thread = t;
    //将Worker添加到作业集合HashSet<Worker> workers中，并启动作业
    workers.add(w);
    t.start();
    return t;
}
```

至此，任务提交过程简单描述完毕，并介绍了任务提交后`ExecutorService`框架下线程池的主要应对逻辑，其实就是接收任务，根据需要创建我这维护管理线程。

维护这些工作线程干什么用？先不用看后面的代码，想想我们老大每月辛苦地把老板丰厚的薪水递到我们手里，定期还要领着大家出去happy下，又是定期的关心下个人生活，所有做的这些都是为什么呢？木讷的代码工不往这边使劲动脑子，但是猜还是能猜的到的，就让干活呗。本文想着重表达细节，诸如线程池里的Worker是怎么工作的，Task到底是不是在这些工作线程中执行的，如何保证执行完成后，外面等待任务的老大拿到想要结果，我们将在下篇文章中详细介绍。

http://www.infoq.com/cn/articles/executor-framework-thread-pool-task-execution-part-02


















































