
# Java实现对称加密算法

> Create Time ： 2017年6月28日  Author ： huduku.io Ref : imooc

## 对称加密算法

* 初等
* DES
    * 3DES
* AES
* PEB
* IDEA

## 对称加密算法 - DES

* DES （Data Encryption Standard） 数据加密标准

| 密钥长度 | 默认 | 工作模式 | 填充方式 | 实现方 |
| -------- | --- | ------- | -------- | ----- |
| 56 | 56 | ECB,CBC,PCBC,CTR,CTS,CFB,CFB8到128 ， OFB, OFB8到128 | NoPadding , PKCS5Padding , ISO10126Padding | JDK |
| 64 | 56 | 同上 | PKCS7Padding , ISO10126d2Padding , X932Padding , ISO7816d4Padding , ZeroBytePadding | BC |

* 源码

```Java
public class ImoocDES {
    private static String src = "imooc security DES";
    public static void main(String[] args) {

    }

    public static void jdkDES () {
        try{
            //生成key
            keyGenerator keyGenerator = keyGenerator.getInstance("DES");
            keyGenerator.init(56);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] bytesKey = secretKey.getEncode();
            
            //KEY转换
            DESkeySpec desKeySpec = new DESkeySpec(bytesKey);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
            Key convertSecretKey = factory.generateSecretKey(desKeySpec);
            
            //加密
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,convertSecretKey);
            byte[] result = cipher.doFinal(src.getBytes());
            System.out.println("jdk des encrypt : " + Hex.encodeHexString(result));

            //解密操作
            cipher.init(Cipher.DECRYPT_MODE,convertSecretKey);
            result = cipher.doFinal(result)
            System.out.println("jdk des decrypt : " + new String(result));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bcDES() {
        try{
            Security.addProvider(new BouncyCastleProvider());
            //生成key
            keyGenerator keyGenerator = keyGenerator.getInstance("DES","BC");
            keyGenerator.init(56);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] bytesKey = secretKey.getEncode();
            
            //KEY转换
            DESkeySpec desKeySpec = new DESkeySpec(bytesKey);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
            Key convertSecretKey = factory.generateSecretKey(desKeySpec);
            
            //加密
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,convertSecretKey);
            byte[] result = cipher.doFinal(src.getBytes());
            System.out.println("jdk des encrypt : " + Hex.encodeHexString(result));

            //解密操作
            cipher.init(Cipher.DECRYPT_MODE,convertSecretKey);
            result = cipher.doFinal(result)
            System.out.println("jdk des decrypt : " + new String(result));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

* 应用

发送方 -- 构建密钥 -- 公布密钥 -- 使用密钥对数据进行加密 -- 发送加密数据 -- 使用密钥解密 -- 接收方

## 对称加密算法 -- 3DES

* 为什么会有3DES
    1. 违反科克霍夫原则
    3. 安全问题

* 3DES的好处
    1. 密钥长度增强
    2. 迭代次数提高
* 3DES（Triple DES或 DESede）

| 密钥长度 | 默认 | 工作模式 | 填充方式 | 实现方 |
| -------- | --- | ------- | -------- | ----- |
| 112，168 | 168 | ECB,CBC,PCBC,CTR,CTS,CFB,CFB8到128 ， OFB, OFB8到128 | NoPadding , PKCS5Padding , ISO10126Padding | JDK |
| 128，192 | 168 | 同上 | PKCS7Padding , ISO10126d2Padding , X932Padding , ISO7816d4Padding , ZeroBytePadding | BC |

* 源码

```Java
public class Imooc3DES {
    private static String src = "imooc security 3des";

    public static void main(String[] args) {

    }

    public static void jdk3DES(){
        try{
            //生成key
            keyGenerator keyGenerator = keyGenerator.getInstance("DESede");
//            keyGenerator.init(168);
            //生成一个默认长度的key，根据不同的算法生成不同的key
            keyGenerator.init(new SecureRandom())
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] bytesKey = secretKey.getEncode();
            
            //KEY转换
            DESedeKeySpec desedeKeySpec = new DESedeKeySpec(bytesKey);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
            Key convertSecretKey = factory.generateSecretKey(desedeKeySpec);
            
            //加密
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,convertSecretKey);
            byte[] result = cipher.doFinal(src.getBytes());
            System.out.println("jdk 3des encrypt : " + Hex.encodeHexString(result));

