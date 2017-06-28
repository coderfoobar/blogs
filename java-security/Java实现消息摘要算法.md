
# Java 实现消息摘要算法

## 实现

* MD (Message Digest)
* SHA (Secure Hash Algorithm)
* MAC (Message Authentication Code)


## 用途

* 用于验证数据完整性
* 数字签名核心算法

## 消息摘要算法MD

* MD5
* MD家族（128位摘要信息）
    * MD2
    * MD4   （电驴）

特点比较 ：

| 算法 | 摘要长度 | 实现方 |
| --   | ------- | ----- |
| MD2 | 128 | JDK |
| MD4  | 128 | Bouncy Castle |
| MD5 | 128 | JDK |


## 编码实现

MD算法：

```Java
public class ImoocMD{
    
    private static final String src = "imooc security md";

    public static void main(String[] args) {

    }

    public static void jdkMD5 () {
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md.digest(src.getBytes());
            //将md5Bytes转换为16进制
            //cc 代表 commons codec
            System.out.println("jdk md5 : " + cc.Hex.encodeHexString(md5Bytes));
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    } 

    public static void jdkMD2 () {
        try{
            MessageDigest md = MessageDigest.getInstance("MD2");
            byte[] md2Bytes = md.digest(src.getBytes());
            //将md2Bytes转换为16进制
            //cc 代表 commons codec
            System.out.println("jdk md2 : " + cc.Hex.encodeHexString(md2Bytes));
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    } 

    public static void bcMD5(){
        Digest digest = new MD5Digest();
        digest.update(src.getBytes(), 0 , src.getBytes().length);
        byte[] md5Bytes = new bytes[digest.getDigestSize()];
        digest.doFinal(md5Bytes,0);
        System.out.println("bc md5: " + bc.Hex.toHexString(md5Bytes));
    }

    public static void bcMD4(){
        Digest digest = new MD4Digest();
        digest.update(src.getBytes(), 0 , src.getBytes().length);
        byte[] md4Bytes = new bytes[digest.getDigestSize()];
        digest.doFinal(md4Bytes,0);
        System.out.println("bc md4: " + bc.Hex.toHexString(md4Bytes));
    }

    public static void jdkBCMD4(){
        // 实验jdk bc 传入 MD5 看使用的是bc的还是jdk的。（通过md.getProvider()）
        try{
            Security.addProvider(new BouncyCastleProvider());
            MessageDigest md = MessageDigest.getInstance("MD4");
            byte[] md5Bytes = md.digest(src.getBytes());
            System.out.println("JDK Provider BC MD4: " + bc.Hex.toHexString(md5Bytes));
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    public static void ccMD5(){
        System.out.println("cc MD5" + DigestUtils.md5Hex(src.getBytes()));
    }

    public static void ccMD2(){
        System.out.println("cc MD2" + DigestUtils.md2Hex(src.getBytes()));
    }
}
```


MD应用

```

用户注册 -- 对密码进行消息摘要 -- 信息持久化  --- 返回注册结果
用户登录 -- 对密码进行消息摘要 -- 通过用户名及摘要查询 -- 返回登录结果

```

## 消息摘要算法 -- SHA

* 安全散列算法
* 固定长度摘要信息
* SHA-1,SHA-2 (SHA-224,SHA-256,SHA-384,SHA-512)

| 算法 | 摘要长度 | 实现方 | 
| ---- | ------- | ------ |
| SHA-1 | 160 | JDK |
| SHA-224 | 224 | Bouncy Castle |
| SHA-256 | 256 | JDK |
| SHA-384 | 384 | JDK |
| SHA-512 | 512 | JDK |


### 编码实现


