
# CentOS - JDK环境变量

```shell

export JAVA_HOME=/usr/lib/java/jdk1.8.0_111
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

```

> 注意： CLASSPATH 分隔符一定要使用 `:` ,不要使用 `;`,否则,在`source /etc/profile`使环境变量生效时，会出现类似于以下错误：

```shell

bash: /usr/lib/java/jdk1.8.0_111/lib/dt.jar: cannot execute binary file
/usr/lib/java/jdk1.8.0_111/lib/tools.jar: line 1: $'PK\003\004': command not found
/usr/lib/java/jdk1.8.0_111/lib/tools.jar: line 2: $'\bn\2026I': command not found
/usr/lib/java/jdk1.8.0_111/lib/tools.jar: line 3: :S1DDMETA-INF/MANIFEST.MFManifest-Version:: No such file or directory
/usr/lib/java/jdk1.8.0_111/lib/tools.jar: line 4: syntax error near unexpected token `('
'usr/lib/java/jdk1.8.0_111/lib/tools.jar: line 4: `Created-By: 1.7.0_07 (Oracle Corporation)
/usr/lib/java/jdk1.8.0_111/jre/lib/rt.jar: line 1: $'PK\003\004': command not found
/usr/lib/java/jdk1.8.0_111/jre/lib/rt.jar: line 2: $'\bo\2026I': command not found
/usr/lib/java/jdk1.8.0_111/jre/lib/rt.jar: line 3: $'\bo\2026I\247\367C\027%': command not found
/usr/lib/java/jdk1.8.0_111/jre/lib/rt.jar: line 4: Implementation-Vendor:: command not found
/usr/lib/java/jdk1.8.0_111/jre/lib/rt.jar: line 5: Implementation-Title:: command not found
/usr/lib/java/jdk1.8.0_111/jre/lib/rt.jar: line 6: Implementation-Version:: command not found
/usr/lib/java/jdk1.8.0_111/jre/lib/rt.jar: line 7: Specification-Vendor:: command not found
/usr/lib/java/jdk1.8.0_111/jre/lib/rt.jar: line 8: syntax error near unexpected token `('
'usr/lib/java/jdk1.8.0_111/jre/lib/rt.jar: line 8: `Created-By: 1.7.0_07 (Oracle Corporation)

```




