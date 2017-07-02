
# Java实现非对称加密算法

> Create Time : 2017年6月29日  Author : huduku.io  Ref : imooc

* 高级
* 双保险
* 公钥，私钥 （成对出现）
* DH （Diffie-Hellman） 密钥交换算法
* RSA - 基于因子分解 （应用最广的非对称加密算法）
* ElGamal - 基于离散对数
* ECC （Elliptical Curve Cryptography） - 椭圆曲线加密

## 对称加密算法回顾

* 数据安全
* 密钥管理复杂



## 非对称加密算法 - DH （密钥交换）

* 对称加密带来的困扰 - 如何安全的传递密钥
* 构建本地密钥
* 对称

| 密钥长度 | 默认 | 工作模式 | 填充方式 | 实现方 |
| ------- | ---- | ------- | -------- | ------ |
| 512~1024(64倍数) | 1024 | 无 | 无 | JDK |

### 算法过程

* 初始化发送方密钥
    * KeyPairGenerator
    * KeyPair 
    * PublicKey

通过KeyPairGenerator来得到KeyPair类的对象。

//创建keyPairGenerator对象

KeyPairGenerator senderKeyPairGenerator = KeyPairGenerator.getInstance("DH");

* 初始化接收方密钥
    * KeyFactory
    * X509EncodedKeySpec  // 根据ASN.1标准进行密钥编码
    * DHPublicKey
    * DHParameterSpec
    * KeyPairGenerator
    * PrivateKey

* 密钥构建
    * KeyAgreement //提供密钥一致性（或密钥交换）协议的功能
         // static KeyAgreement getInstance(String algorithm) 生成实现指定密钥一致性算法的KeyAgreement对象
         // static KeyAgreement getInstance(String algorithm , Provider provider) 为来自制定提供程序的指定密钥一致性算法生成一个KeyAgreement对象
    * SecretKey // 生成一个分组的秘密密钥，提供了一个相对安全的操作 实现了接口Key
    * KeyFactory
    * X509EncodedKeySpec
    * PublicKey 

* 加密 ， 解密
    * Cipher //构成了JCE框架的核心  Cipher cipher = Cipher.getInstance("DES");

### 源码

```Java
public class ImoocDH {

    private static String src = "imooc security dh";

    public static void main(String[] args) {

    }

    public static void jdkDH(){
        try{
            //1 初始化发送方的密钥
            KeyPairGenerator senderKeyPairGenerator = KeyPairGenerator.getInstance("DH");
            senderKeyPairGenerator.initialize(512);
            KeyPair senderKeyPair = senderKeyPairGenerator.generateKeyPair();
            byte[] senderKeyPairEnc = senderKeyPair.getPublic().getEncode(); //发送方公钥，发送给接收方 （网络，文档。。。）

            //2. 初始化接收方密钥
            KeyFactory receiverKeyFactory = KeyFactory.getInstance("DH");
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(senderKeyPairEnc);
            PublicKey receiverPublicKey = receiverKeyFactory.generatePublic(x509EncodedKeySpec);
            DHParamterSpec dhParameterSpec = ((DHPublicKey)receiverPublicKey).getParams();
            KeyPairGenerator recevierKeyPairGenerator = KeyPairGenerator.getInstance("DH");
            recevierKeyPairGenerator.initialize(dhParameterSpec);
            KeyPair receiverKeyPair = recevierKeyPairGenerator.generateKeyPair();
            PrivateKey receiverPrivateKey = receiverKeyPair.getPrivate();
            byte[] receiverPublicKeyEnc = receiverKeyPair.getPublic().getEncode();

            //3. 密钥构建
            KeyAgreement receiverKeyAgreement = KeyAgreement.getInstance("DH");
            receiverKeyAgreement.init(receiverPrivateKey);
            receiverKeyAgreement.doPhase(receiverPublicKey,true);
            SecretKey receiverDesKey = receiverKeyAgreement.generateSecret("DES");
            
            KeyFactory keyFactory = KeyFactory.getInstance("DH");
            x509EncodedKeySpec = new X509EncodedKeySpec(receiverPublicKeyEnc);
            PublicKey senderPublicKey = senderKeyFactory.generatePublic(x509EncodedKeySpec);
            KeyAgreement senderKeyAgreement = KeyAgreement.getInstance("DH");
            senderKeyAgreement.init(senderKeyPair.getPrivate());
            senderKeyAgreement.doPhase(senderPublicKey,true);

            SecretKey senderDesKey = senderKeyAgreement.generateSecret("DES");
            if (Objects.equals(receiverDesKey,senderDesKey)) {
                System.out.println("双方密钥相同");
            }

            //4. 加密
            Cipher cipher = Cipher.getInstance();
            cipher.initCipher(Cipher.ENCRYPT_MODE , senderDesKey);
            byte[] result = cipher.doFinal(src.getBytes());
            System.out.println("jdk dh encrypt : " + Base64.encodeBase64String(result));

            //5 解密
            cipher.initCipher(Cipher.DECRYPT_MODE , receiverDesKey);
            byte[] result = cipher.doFinal(src.getBytes());
            System.out.println("jdk dh encrypt : " + new String(result));
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
```

### 应用

* 初始化DH算法密钥对

