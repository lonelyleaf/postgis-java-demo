# 使用jts与postgis进行空间数据操作

jts

## 找出轨迹经过的区县

```postgresql
select DISTINCT ON (boundary.name) name,
                                   boundary.layer,
                                   boundary.geom,
                                   gps.location
from t_guizhou_boundary boundary
         inner join t_gps gps
                    on st_within(gps.location, boundary.geom)
where layer = '区县'
```