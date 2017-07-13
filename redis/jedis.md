
# jedis 

> Create Time : 2017年7月13日  Ref : http://www.cnblogs.com/liuling/p/2014-4-19-04.html

```Java
package com.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;

public class TestRedis {
    private Jedis jedis;

    @Before 
    public void setUp () {
        //连接到redis服务器 192.168.0.100:6379  
        jedis = new  Jedis("192.168.0.100" , 6379);
        //权限认证
        jedis.auth("admin");
    }

    /**
     * redis 存储字符串
     **/
    @Test
    public void testString () {
        //添加数据
        jedis.set("name","Rose"); //向key --> name 中放入了value --> Rose
        System.out.println(jedis.get("name")); //执行结果：Rose
        
        //拼接
        jedis.del("name"," is my lover!");
        System.out.println(jedis.get("name"));//Rose is my lover!

        //删除
        jedis.del("name");
        System.out.println(jedis.get("name"));

        //设置多个key
        jedis.mset("name","Jack" , "age","23","qq" , "476777XXX");
        jedis.incr("age");//进行加1操作
        System.out.println(jedis.get("name") + "-" + jedis.get("age") + "-" + jedis.get("qq"));
        jedis.incrby("age","10");//加10
        System.out.println(jedis.get("name") + "-" + jedis.get("age") + "-" + jedis.get("qq"));
    }

    /**
     * jedis 操作Map
     **/
    @Test 
    public void testMap () {
        //----- 添加数据 -----
        Map<String,String> map = new HashMap<String,String>();
        map.put("name","Rose");
        map.put("age", "22");
        map.put("qq" , "123456");
        jedis.hmset("user" , map);
        //取出user中的name，执行结果： Rose  注意结果是一个泛型的List
        //第一个参数是存入redis中map对象的key，后面跟的是放入map中的对象的key，后面的key可以跟多个，是可变参数
        List<String> rsmap = jedis.hmget("user","name","age","qq");
        System.out.println(rsmap);

        //删除map中的某个键值
        jedis.hdel("user","age");
        System.out.println(jedis.hmget("user","age"));//因为删除了，所以返回null
        System.out.println(jedis.hlen("user"));//返回key为user的键中存放的值的个数2
        System.out.println(jedis.exists("user"));//是否存在key为user的记录,返回true
        System.out.println(jedis.hkeys("user"));//返回map对象中所有的key
        System.out.println(jedis.hvals("user"));//返回map对象中所有的value
//        System.out.println(jedis.hexists("user","test"));//是key=user的记录中是否存在key为test的键值对,返回true

        Iterator<String> iter = jedis.hkeys("user").iterator();
        while(iter.hasNext()) {
            String key = iter.next();
            System.out.println(key + " : " + jedis.hmget("user",key));
        }
    }

    /**
     * jedis 操作list
     **/
    @Test
    public void testList () {
        //开始前，先移除所有内容
        String lkey = "java framework";
        jedis.del(lkey);
        System.out.println(jedis.lrange(lkey , 0 -1));
        //先向key java framework中存放三条数据
        jedis.lpush(lkey , "spring");
        jedis.lpush(lkey , "struts");
        jedis.lpush(lkey , "hibernate");
        //再取出所有数据jedis.lrange是按范围取出
        // 第一个是key，第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示区所有内容
        System.out.println(jedis.lrange(lkey, 0 -1));


        jedis.del(lkey);
        jedis.rpush(lkey,"spring");
        jedis.rpush(lkey,"struts");
        jedis.rpush(lkey,"hibernate");
        System.out.println(jedis.lrange(lkey,0 , -1));

        jedis.linsert(lkey,BinaryClient.LIST_POSITION.BEFORE,"spring","mybatis");
        jedis.linsert(lkey,BinaryClient.LIST_POSITION.AFTER,"stuts","cglib");

    }

    /**
     * jedis 操作set
     **/
    @Test
    public void testSet(){
        String skey = "user";
        //添加
        jedis.sadd(skey,"Jack");
        jedis.sadd(skey,"Rose");
        jedis.sadd(skey,"David");
        jedis.sadd(skey,"who");
        
        //移除who
        jedis.sdel(skey,"who");
        System.out.println(jedis.smembers(skey)); //返回所有加入的value
        System.out.println(jedis.sismember(skey,"who"));//判断who是否是user的元素
        System.out.println(jedis.srandmember(skey));//随机返回user中的一个元素
        System.out.println(jedis.scard(skey));//返回集合的元素个数

        String skey2 = "test";
        jedis.sadd(skey2,"Rose");
        jedis.sadd(skey2,"Jack");
        jedis.sadd(skey2,"1");
    }

    /** 
     * jedis操作zset
     **/
    @Test
    public void testZset () {
        //jedis 排序  
        //注意，此处的rpush和lpush是List的操作。是一个双向链表（但从表现来看的）  
        jedis.del("a");//先清除数据，再加入数据进行测试  
        jedis.rpush("a", "1");  
        jedis.lpush("a","6");  
        jedis.lpush("a","3");  
        jedis.lpush("a","9");  
        System.out.println(jedis.lrange("a",0,-1));// [9, 3, 6, 1]  
        System.out.println(jedis.sort("a")); //[1, 3, 6, 9]  //输入排序后结果  
        System.out.println(jedis.lrange("a",0,-1));  
    }

    @Test
    public void testRedisPool () {
        RedisUtil.getJedis.set("newname","中文测试");
        System.out.println(RedisUtil.getJedis().get("newname"));
    }

}
```


Redis 连接池

```Java

package com.test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class RedisUtil {
    //Redis服务器IP
    private static String IP = "192.168.0.100";
    //Redis访问端口
    private static int PORT = 6379;
    //访问密码
    private static String AUTH = "admin";
    //可用连接实例的最大数目，默认值为9
    //如果赋值为-1，则表示不限制，如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted（耗尽）
    private static int MAX_ACTIVE = 1024;
    //控制一个pool最多有多少个状态为idle（空闲）的jedis实例，默认值是8
    private static int MAX_IDLE = 200;
    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException
    private static MAX_WAIT = 10000;
    private static int TIMEOUT = 10000;
    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的
    private static boolean TEST_ON_BORROW = true;
    private static JedisPool jedisPool = null;

    /**
     * 初始化Redis连接池
     **/
    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxActive(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE); 
            config.setMaxWait(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config,IP,PORT,TIMEOUT,AUTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Jedis实例
     **/
    public synchronized static Jedis getJedis () {
        try {
            if (jedisPool != null) {
                Jedis resources = jedisPool.getResource() ;
                return resources;
            } 
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 释放Jedis资源
     **/
    public static void returnResource (final Jedis jedis) {
        if (null != jedis) {
            jedisPool.returnResource(jedis);
        }
    }
}
```