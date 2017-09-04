
# git 配置多个用户

> Create Time : 2017年9月4日 Ref : http://www.cnblogs.com/ayseeing/p/4445194.html

因为工作与github上个人源码管理的原因，需要配置多个git ssh密钥。

1. 首先，通过`git bash`执行命令：

```shell
ssh-keygen -t rsa -C "your-email-address@hostname.com" -f ~/.ssh/you-new-ssh-key-name
```

执行之后，`~/.ssh/`下会生成两个文件，分别为`you-new-ssh-key-name`,`you-new-ssh-key-name.pub`。

2. 将`you-new-ssh-key-name.pub`添加到github或者工作的gitlab的sshkey上。

3. 在`~/.ssh`文件夹下找到config文件，如果没有，则新建，并输入以下内容：

```
Host github.com www.github.com
    IdentityFile ~/.ssh/you-new-ssh-key-name
```

4. 测试：

```
ssh -T github.com
```

如果成功的话，会输出以下内容：

```
Hi huduku! You've successfully authenticated, but GitHub does not provide shell access.
```