```Java
public class ImoocSHA {

    private static final String src = "imooc security sha";
    public static void main(String[] args) {

    }

    public static void jdkSHA1() {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(src.getBytes());
            //cc 代表 commons codec
            System.out.println("jdk sha1: " + cc.Hex.encodeHexString(md.digest()));
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    public static void bcSHA1() {
        Digest digest = new SHA1Digest();
        digest.update(src.getBytes(),0,src.getBytes().length);
        byte[] sha1Bytes = new Bytes[digest.getDigestSize()];
        digest.doFinal(sha1Bytes,0);
        System.out.println("bc sha1 : " + bc.Hex.toHexString(sha1Bytes));
    }


    public static void bcSHA224(){
        Digest digest  = new SHA224Digest();
        digest.update(src.getBytes(),0,src.getBytes().length);
        byte[] sha224Bytes = new bytes[digest.getDigestSize().length];
        digest.doFinal(sha224Bytes,0);
        System.out.println("bc sha224 : " + bc.Hex.toHexString(sha224Bytes));
    }

    public static void bcjdkSHA224() {
        Security.addProvider(new BouncyCastleProvider());
        //md 算法类似的方式实现  练习 
        try{
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(src.getBytes());
            //cc 代表 commons codec
            System.out.println("jdk sha1: " + cc.Hex.encodeHexString(md.digest()));
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    

    public static void ccSHA1(){
        System.out.println("cc sha1 -1 : " + DigestUtils.sha1Hex(src.getBytes()));
        System.out.println("cc sha1 -2 : " + DigestUtils.sha1Hex(src));
    }
//练习， 384 512 
}
```

应用：

发送方 -- 公布消息摘要算法 -- 将待发布消息进行摘要处理 -- 发布摘要消息 -- 发送消息 -- （接收方）消息鉴别 

oauth2 ： 
1. 加入约定的key
2. 增加时间戳
3. 排序

* http://***?msg=12Hsd74mj&timestamp=1209488734
* msg : 原始消息+key+时间戳


## 消息摘要算法 -- MAC

Message Authentication Code 兼容了MD 和 SHA 

HMAC keyed-hash Message Authentication Code  含有密钥的散列函数算法

* 融合MD ， SHA
    * MD系列 ： HmacMD2,HmacMD4,HmacMD5
    * SHA系列 : HmacSHA1,HmacSHA224,HmacSHA256,HmacSHA384,HmacSHA512

| 算法 | 摘要长度 | 实现方 | 
| ---- | ------- | ------ |
| HmacMD2 | 128 | Bouncy Castle |
| HmacMD4 | 128 | Bouncy Castle |
| HmacMD5 | 128 | JDK |
| HmacSHA1 | 160 | JDK |
| HmacSHA224 | 224 | Bouncy Castle |
| HmacSHA256 | 256 | JDK |
| HmacSHA384 | 384 | JDK |
| HmacSHA512 | 512 | JDK |

```Java
public class ImoocHmac {
    public static void main(String[] args) {

    }

    public static void jdkHmacMD5(){
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacMD5"); //初始化keyGenerator
            SecretKey secretKey = keyGenerator.generateKey(); //产生密钥
//            byte[] key = secretKey.getEncoded();// 获取密钥
            byte[] key = Hex.decodeHex(new char[]{'a','a','a','a','a','a','a','a','a','a'});
            SecretKey restorekey = new SecretKeySpec(key,"HmacMD5");
            Mac mac = Mac.getInstance(restorekey.getAlgorithrm);
            mac.init(restorekey);//初始化Mac
            byte[] hmacMD5Bytes = mac.doFinal(src.getBytes());//执行摘要
            System.out.println("jdk hmacMD5 : " + Hex.encodeHexString(hmacMD5Bytes));
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void bcHmacMD5() {
        HMac hmac = new HMac(new MD5Digest());
        hmac.init(new KeyParameter(org.bouncycastle.util.encoders.Hex.decode("aaaaaaaaaa")));
        hmac.update(src.getBytes(),0,src.getBytes().length);
        byte[]  hmacMD5Bytes = new byte[hmac.getMacSize()];//执行清理
        hmac.doFinal(hmacMD5Bytes,0);
        System.out.println("bc hmacMD5 : " + org.bouncycastle.util.encoders.Hex.toHexString(hmacMD5Bytes));
    }

    
}
```


### 应用

发送方 -- 公布消息摘要算法 -- 构建密钥 -- 发送密钥 -- 对密码进行消息摘要 -- 发送消息摘要 -- 发送消息 -- 消息鉴别（接收方）

## 消息摘要算法 -- 其他

* RipeMD
* Tiger
* Whirlpool
* GOST3411

* Bouncy Castle 实现

























