
# Java Base64加密

## OSI

Open System Interconnection 开放系统互联

1. 网络通信
    * 物理层
    * 数据链路层
    * 网络层
    * 传输层
    * 会话层
    * 表示层
    * 应用层
2. 安全机制
    * 加密机制
    * 数字签名机制
    * 访问控制机制
    * 数据完整性控制机制
    * 认证机制
    * 业务流填充机制
    * 路由控制机制
    * 公正机制
3. 安全服务
    * 认证（鉴别）
    * 访问控制服务
    * 数据保密性服务
    * 数据完整性服务
    * 抗否认性服务


## TCP/IP安全体系

```
                       应用层
应用层                 表示层
                      会话层  


传输层                 传输层

网络层                 网络层

                      数据链路层
网络接口层             物理层
```


```
应用层              应用层安全
传输层              传输层安全
网络层              网络层安全
网络接口层          网络接口层安全
```

```
                  认证机制
认证（鉴别）服务   数字签名机制

访问控制服务       访问控制机制
                  路由控制机制

                  加密机制
数据保密性服务     业务流填充机制

数据完整性服务     数据完整性机制
抗否认性服务       公正机制
```




## Java安全组成

* JCA （Java Crytography Architecture）  消息摘要 数字签名
* JCE （Java Crytography Extension）     密钥管理 加密算法
* JSSE （Java Secure Socket Extension）  
提供基于SSL的加密功能，主要用在网络传输的过程中
* JAAS （Java Authentication and Authorization Service）
Java验证和授权服务


使用JDK以外的扩展包需要修改资源文件并增加相关的内容，这是使用JDK以外扩展包的方式之一
配置方式：
* security.provider.1=sun.security.provider.Sun
* security.provider.2=com.sun.net.ssl.internal.ssl.Provider
* security.provider.3=com.sun.rsajca.Provider
* security.provider.4=com.sun.crypto.provider.SunJCE
* security.provider.5=sun.security.jgss.SunProvider
* security.provider.6=org.bouncycastle.jce.provider.BouncyCastleProvider

硬编码方式
 Note: Providers can be dynamically registered instead by calls to
 either the addProvider or insertProviderAt method in the Security
 class.


## 相关包，类

* java.security - 消息摘要
* java.crypto - 安全消息摘要，消息认证（鉴别）码
* java.net.ssl - 安全套接字，网络数据传输相关的类
  HttpsURLConnection,SSLContext

查询相关的Javadoc

## 第三方扩展

* Bouncy Castle
    * 配置，
    * 调用
* Commons Codec
    * Apache
    * Base64 ， 二进制，十六进制，字符集编码
    * Url编码/解码

## 应用

* Base64算法
* 消息摘要算法
* 对称加密算法
* 非对称加密算法
* 数字签名算法
* 数字证书
* 安全协议


### Base64 算法

* aW1vb2Mgc2VjdXJpdHkgYmFzZTY0
* imooc security base64

算法实现
* JDK
* Commons Codec
* Bouncy Castle

## Java实现
cc , bc jar 
```Java
//ImoocBase64
 
// import ...

 public class ImoocBase64{
    public static final String src = "imooc security base64";

    public static void main(String[] args) {
        jdkBase64();
        commonsCodecBase64();
    }

    public static void jdkBase64(){
        try{
            BASE64Encoder encoder = new BASE64Encoder();
            String encode = encoder.encode(src.getBytes());

            System.out.println("encode: " + encode);

            BASE64Decoder decoder  = new BASE64Decoder();
            System.out.println("decode: " + new String(decoder.decoderBuffer(encode)));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void commonsCodecBase64(){
        byte[] encodeBytes = Base64.encodebase64(src.getBytes());
        System.out.println("encode: " + new String(encodeBytes));

        byte[] decodeBytes = Base64.decodeBase64(encodeBytes);
        System.out.println("dncode: " + new String(decodeBytes));
    }


    public static void bouncyCastleBase64(){
        byte[] encodeBytes = Base64.encode(src.getBytes());
        System.out.println("encode: " + new String(encodeBytes));

        byte[] decodeBytes = Base64.decode(encodeBytes);
        System.out.println("decoee: " + new String(decodeBytes));
    }
 }


```

## Base64 算法的应用场景

* email
* 密钥
* 证书文件

* 产生： 邮件的历史问题
* 定义： 基于64个字符的编码算法
* 关于RFC 2045 Base64算法规范
* 衍生： Base16 Base32 Url Base64
* Base64算法与加解密算法





