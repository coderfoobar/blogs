

# Java - 环境变量

> Create Time : 2017年1月3日

> 在 Win10 中，配置JDK环境变量，不要使用反斜线 "\", 使用正斜线 "/".

```Java
JAVA_HOME
C:/Program Files/Java/jdk1.8.0_111

%JAVA_HOME%/bin;
%JAVA_HOME%/jre/bin

CLASSPATH
.;%JAVA_HOME%/lib/dt.jar;%JAVA_HOME%/lib/tools.jar; 
```




