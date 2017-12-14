
# TreeMap按value排序

> Create Time : 2017年12月14日  Ref : http://blog.csdn.net/u011734144/article/details/52384284

```Java
public class Testing {  
  
    public static void main(String[] args) {  
  
        HashMap<String,Long> map = new HashMap<String,Long>();  
        ValueComparator bvc =  new ValueComparator(map);  
        TreeMap<String,Long> sorted_map = new TreeMap<String,Long>(bvc);  
  
        map.put("A",99);  
        map.put("B",67);  
        map.put("C",67);  
        map.put("D",67);  
  
        System.out.println("unsorted map: "+map);  
  
        sorted_map.putAll(map);  
  
        System.out.println("results: "+sorted_map);  
    }  
}  
  
class ValueComparator implements Comparator<String> {  
  
    Map<String, Long> base;  
    //这里需要将要比较的map集合传进来
    public ValueComparator(Map<String, Long> base) {  
        this.base = base;  
    }  
  
    // Note: this comparator imposes orderings that are inconsistent with equals.    
    //比较的时候，传入的两个参数应该是map的两个key，根据上面传入的要比较的集合base，可以获取到key对应的value，然后按照value进行比较   
    public int compare(String a, String b) {  
        if (base.get(a) >= base.get(b)) {  
            return -1;  
        } else {  
            return 1;  
        } // returning 0 would merge keys  
    }  
}  
```