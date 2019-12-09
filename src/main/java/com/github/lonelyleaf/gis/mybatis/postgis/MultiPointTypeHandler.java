package com.github.lonelyleaf.gis.mybatis.postgis;

import org.apache.ibatis.type.MappedTypes;
import org.postgis.MultiPoint;

@MappedTypes(MultiPoint.class)
public class MultiPointTypeHandler extends AbstractGeometryTypeHandler<MultiPoint>{
}
