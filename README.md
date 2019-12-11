# postgis-java-demo

使用spring boot+mybatis对postgis进行基本增删改查操作。


## 1.环境说明

- jdk11
- gradle6
- postgres11+postgis
- idea 2019.3


## 2.项目结构说明

- 项目基于spring boot搭建
- 数据库层使用的[mybatis-plus]而不是直接使用mybatis，但基本配置差别不大
- 使用了lombok
- 使用了flyway来管理数据库表
- 使用了[mapstruct]来做dto转换
- 使用了[postgis-jdbc]来做postgis与java的类型转换
- 使用了[spring-fox]来自动文档生成

## 3.启动项目

首先需要安装好postgres与postgis。
然后创建数据库,这里叫做`gis-test`

进入数据库后启用postgis

> CREATE EXTENSION postgis

之后在ide中启动项目，就会自动通过flyway把表结构与样本数据自动初始化好了。

启动后通过`http://localhost:8080/swagger-ui.html`就可以访问文档并调用rest接口了。

## 4.注意事项


##### 4.1. geography的处理

[postgis-jdbc]的文档中，对使用有说明
```
- You can use the org.postgis.DriverWrapper as replacement for the
  jdbc driver. This class wraps the PostGreSQL Driver to
  transparently add the PostGIS Object Classes. This method currently
  works both with J2EE DataSources, and with the older DriverManager
  framework. I's a thin wrapper around org.postgresql.Driver that
  simply registers the extension on every new connection created.

  To use it, you replace the "jdbc:postgresql:" with a
  "jdbc:postgresql_postGIS" in the jdbc URL, and make your
  environment aware of the new Driver class.
```

也就是要使用`org.postgis.DriverWrapper`作为driver，然后jdbc的url使用`jdbc:postgresql_postGIS`就可以
自动注册`org.postgis.Geometry`。但是，源码中支持的数据库类型是`geometry`而不支持`geography`，这两者的
差别可以参考`不睡觉的怪叔叔`和`德哥`的文章

[PostGIS教程十三：地理](https://blog.csdn.net/qq_35732147/article/details/86489918)
[PostGIS 距离计算建议 - 投影 与 球 坐标系, geometry 与 geography 类型](https://github.com/digoal/blog/blob/master/201710/20171018_02.md)


为了让`geography`也能自动注册，项目中自定义了`com.github.lonelyleaf.gis.db.DriverWrapper`类，
转换出来还是`org.postgis.Geometry`,在java代码层使用和`geometry`并没做区分。

##### 4.2. org.postgis.Geometry的json序列化

由于没有现成的`org.postgis.Geometry`转为[GeoJson]的库，所以项目中使用了自定义`SimplePoint`类来
转换以下然后序列化。

其实[postgis-jdbc]中有`org.postgis.jts`包，有对[JTS]的支持，[JTS]是有jackson库转[GeoJson]的。
如果需要在java层做些地理位置的运算，使用`org.postgis.jts`包应该更好。

##### 4.3. 坐标系的选择

SRID的选择其实很复杂，详细解释可以参考下`不睡觉的怪叔叔`的文章https://blog.csdn.net/qq_35732147/article/details/86301242。
这里摘抄一段

```
地球不是平的，也没有简单的方法把它放在一张平面纸地图上（或电脑屏幕上），所以人们想出了各种巧妙的解决方案（投影）。

每种投影方案都有优点和缺点，一些投影保留面积特征；一些投影保留角度特征，如墨卡托投影（Mercator）；
一些投影试图找到一个很好的中间混合状态，在几个参数上只有很小的失真。所有投影的共同之处在于，
它们将（地球）转换为平面笛卡尔坐标系，选择哪种投影取决于你将如何使用数据（需要哪些数据特征，面积？角度？或者其他）。
```

[SRID]其实就决定了你的坐标使用的哪种投影，由于我的数据都是标准的gps坐标（经纬度，没有偏移），
所以在转换与建表时，都使用了`srid=4326`。具体数据库应该使用哪种一定要根据业务来，不然使用postgis进行计算与使用
各种gis软件进行分析时一定会出问题。

在转换到`org.postgis.Point`时，项目中写死了srid的值，这不是必须，但为了保证正确最好这样做：
```
default org.postgis.Point toGisPoint(SimplePoint point) {
    Point gisPoint = new Point();
    gisPoint.x = point.getX();
    gisPoint.y = point.getY();
    gisPoint.dimension = 2;
    //WGS84坐标系，也就是GPS使用的坐标
    gisPoint.srid = 4326;
    return gisPoint;
}
```


[mybatis-plus]: https://mp.baomidou.com/
[mapstruct]: https://mapstruct.org/
[postgis-jdbc]: https://github.com/postgis/postgis-java
[spring-fox]: https://github.com/springfox/springfox
[GeoJson]: https://geojson.org/
[JTS]: https://github.com/locationtech/jts
[SRID]: https://en.wikipedia.org/wiki/Spatial_reference_system