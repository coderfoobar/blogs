

# nginx - windows 下启动和停止

* 1. 启动 

```shell
C:\nginx\> start nginx.exe 
# 或者
C:\nginx\> nginx.exe 
```

* 2. 停止

```shell

C:\nginx\> nginx.exe -s stop
# 或者
C:\nginx\> nginx.exe -s quit

```

* 3. 重启

```shell

C:\nginx\> nginx.exe -s reload

```

* 4. 重新打开日志文件

```shell

C:\nginx\> nginx.exe -s reopen

```

* 5. 查看nginx版本

```shell

C:\nginx\> nginx.exe -v

```