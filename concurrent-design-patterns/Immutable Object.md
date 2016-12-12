
# [Immutable Object](https://en.wikipedia.org/wiki/Immutable_object) 模式

## 简介

>   In object-oriented and functional programming , an `immutable object` (unchangeable object) is an object whose state cannot be modified after it is created . This is contrast to a `mutable object` (changeable object) , which can be modified after it is created . In some cases, an object is considered immutable even if some internally used attributes change but the object's state appears to be unchanging from external point of view . For examble , an object that uses memoization to cache the results of expensive computations could still be considered an immutable objected.  

>  Strings and other concrete are typically expressed as immutable objects to improve readability and runtime efficiency in `object-orented programming`. Immutable objects are also useful because they are inherently `thread safe`. Other benefits are that they are simpler to understand and reason about and offer higher security that mutable objects.       

所谓状态不可变对象，即对象一经创建，其对外可见的状态就保持不变。例如Java中的String和Integer 