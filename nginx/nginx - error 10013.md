
# nginx - error 10013



> Nginx在win7，win2008下启动报错：`bind() to 0.0.0.0:80 failed (10013: An attempt was made to access a socket in a way forbidden by its access permissions)` 。

* 原因: `nginx`默认`80`端口被`System`占用，造成`nginx`启动报错的解决方案。

排错：

    在cmd窗口运行如下命令

```shell
C:\Users\Administrator> netstat -aon | findstr :80

# 看到80端口果真被占用。发现占用的pid是4，名字是System。怎么禁用呢？
```

解决办法:

* 1、打开注册表：`regedit`

* 2、找到：`HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\services\HTTP`

* 3、找到一个`REG_DWORD`类型的项`Start`，将其改为`0`

* 4、重启系统，`System`进程不会占用`80`端口

重启之后，`start nginx.exe` 。在浏览器中，输入`127.0.0.1` 或者 `localhost`，即可看到亲爱的“Welcome to nginx!” 了。