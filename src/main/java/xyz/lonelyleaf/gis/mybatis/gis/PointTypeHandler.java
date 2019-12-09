package xyz.lonelyleaf.gis.mybatis.gis;

import org.apache.ibatis.type.MappedTypes;
import org.postgis.Point;

@MappedTypes(Point.class)
public class PointTypeHandler extends AbstractGeometryTypeHandler<Point> {
}
