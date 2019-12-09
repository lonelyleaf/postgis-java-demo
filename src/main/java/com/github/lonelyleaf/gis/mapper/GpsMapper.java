package com.github.lonelyleaf.gis.mapper;


import com.github.lonelyleaf.gis.dto.GpsDto;
import com.github.lonelyleaf.gis.dto.SimplePoint;
import com.github.lonelyleaf.gis.entity.GpsEntity;
import org.postgis.Point;

@org.mapstruct.Mapper
public interface GpsMapper {

    GpsDto toDto(GpsEntity entity);

    GpsEntity toEntity(GpsDto entity);

    default SimplePoint toSimple(org.postgis.Point point) {
        return new SimplePoint(point);
    }

    default org.postgis.Point toGisPoint(SimplePoint point) {
        Point gisPoint = new Point();
        gisPoint.x = point.getX();
        gisPoint.y = point.getY();
        gisPoint.dimension = 2;
        //WGS84坐标系，也就是GPS使用的坐标
        gisPoint.srid = 4326;
        return gisPoint;
    }

}
