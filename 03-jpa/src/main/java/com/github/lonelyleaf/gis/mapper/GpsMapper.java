package com.github.lonelyleaf.gis.mapper;


import com.github.lonelyleaf.gis.dto.GpsDto;
import com.github.lonelyleaf.gis.dto.SimplePoint;
import com.github.lonelyleaf.gis.entity.GpsEntity;
import com.github.lonelyleaf.gis.util.JtsUtil;
import org.locationtech.jts.geom.*;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@org.mapstruct.Mapper
public interface GpsMapper {

    GpsDto toDto(GpsEntity entity);

    @Mappings({
            @Mapping(ignore = true, target = "gid")
    })
    GpsEntity toEntity(GpsDto entity);

    default SimplePoint toSimple(Point point) {
        return new SimplePoint(point);
    }

    default org.geolatte.geom.Point toGeolattePoint(SimplePoint point) {
        return org.geolatte.geom.Point.createEmpty();
//        Coordinate coordinate = new CoordinateXY(point.getX(), point.getY());
//        return JtsUtil.geometryFactory4326.createPoint(coordinate);
    }

    default Point toGisPoint(SimplePoint point) {
        Coordinate coordinate = new CoordinateXY(point.getX(), point.getY());
        return JtsUtil.geometryFactory4326.createPoint(coordinate);
    }

}
