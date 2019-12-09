# 在java中使用postgis操作地理位置数据

最近要做gps数据的分析，开始学习postgis。首先推荐下`不睡觉的怪叔叔`的[专栏](https://zhuanlan.zhihu.com/p/67232451)，
里面的postgis教程可以说很全面了，我就是靠这入的坑。

但在java程序中，对gis数据进行操作还有些问题，这里简单用文章记录下。

demo源码在https://github.com/lonelyleaf/postgis-java-demo，下面简单说明下整个过程与遇到的坑


## 1.环境说明

- jdk11
- gradle6
- postgres11+postgis
- idea 2019.3

## 2.预备工作

首先需要安装postgres与postgis，在windows下，安装postgres时可以顺带安装postgis，记得勾选

![img](img/postgis安装.png)

安装好后，使用pgadmin来新建一个数据库，这里就叫做`gis-test`

![img](img/pgadmin.png)
![img](img/新建数据库.png)

数据库就准备好了！

## 3.准备数据



如果直接使用我的源码，那么项目中已经使用flay
