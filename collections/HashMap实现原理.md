
# HashMap实现原理

> Create Time : 2017年3月21日 Author : huduku.io

HashMap和HashTable的底层实现都是数组+链表结构实现的，这点上完全一致。

添加、删除、获取元素时都是先计算hash，根据hash和table.length计算index，也就是table数组的下标，然后进行相应的操作。

下面以HashMap为例，说明其实现：

```Java
/**
 * HashMap初始容量，必须为2的次幂
 **/
static final int DEFAULT_INITAL_CAPACITY = 16;

/**
 * HashMap容量的最大值
 **/
static final int MAXMUM_CAPACITY = 1 << 30;

/**
 * 默认的加载因子
 **/
static final float DEFAULT_LOAD_FACTOR = 0.75f;

/**
 * HashMap 用来存储数据的数组
 **/
transient Entry[] table;
```

## HashMap的创建

HashMap默认初始化时会创建一个默认容量为16的Entry数组，默认加载因子为0.75，同时设置临界值为16*0.75

```Java
/**
 * Constructs an empty <tt> HashMap </tt> with the default initial capacity (16) and the default load factor (0.75).
 **/
public HashMap(){
    this.loadFactor = DEFAULT_LOAD_FACTOR;
    threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    table = new Entry[DEFAULT_INITIAL_CAPACITY];
    init();
}
```

## put方法

HashMap会对值为null的key进行特殊处理，总是放到table[0]的位置。

put过程是先计算hash，然后通过hash与table.length取模计算index值，然后将key放到table[index]位置，当table[index]已经存在其他元素时，会在table[index]位置上形成一个链表，将新添加元素放在table[index]，原来的元素通过Entry的next进行链接，这样以链表形式解决hash冲突问题，当元素数量达到临界值（capacity * factor）时，则进行扩容，是table数组长度变为table.length * 2

```Java
public V put(K key , V value) {
    if (null == key){ //处理null值key
        return putForNullKey(value);
    }

    int hash = hash(key.hashCode());//计算hash
    int i = indexFor(hash , table.length); //计算在数组中存储位置
    
    //遍历table[i] 位置的链表，查找相同的key，若找到则使用新的value替换掉原来的oldValue并返回oldValue
    for (Entry<K,V> e = table[i] ; e != null ; e = e.next ) {
        Object k;
        if(e.hash == hash && ((k = e.key) == key || key.equals(k) ) ){
            V oldValue = e.value;
            e.value = value;
            e.recordAccess(this);
            return oldValue;
        }
    } 

    //若没有在table[i]位置找到相同的key，则添加key到table[i] 位置，新的元素总是在table[i]位置的第一个元素，原来的元素后移
    modCount++;
    addEntry(hash , key , value , i);
    return null;
}

void addEntry(int hash , K key , V value , int bucketIndex) { 
    //添加key到table[bucketIndex]位置，新元素总是在table[bucketIndex]的第一个元素，原来的元素后移
    Entry<K,V> e = table[bucketIndex];
    table[bucketIndex] = new Entry<K,V> (hash,key,value,e);
    //判断元素个数是否达到了临界值，若达到临界值则扩容，table长度翻倍
    if (size++ > threshold){
        resize (2 * table.length);
    }
}
```


