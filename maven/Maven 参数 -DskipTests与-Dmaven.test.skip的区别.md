
# Maven 参数 -DskipTests与-Dmaven.test.skip的区别

> Create Time : 2018年5月10日16:24:47 Author : huduku.io


1. -DskipTests，不执行测试用例，但编译测试用例类生成相应的class文件至target/test-classes下。

2. -Dmaven.test.skip=true，不执行测试用例，也不编译测试用例类。