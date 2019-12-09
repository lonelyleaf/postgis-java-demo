package com.github.lonelyleaf.gis.mybatis.postgis;

import org.apache.ibatis.type.MappedTypes;
import org.postgis.LineString;

@MappedTypes(LineString.class)
public class LineStringTypeHandler extends AbstractGeometryTypeHandler<LineString> {
}
