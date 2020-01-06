# 使用JTS与postgis进行空间数据交互

上篇文章[在java中使用postgis操作地理位置数据](../01-postgis/README.md)简单说明了基本的postgis建模，还有其如何与java程序进行数据交互。
但postgis-jdbc中提供的java模型生态与通用行不好，在java生态中，还有一个专门进行几何运算的库[JTS]。

JTS Topology Suite([JTS])拓扑套件是开源Java软件库，它提供平面几何的对象模型以及一组基本几何功能。并且
[JTS]符合Open GIS联盟发布的SQL简单功能规范(Simple Features Specification for SQL)。所以[JTS]
不仅可以和postgis的数据进行交互，并且还可以在java层提供空间数据关系的运算。

下面会介绍下怎样在一个java项目中引入[JTS]并与postgis中的数据进行交互

## 1 环境说明

- jdk11
- gradle6
- postgres11+postgis
- idea 2019.3

## 2 预备工作

同上编文章，这里不赘述

## 3 准备数据

这里也和上篇一致，不赘述。表结构为

```postgresql
CREATE TABLE "t_gps"
(
    "time"     timestamptz(3)        NOT NULL,
    "dev_id"   varchar(36)           NOT NULL,
    "location" geography(Point, 4326) NOT NULL,
    "gps_num"  int4,
    "gps_type" varchar(10)           NOT NULL,
    "azimuth"  float4,
    "gnd_rate" float4
) WITHOUT OIDS;
```

## 4 java后端项目

项目的基本结构还是spring-boot + mybatis-plus，下面介绍下其它工作与要点

### 4.1 引入[JTS]

使用gradle，直接引入`jts-core`即可

>  implementation "org.locationtech.jts:jts-core:1.16.1" 

其中核心是`org.locationtech.jts.geom.Geometry`类，可以看到其结构与postgis的数据类型是基本一致的

![img](img/jts-geometry.png)

### 4.2 java实体建模

这里建模也基本与上次一致，但要注意`location`字段的类型已经变为了`org.locationtech.jts.geom.Point`

```
@Data
@TableName("t_gps")
public class GpsEntity {
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
            timezone = "+08")
    @ApiModelProperty("时间")
    private Date time;
    @ApiModelProperty("设备id")
    private String devId;
    @ApiModelProperty("位置")
    private Point location;
    @ApiModelProperty("卫星定位数")
    private int gpsNum;
    @ApiModelProperty("GPS定位信息")
    private String gpsType;
    @ApiModelProperty("对地真北航向角")
    private double azimuth;
    @ApiModelProperty("地面速率")
    private double gndRate;
}
```

### 4.2 jts与jdbc交互

在[postgis-jdbc]项目中，本来对[JTS]有支持，但由于其太久没有维护，包名都不对了……
这里只能自行修改其代码了。




 [locationtech] :https://projects.eclipse.org/projects/locationtech.jts
 [JTS]: https://github.com/locationtech/jts
 [postgis-jdbc]: https://github.com/postgis/postgis-java