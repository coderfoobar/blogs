
# 快速排序中的Partition算法

> Create Time : 2017年7月5日 Ref : https://segmentfault.com/a/1190000006771980

如果你学习过算法，那么肯定听说过快速排序的大名，但是对于快速排序中用到的 partition 算法，你了解的够多吗？或许是快速排序太过于光芒四射，使得我们往往会忽视掉同样重要的 partition 算法。

Partition 可不只用在快速排序中，还可以用于 Selection algorithm（在无序数组中寻找第K大的值）中。甚至有可能正是这种通过一趟扫描来进行分类的思想激发 Edsger Dijkstra 想出了 Three-way Partitioning，高效地解决了 Dutch national flag problem 问题。接下来我们一起来探索 partition 算法。



```Java
// Do partition in arr[begin, end), with the first element as the pivot.
int partition(int[] arr, int begin, int end){
    int pivot = arr[begin];
    // Last position where puts the no_larger element.
    int pos = begin;
    for(int i = begin+1; i != end; i++){
        if(arr[i] <= pivot){
            pos++;
            if(i != pos){
                swap(arr[pos], arr[i]);
            }
        }
    }
    swap(arr[begin], arr[pos]);
    return pos;
}
```

```Java
int partition(int[] arr, int begin, int end)
{
    int pivot = arr[begin];
    while(begin < end)
    {
        while(begin < end && arr[--end] >= pivot);
        arr[begin] = arr[end];
        while(begin < end && arr[++begin] <= pivot);
        arr[end] = arr[begin];
    }
    arr[begin] = pivot;
    return begin;
}
```

```Java
public int partition(int[] arr , int start, int end) {
    int pivot = arr[start];
    while (true) {
        //右侧大于pivot
        while (start < end && arr[--end] > pivot) ;
        arr[start] = arr[end];
        //左侧小于等于pivot
        while (start < end && arr[++begin] <= pivot>);
        arr[end] = arr[start];
    }
    arr[start] = pivot;
    return start;
}
```

```Java
void quick_sort(int[] arr, int begin, int end){
    if(begin >= end - 1){
        return;
    }
    int pos = partition(arr, begin, end);

    quick_sort(arr, begin, pos);
    quick_sort(arr, pos+1, end);
}
```

