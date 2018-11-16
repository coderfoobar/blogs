# 创建项目骨架

# 通过已有的骨架生成项目

> ref : https://blog.csdn.net/lvyuan1234/article/details/79159115

1. 下载 archetype-catalog.xml 至本地: 放到相应的 catalog 目录中.

```
.m2/repository/org/apache/maven/archetype/archetype-catalog/2.4

```

2. 在想要创建项目的目录下执行:

```
 mvn org.apache.maven.plugins:maven-archetype-plugin:2.4:generate -DarchetypeCatalog=local
```

在《Maven实战》中maven-archetype-plugin插件可以从`internal local remote file://... http://...`这几个地方读取archetype-catalog.xml文件。
然后我也自定义了一个archetype-catalog.xml文件，放在任意一个目录下.

执行命令: `mvn archetype:generate -DarchetypeCatalog=file://D:/maven/archetype-catalog.xml`

结果报错了：

```
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-archetype-plugin:3.0.1:generate (default-cli) on project standalone-pom: archetypeCatalog 'http://nexus.opendaylight.org/content/repositories/opendaylight.snapshot/archetype-catalog.xml' is not supported anymore. Please read the plugin documentation for details. -> [Help 1]
```

原因是：maven-archetype-plugin 3.x插件有一些有争议性的变化，带来的直接影响就是对一些参数（例如自定义archetype-catalog.xml）不再支持，有bug，这里是issues连接：

https://issues.apache.org/jira/browse/ARCHETYPE-519

http://mail-archives.apache.org/mod_mbox/maven-dev/201705.mbox/thread  (search for the thread 'The maven-archetype-plugin paradox')


> 如果还想用自定义archetype-catalog.xml文件，就要用maven-archetype-plugin 2.x插件，改为如下：

```
mvn org.apache.maven.plugins:maven-archetype-plugin:2.4:generate -DarchetypeCatalog=local
```

参考地址:

https://communities.vmware.com/message/2707795#2707795