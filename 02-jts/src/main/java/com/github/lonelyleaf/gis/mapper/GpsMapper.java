package com.github.lonelyleaf.gis.mapper;


import com.github.lonelyleaf.gis.dto.GpsDto;
import com.github.lonelyleaf.gis.dto.SimplePoint;
import com.github.lonelyleaf.gis.entity.GpsEntity;
import com.github.lonelyleaf.gis.util.JtsUtil;
import org.locationtech.jts.geom.*;

@org.mapstruct.Mapper
public interface GpsMapper {

    GpsDto toDto(GpsEntity entity);

    GpsEntity toEntity(GpsDto entity);

    default SimplePoint toSimple(Point point) {
        return new SimplePoint(point);
    }

    default Point toGisPoint(SimplePoint point) {
        Coordinate coordinate = new CoordinateXY(point.getX(), point.getY());
        return JtsUtil.geometryFactory4326.createPoint(coordinate);
    }

}
