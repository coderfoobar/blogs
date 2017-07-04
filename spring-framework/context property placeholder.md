
#  spring <context:property-placeholder/> 配置详解

> Create Time : 2017年7月4日 Ref : http://blog.csdn.net/ws_blog/article/details/46986051

```xml
<context:property-placeholder     
        location="属性文件，多个之间逗号分隔"    
        file-encoding="文件编码"    
        ignore-resource-not-found="是否忽略找不到的属性文件"    
        ignore-unresolvable="是否忽略解析不到的属性，如果不忽略，找不到将抛出异常"    
        properties-ref="本地Properties配置"    
        local-override="是否本地覆盖模式，即如果true，那么properties-ref的属性将覆盖location加载的属性，否则相反"    
        system-properties-mode="系统属性模式，默认ENVIRONMENT（表示先找ENVIRONMENT，再找properties-ref/location的），NEVER：表示永远不用ENVIRONMENT的，OVERRIDE类似于ENVIRONMENT"    
        order="顺序"    
        />   
```


* `location`：表示属性文件位置，多个之间通过如逗号/分号等分隔；
* `file-encoding`：文件编码；
* `ignore-resource-not-found`：如果属性文件找不到，是否忽略，默认false，即不忽略，找不到将抛出异常
* `ignore-unresolvable`：是否忽略解析不到的属性，如果不忽略，找不到将抛出异常
* `properties-ref`：本地Java.util.Properties配置
* `local-override`：是否本地覆盖模式，即如果true，那么properties-ref的属性将覆盖location加载的属性
* `system-properties-mode`：系统属性模式，ENVIRONMENT（默认），NEVER，OVERRIDE
    * `ENVIRONMENT`：将使用spring 3.1提供的PropertySourcesPlaceholderConfigurer，其他情况使用Spring 3.1之前的PropertyPlaceholderConfigurer
       如果是本地覆盖模式：那么查找顺序是：properties-ref、location、environment，否则正好反过来；
    * `OVERRIDE`： PropertyPlaceholderConfigurer使用，因为在spring 3.1之前版本是没有Enviroment的，所以OVERRIDE是spring 3.1之前版本的Environment
       如果是本地覆盖模式：那么查找顺序是：properties-ref、location、System.getProperty(),System.getenv()，否则正好反过来； 
    * `NEVER`：只查找properties-ref、location；
* `order`：当配置多个<context:property-placeholder/>查找顺序；