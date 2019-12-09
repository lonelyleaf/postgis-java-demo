package com.github.lonelyleaf.gis.mybatis.postgis;

import org.apache.ibatis.type.MappedTypes;
import org.postgis.Point;

@MappedTypes(Point.class)
public class PointTypeHandler extends AbstractGeometryTypeHandler<Point> {
}
