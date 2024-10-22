# 食用指南
## 前置条件和限制
1. 支持查看指定ibd文件的主键索引对应的B+树
2. 只支持int类型（占4个字节）的主键
3. 数据量太大的不要使用，数据太多图片渲染会不成功，不过可以通过文字信息查看B+树的效果
4. 有一些统计信息
5. 需要JDK17
6. 需要graalvm，并且安装js库

graalvm-jdk目录
![需要graalvm.png](graalvm.jpg)
```shell
# 如提示
A language with id 'js' is not installed. Installed languages are: [].
# 请安装 js库
cd graalvm-jdk目录
gu install js
```

生成的B+树效果图
![result.png](btree%2Fresult.png)

统计信息截图
![img.png](img.png)

修改Main.Java中的ibdFilePath变量的值为ibd文件的绝对路径即可使用

如需交流可添加微信：
![wx.png](wx.jpg)
