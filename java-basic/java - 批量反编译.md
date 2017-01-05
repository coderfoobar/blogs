
# Java - 批量反编译

> Create Time : 2017年1月5日

```bat
jad -o -r -dC:\Resource\tmp\rmcs-dispatch-center-v2-raf-s-src -sjava C:\Resource\tmp\rmcs-dispatch-center-v2-raf-s\**\*.class
```

* `-o`  - overwrite output files without confirmation (default: no) 无需确定覆写文件
* `-r`  - restore package directory structrure 恢复包目录结构
* `-s` <ext></ext>- output file extension (by default '.jad') 如果不设置为-sjava，则默认扩展名为.jad
* 其他的，C:\Resource\tmp\rmcs-dispatch-center-v2-raf-s\**\*.class 中的两颗接连的星，表示任意层次的子目录。

> jad下载地址: http://www.javadecompilers.com/jad/Jad%201.5.8g%20for%20Windows%209x-NT-2000%20on%20Intel%20platform.zip

> jad下载地址(上级目录):  http://www.javadecompilers.com/jad