发送方 -- 构建发送方密钥 -- 公布发送方密钥 -- 使用发送方密钥构建自己密钥 -- 公布接收者公钥 -- 接收者

* DH算法加密消息传递 

发送方 -- 使用本地密钥加密消息 -- 发送加密消息 -- 使用本地密钥解密消息 -- 接收方




## 非对称加密算法 -- RSA

* 唯一广泛接受并实现
* 数据加密 & 数字签名
* 公钥加密 ， 私钥解密
* 私钥加密 ， 公钥解密
* 基于大数因子分解

| 密钥长度 | 默认 | 工作模式 | 填充方式 | 实现方 |
| ------- | ---- | ------- | -------- | ------ |
| 512~1024(64倍数) | 1024 | ECB | NoPadding,PKCS1Padding,OAEPWITMD5AndMGF1Padding,OAEPWITSHA1AndMGF1Padding,OAEPWITSHA256AndMGF1Padding,OAEPWITSHA384AndMGF1Padding,OAEPWITSHA512AndMGF1Padding | JDK |
| 512~1024(64倍数) | 2048 | NONE | NoPadding,PKCS1Padding,OAEPWITMD5AndMGF1Padding,OAEPWITSHA1AndMGF1Padding,OAEPWITSHA256AndMGF1Padding,OAEPWITSHA384AndMGF1Padding,OAEPWITSHA512AndMGF1Padding,ISO9769-1Padding | BC |

```Java

public class ImoocRSA {
    private static final String src = "imooc security rsa";
    public static void main(String[] args) {

    }

    public static void jdkRSA(){
        try{
            //1. 初始化密钥
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");        
            keyPairGenerator.initialize(512);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey rsaPublicKey = (RSAPublicKey)keyPair.getPublic();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey)keyPair.getPrivate();
            System.out.println("Public Key : " + Base64.encodeBase64String(rsaPublicKey.getEncoded()));
            System.out.println("Private Key : " + Base64.encodeBase64String(rsaPrivateKey.getEncoded()));

            //2. 私钥加密，公钥解密 -- 加密
            PKCS8EncodeKeySpec pkcs8EncodeKeySpec = new PKCS8EncodeKeySpec(rsaPrivateKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodeKeySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE,privateKey);
            byte[] result = cipher.doFinal(src.getBytes());
            System.out.println("私钥加密，公钥解密 -- 加密 ： " + Base64.encodeBase64String(result));

            //3. 私钥加密，公钥解密 -- 解密
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
            keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE,publicKey);
            result = cipher.doFinal(result);
            System.out.println("私钥加密，公钥解密 -- 解密 : " + new String(result));

            //4. 公钥加密，私钥解密 -- 加密
            x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
            keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE,publicKey);
            result = cipher.doFinal(src.getBytes());
            System.out.println("公钥加密，私钥解密 -- 加密 ： " + Base64.encodeBase64String(result));

            //5. 公钥加密，私钥解密 -- 解密
            pkcs8EncodeKeySpec = new PKCS8EncodeKeySpec(rsaPrivateKey.getEncoded());
            keyFactory = KeyFactory.getInstance("RSA");
            privateKey = keyFactory.generatePrivate(pkcs8EncodeKeySpec);
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE,privateKey);
            result = cipher.doFinal(result);
            System.out.println(" 公钥加密，私钥解密 -- 解密 ： " + new String(result));

        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
```


## 非对称加密算法 -- ElGamal

* 公钥加密算法
* 实现方 : Bouncy Castle (JDK未提供相应的实现)


| 密钥长度 | 默认 | 工作模式 | 填充方式 | 实现方 |
| ------- | ---- | ------- | -------- | ------ |
| 160~16384 (8的整数倍) | 1024 | ECB, NONE | NoPadding,PKCS1Padding,OAEPWITMD5AndMGF1Padding,OAEPWITSHA1AndMGF1Padding,OAEPWITSHA224AndMGF1Padding,OAEPWITSHA256AndMGF1Padding,OAEPWITSHA384AndMGF1Padding,OAEPWITSHA512AndMGF1Padding,ISO9769-1Padding | BC |


### 源码

```Java
public class ImoocElGamal {
    public static void main(String[] args) {

    }

    public static void bcElgamal(){
        //公钥加密 ， 私钥解密
        Security.addProvider(new BouncyCastleProvider());

        //1. 初始化密钥
        AlgorithmParameterGenerator algorithmParameterGenerator = 
            AlgorithmParameterGenerator.getInstance("ElGamal");
        algorithmParameterGenerator.init(256);
        AlgorithmParameters algorithmParameters = algorithmParameterGenerator.generateParameters();
        DHParameterSpec dhParameterSpec = 
            (DHParameterSpec)algorithmParameters.getParameterSpec(DHParameterSpec.class);
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ElGamal");
        keyPairGenerator.initialize(dhParameterSpec,new SecureRandomn());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey elGamalPublicKey = keyPair.getPublic();
        PrivateKey elGamalPrivateKey = keyPair.getPrivate();
        System.out.println("Public key : " + Base64.encodeBase64String(elGamalPublicKey.getEncoded()));
        System.out.println("Private key : " + Base64.encodeBase64String(elGamalPrivateKey.getEncoded()));

        

    }
}
```







