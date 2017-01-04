# git - 简明指南

> Create Time : 2017年1月1日

> 引用自： http://rogerdudler.github.io/git-guide/index.zh.html

* 1. 安装

[下载git OSX版](http://code.google.com/p/git-osx-installer/downloads/list?can=3)

[下载git Windows版](http://msysgit.github.io/)

[下载git Linux版](http://book.git-scm.com/2_installing_git.html)

* 2. 创建新仓库

创建新文件夹，打开，然后执行`git init`
以创建新的git仓库

* 3. 检出仓库

执行如下命令可以创建一个本地仓库的克隆版本
```shell
git clone /path/to/repository
```
如果是远端服务器上的仓库，你的命令会是这个样子：
```shell
git clone username@host:/path/to/repository
```

* 4. 工作流

你的本地仓库由`git`维护的三棵树组成。

第一个是你的`工作目录`，它持有实际文件；

第二个是`暂存区（Index）`，它像个缓存区域，临时保存你的改动；

最后是`HEAD`，它执行你最后一次提交的结果。

```
                 add              commit
<working dir> ---------=> Index -----------=> HEAD

```

* 5. 添加和提交

你可以提出更改（把它们添加到暂存区），试用如下命令：
```shell
git add <filename>
git add *
```
这是git基本工作流程的第一步；试用如下命令以实际提交改动：
```shell
git commit -m "代码提交信息"
```
现在，你的改动已经提交到了`HEAD`,但是还没到你的远端仓库。


* 6. 推送改动

你的改动现在已经在本地仓库的`HEAD`中了。执行如下命令，将这些改动提交到远端仓库：
```shell
git push origin master
```
可以把`master`换成你想要推送的任何分支。
如果你还没有克隆到现有仓库，并欲将你的仓库连接到某个远程服务器，你可以试用如下命令添加：
```shell
git remote add origin <server>
```
如此你就能够将你的改动推送到所添加的服务器上去了。


* 7. 分支

分支是用来将特性开发绝缘开来的。在你创建仓库的时候，`master`是`默认的`分支。在其他分支上进行开发，完成后再将它们合并到主分支上。
```

    branch  /ˉˉˉˉˉˉ feature_x ˉˉˉˉˉˉˉˉˉ\   merge
-----------·-------  master ------------·--------->
```

创建一个叫做`feature_x`的分支，并切换过去：
```shell
git checkout -b feature_x
```
切换回主分支：
```shell
git checkout master
```
把新建的分支删掉：
```shell
git branch -d feature_x
```
除非你将分支推送到远端仓库，不然该分支就是`不为他人所见`的：
```shell
git push origin <branch>
```

* 8. 更新与合并

要更新你的本地仓库至最新改动，执行：
```shell
git pull
```
以在你的工作目录中***获取(fetch)***并***合并(merge)***远端的改动。

要合并其他分支到你的当前分支（例如master），执行：
```shell
git merge <branch>
```
两种情况下，git都会尝试去自动合并改动。不幸的是，自动合并并非次次都能成功，并可能导致***冲突（conflicts）***。这时候就需要你修改这些文件夹来手动合并这些***冲突（conflicts）***了。

改完之后，你需要执行如下命令以将它们标记为合并成功：
```shell
git add <filename>
```
在合并改动之前，也可以试用如下命令查看：
```shell
git diff <source_branch> <target_branch>
```

* 9. 标签

在软件发布的时候创建标签，是被推荐的。这是个旧有概念，在SVN中也有。可以执行如下命令创建一个叫做**1.0.0**的标签：
```shell
git tag 1.0.0 1b231d63ff
```
**1b231d63ff**是你想要标记的提交ID的前10位字符。使用如下命令获取提交ID：
```shell
git log
```
你也可以用该提交ID的前几位，只要能够保证它是唯一的。

* 10. 替换本地改动

加入你做错事（自然，这是不可能的），你可以使用如下命令替换掉本地改动：
```shell
git　checkout -- <filename>
```
此命令会试用**HEAD**中的最新内容替换掉你的工作目录中的文件。

已添加缓存区的改动，以及新文件，都不受影响。

假如你想要丢弃你所有的本地改动与提交，可以到服务器上获取最新的版本，并将你本地分支执行到它：
```shell
git　fetch origin
git reset --hard origin/master
```

* 11. 有用的贴士

内建的图形化 git：
```shell
gitk
```

彩色的 git 输出：
```shell
git config color.ui true
```

显示历史记录时，只显示一行注释信息：
```shell
git config format.pretty oneline
```

交互地添加文件至缓存区：
```shell
git add -i
```
