

# Linux - 配置集群之间SSH免密码登录

SSH 是Secure Shell的缩写，有IETF的网络小组（Network Working Group）所制定。SSH是建立在应用层和传输层基础上的安全协议。

## SSH配置

在集群上的每台主机打开配置
```shell
sudo vim /etc/ssh/sshd_config
```
开启下面的选项
```txt
RSAAuthentication yes
PubkeyAuthentication yes
AuthorizedKeyFile .ssh/authorized_keys
```

## 生成SSH密钥

在集群的每台主机上执行下面的命令：
```shell
ssh-keygen -t rsa
```

## 把公钥写入authorized_keys文件

在集群中的每台机器上执行下面的命令，将每台机器本身的公钥拷贝到Master主机上：
```shell
ssh-copy-id hadoop@HadoopMaster
``` 

最终会在Master主机上生成~/.ssh/authorized_keys文件：
把HadoopMaster的authorized_keys拷贝到slave1和slave2上：

```shell
scp ~/.ssh/authorized_keys hadoop@HadoopSlave1:~/.ssh/
scp ~/.ssh/authorized_keys hadoop@HadoopSlave2:~/.ssh/
```

## 重启SSH服务

```shell
sudo service sshd restart
```

## 测试连接

```shell
ssh HadoopSlave1
# 此时连接不需要输入密码
```


## 常见错误

### 配置问题

* 1. 检查配置文件/etc/ssh/sshd_config是否开启了AuthorizedKeysFile选项

* 2. 检查AuthorizedKeysFile选项是否存在并内容正常

### 目录权限问题

* 1. `~` 权限设置为700   `sudo chmod 700 ~`
* 2. `~/.ssh` 权限设置为700   `sudo chmod 700 ~/.ssh`
* 3. `~/.ssh/authorized_keys`的权限设置为600   `sudo chmod 700 ~/.ssh/authorized_keys`

之后，重启SSH服务
```shell
sudo service sshd restart
```