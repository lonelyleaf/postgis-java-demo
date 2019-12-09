package xyz.lonelyleaf.gis.mybatis.gis;

import org.apache.ibatis.type.MappedTypes;
import org.postgis.LineString;

@MappedTypes(LineString.class)
public class LineStringTypeHandler extends AbstractGeometryTypeHandler<LineString> {
}
