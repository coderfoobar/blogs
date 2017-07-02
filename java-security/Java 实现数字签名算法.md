

# Java 实现数字签名算法

> Create Time : 2017年7月2日 Author : huduku.io  Ref : imooc

* 签名
* 数字签名 （带有密钥（公钥，私钥）的消息摘要算法）
* 验证数据完整性，认证数据来源，康否认
* OSI参考模型
* 私钥签名，公钥验证
* RSA，DSA，ECDSA

## 数字签名算法 - RSA

* 经典算法
* MD，SHA

| 算法 | 密钥长度 | 默认长度 | 签名长度 | 实现方 |
| --- | -------- | -------- | ------ | ------ |
| MD2withRSA | 512~65536 (64的倍数) | 1024 | 与密钥长度相同 | JDK |
| MD5withRSA |   |   |   |   |
| SHA1withRSA |  |   |   |   |
| SHA224withRSA |   | 2048 |   | BC |
| SHA256withRSA |   |   |   |   |
| SHA384withRSA |   |   |   |   |
| SHA512withRSA |   |   |   |   |
| RIPEMD128withRSA |   |   |   |   |
| RIPEMD160withRSA |   |   |   |   |



```Java
public class ImoocRSASingnature {
    private static final String src = "imooc security rsa";
    public static void main(String[] args) {

    }

    public static void jdkRSA() {
        try{
            //1. 初始化密钥
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(512);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey rsaPublicKey = (RSAPublicKey)keyPair.getPublic();
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey)keyPair.getPrivate();

            //2. 执行签名
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivateKey(pkcs8EncodedKeySpec);
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initSign(privateKey);
            signature.update(src.getBytes());
            byte[] result = signature.sign();
            System.out.println("jdk rsa sign : " + Hex.encodeHexString(result));

            //3. 验证签名
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(rsaPublicKey.getEncoded());
            keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublicKey(x509EncodedKeySpec);
            signature = Signature.getInstance("MD5withRSA");
            signature.initVerify(publicKey);
            signature.update(src.getBytes());
            boolean isPassVerify = signature.verify(result);
            System.out.println("jdk rsa verify : " + isPassVerify);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
```


## 数字签名算法 -- DSA

* DSS (Digital Signature Standard) 数字签名标准
* DSA (Digital Signature Algorithm) 数字签名算法
* DSA 仅包含数字签名

| 算法 | 密钥长度 | 默认长度 | 签名长度 | 实现方 |
| --- | -------- | -------- | ------ | ------ |
| SHA1withDSA | 512 ~ 1024 (64的倍数) | 1024 | JDK |
| SHA224withDSA | | | BC |
| SHA256withDSA | | |    |
| SHA384withDSA | | |    |
| SHA512withDSA | | |    |


### 源码

```Java
public class ImoocDSA {

    private static final String src = "imooc security dsa";

    public static void main(String[] args) {

    }

    public static void jdkDSA () {
        try{
            //1. 初始化密钥
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
            keyPairGenerator.initialize(512);
            KeyPair keyPair = keyPairGenerator.generatorKeyPair();
            DSAPublicKey dsaPublicKey = (DSAPublicKey)keyPair.getPublic();
            DSAPrivateKey dsaPrivateKey = (DSAPrivateKey)keyPair.getPrivate();

            //2. 执行签名
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(dsaPrivateKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            Privatekey privatekey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Signature signature = Signature.getInstance("SHA1withDSA");
            signature.initSign(privateKey);
            signature.update(src.getBytes());
            byte[] result = signature.sign();
            System.out.println("jdk dsa sign : " + Hex.encodeHexString(result));

            //3. 验证签名
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(dsaPublicKey.getEncoded());
            keyFactory = KeyFactory.getInstance("DSA");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            signature = Signature.getInstance("SHA1withDSA");
            signature.initVerify(publicKey);
            signature.update(src.getBytes());
            boolean isPassVerify = signature.verify(result);
            System.out.println("jdk dsa verify : " + isPassVerify);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
```

## 数字签名算法 -- ECDSA

* 微软
* Elliptic Curve Signature Algorithm , 椭圆曲线数字签名算法
* 速度快，强度高，签名短

| 算法 | 密钥长度 | 默认长度 | 签名长度 | 实现方 |
| --- | -------- | -------- | ------ | ------ |
| NONEwithECDSA | 112~571 | 128 | JDK/BC |
| RIPEMD1withECDSA | | 160 | BC |
| SHA1withECDSA | | 160 |  JDK/BC  |
| SHA224withECDSA | | 224 |  BC  |
| SHA256withECDSA | | 256 |  JDK/BC  |
| SHA384withECDSA | | 384 |  JDK/BC  |
| SHA512withECDSA | | 512 |  JDK/BC  |

### 源码

```Java
public class ImoocECDSA{
    private static final String src = "imooc security ecdsa";
    public static void main(String[] args){

    }

    public static void jdkECDSA(){
        try{
            //1 初始化密钥
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            keyPairGenerator.initialize(256);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            ECPublicKey ecPublicKey = (ECPublicKey) keyPair.getPublic();
            ECPrivateKey ecPrivatekey = (ECPrivateKey)keyPair.getPrivate();

            //2. 执行签名
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(ecPrivatekey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            Signature signature = Signature.getInstance("SHA1withECDSA");
            signature.initSign(privateKey);
            signature.update(src.getBytes());
            byte[] result = signature.sign();
            System.out.println("jdk ecdsa sign : " + Hex.encodeHexString(result));

            //3. 验证签名
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
            keyFactory = KeyFactory.getInstance("EC");
            PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
            signature = Signature.getInstance("SHA1withECDSA");
            signature.initVerify(publicKey);
            signature.update(src.getBytes());
            boolean isPassVerify = signature.verify(result);
            System.out.println("jdk ecdsa verify : "  + isPassVerify);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
```