            //解密操作
            cipher.init(Cipher.DECRYPT_MODE,convertSecretKey);
            result = cipher.doFinal(result)
            System.out.println("jdk 3des decrypt : " + new String(result));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bc3DES(){
        //练习
    }
}
```


## 对称加密算法AES

* DES效率低，安全性低，
* AES 的优势之一是没有官方报道被破解
* AES通常用于移动痛惜系统的加密以及基于SSH通信协议的软件，SSH Client

* 高级
* DES替代者

| 密钥长度 | 默认 | 工作模式 | 填充方式 | 实现方 |
| -------- | --- | ------- | -------- | ----- |
| 128,192,256 | 128 | ECB,CBC,PCBC,CTR,CTS,CFB,CFB8到128 ， OFB, OFB8到128 | NoPadding , PKCS5Padding , ISO10126Padding | JDK (256位密钥需要获得政策限制权限文件)|
| 同上 | 同上 | 同上 | PKCS7Padding , ZeroBytePadding | BC |


无政策限制权限文件是，因为某些国家的出口管制限制，Java发布运行环境包中的加解密有一定的限制

* 源码

```Java

public class ImoocAES {
    private static String src = "imooc security aes";
    public static void main(String[] args) {

    }

    public static void jdkAES () {
        try{
            //生成key
            keyGenerator keyGenerator = keyGenerator.getInstance("AES");
//            keyGenerator.init(128);
            keyGenerator.init(new SecureRandom());
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keyBytes = secretKey.getEncode();

            //key的转换
            Key key = new SecretKeySpec(keyBytes,"AES");

            //加密
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(src.getBytes());
            System.out.println("jdk aes encrypt : " + Base64.encodeBase64String(result));

            //解密
            cipher.init(Cipher.DECRYPT_MODE,key);
            result = cipher.doFinal(result);
            System.out.println("jdk aes dncrypt : " + new String(result));
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    public static void bcAES(){
        //练习
    }
}

```

* 应用

发送方 -- 构建密钥 -- 公布密钥 -- 使用密钥对数据加密 -- 发送加密数据 -- 使用密钥对数据解密


## 对车加密算法 -- PBE

PBE算法 相对于DES ，3DES ， AES有些另类

PBE结合了消息摘要算法和对称加密算法的优点。

* PBE （Password Based Encryption）基于口令加密
* 对已有算法进行包装
* JDK，BC
* 盐 salt
* PBEWithMD5AndDES

| 算法 | 密钥长度 | 默认 | 工作模式 | 填充方式 | 实现 |
| ---  | ------- | ---- | ------- | ------- | ---- |
| PBEWithMD5AndDES | 64 | 64 | CBC | PKCS5Padding , PKCS7Padding , ISO10126Padding , ZeroBytePadding | BC |
| PBEWithMD5AndRC2 | 112 | 128 | 同上  | 同上 | 同上 | 
| PBEWithSHA1AndDES | 64 | 64 | 同上  | 同上 | 同上 | 
| PBEWithSHA1AndRC2 | 128 | 128 | 同上  | 同上 | 同上 | 
| PBEWithSHAAndIDEA-CBC | 128 | 128 | 同上  | 同上 | 同上 | 
| PBEWithSHAAnd2KeyTripleDES-CBC | 128 | 128 | 同上  | 同上 | 同上 | 
| PBEWithSHAAnd3keyTripleDES-CBC | 192 | 192 | 同上  | 同上 | 同上 | 
| PBEWithMD5AndDES | 56 | 56 | CBC | PKCS5Padding  | JDK |
| PBEWithMD5AndTripleDES | 112,168 | 168 | 同上 | 同上  | 同上 |
| PBEWithSHA1AndDES | 112,168 | 168 | 同上 | 同上  | 同上 |
| PBEWithSHA1AndRC2_40 | 40~1024 (8的倍数) | 128 | 同上 | 同上  | 同上 |


* 源码


```Java

public class ImoocPBE {
    private static String src = "imooc security pbe";
    public static void main(String[] args) {

    }

    public static void jdkPBE () {
        try{
            //初始化盐
            SecureRandom random = new SecureRandom();
            byte[] salt = random.generateSeed(8);

            //加密  口令与密钥
            String password = "imooc";
            //口令转换成密钥
            PBEkeySpec pbeKeySpec = new PBEkeySpec(password.toCharArray());
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWITHMD5andDES"); 
            Key key = factory.generateSecret(pbeKeySpec);

            //加密过程
            PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt,100);
            Cipher cipher = Cipher.getInstance("PBEWITHMD5andDES");
            cipher.init(Cipher.ENCRYPT_MODE,key,pbeParameterSpec);
            byte[] result = cipher.doFinal(src.getBytes());
            System.out.println("jdk pbe encrypt : " + Base64.encodeBase64String(result));

            //解密
            cipher.init(Cipher.DECRYPT_MODE,key,pbeParameterSpec);
            result = cipher.doFinal(result);
            System.out.println("jdk pbe decrypt : " + new String(result));

        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void bcPBE () {
        //练习 
    }
}
```

* 应用

发送方 -- 构建口令 -- 公布口令 -- 构建盐 -- 使用口令、盐对数据加密 -- 发送盐、加密数据 -- 使用口令、盐对数据进行解密 -- 接收方

## 对称加密算法回顾

* 初等
* DES 3DES
* AES
* PBE



加密和解密不在同一方怎么办？
将密钥发送给接收方

