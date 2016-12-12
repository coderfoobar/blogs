

# java.util.Arrays

 这个类包含了多种不同的操纵数组（比如排序和查找）的方法。这个类也包含了一个允许将数组看作列表的静态工厂。
 
 假如数组的引用为`null`，则这个类中的方法都会抛出一个`NullPointerException`,除非另有说明。

 本类中所包含方法的文档包括了其实现的简要描述。这些说明应当被认作实现的笔记，而不是规范的一部分。实现者应该随时替换其他的算法，就像规范本身所述。（比如，`sort(Object[])`算法没有必要成为一个`MergeSort`,但是它确实必须势稳定的）。

 这个类是`Java Collections Framework`中的一员。

* public static void sort(type[] a)
> Sorts the specified array into ascending numerical orader.

> Implementation note: The sorting algorithm is a Dual-Pivot Quicksort by Valdimir Yaroslavskiy,Jon Bentley, and Joshua Bloch.This algorithm offers O(nlog(n)) performance on many data sets that cause other quicksorts to degrade to quadratic performance , and is typically faster than traditional (on-pivot) Quicksort implentations.
* public static void sort(type[] a ,int fromIndex, int toIndex)
> Sorts the specified range of the array into ascending order.The range to be sorted extends from the index fromIndex,inclusive , to the index toIndex,exclusive,If `fromIndex == toIndex` ,the range to be sorted is empty.

> Implementation note : The sorting algorithm is a Dual-Pivot Quicksort by Vladimir Yarostavskiy ,Jon Bentley , and Joshua Bloch.This algorithm offers O(nlog(n)) performance on many data sets that cause other quicksorts to degrade to quadratic performance, and is typocally faster than traditional (one-pivot) Quicksort implemntations.

* public static void sort(Object[] a)
> Sort the specified array of objects into ascending order,according to the natural ordering of its elements . All elements in the array must implements the `Comparable` interface.Furthermore, all elements in the array must be mutually comparable (that is , `e1.compareTo(T2)` must not throw a `ClassCastException` of any elements `e1` and `e2` in the array). 

> The sort is guaranteed to be ***`stable`*** : equal elements will not be recordered as a result of the sort.

> Implementation note : This implementation is a stable , adaptive , iterative mergesort that requires far fewer than `n` `lg(n)` comparisons when the input array is partially sorted, while offerring the performance of a traditional `mergesort` when the input array is randomly ordered . If the input array is neary sorted , the implementation requires approximately n comparisons. Temporary storage rquirements vary from a small constant for nearly sorted input arrays to `n/2` object references for randomly ordered input arrays.

> The implementation takes equal advantage of ascending and descending order in its input array, and can take advantage of ascending and descending order in different parts of the same input array . It is well-suited to merging two or more sorted arrays: simply concatenate the arrays and sort the resulting array.

> The implementation was adapted from Tim Peters's list sort for Python (TimSort) . It uses techiques from Peter Mcllroy's "Optimistic Sorting and Information Theoretic Complexity ", in Proceedings of the Fourth Annual ACM-SIAM Symposium on Discrete Algorithms , pp 467-474, January 1993.

* public static void sort(Object[] a, int fromIndex,int toIndex)

> Sorts the specified array of objects into ascending order , according to the natural ordering of its elements . The range to be sorted extends from index `fromIndex` , inclusive to Index `toIndex` , exlusive (If `fromIndex == toIndex` , the range to be sorted is empty . ) All elements in this range must implement the `Comparable` interface . Futhermore , all elements in this range must be *mutually* comparable (that is , `e1.compareTo(e2)` must not throw a `ClassCastException` for any elements , `e1` and `e2` in the array).

> This sort is guaranteed to be *stable* : equal elements will not be reordered as a result of the sort . 

> Implementation note : This implementation is a stable , adaptive , iterative `mergesort` that requires far fewer than `n lg(n)` comparisons when the input array is partially


