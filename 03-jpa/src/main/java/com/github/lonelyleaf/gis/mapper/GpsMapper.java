package com.github.lonelyleaf.gis.mapper;


import com.github.lonelyleaf.gis.dto.GpsDto;
import com.github.lonelyleaf.gis.dto.SimplePoint;
import com.github.lonelyleaf.gis.entity.GpsEntity;
import com.github.lonelyleaf.gis.util.JtsUtil;
import org.geolatte.geom.G2D;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.Point;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import static org.geolatte.geom.builder.DSL.g;
import static org.geolatte.geom.builder.DSL.point;

@Mapper
public interface GpsMapper {

    GpsDto toDto(GpsEntity entity);

    @Mappings({
            @Mapping(ignore = true, target = "gid")
    })
    GpsEntity toEntity(GpsDto entity);

    default SimplePoint toSimple(Point point) {
        return new SimplePoint(point);
    }

    default SimplePoint toSimple(org.geolatte.geom.Point<G2D> point) {
        return new SimplePoint(point.getPosition().getLat(), point.getPosition().getLon());
    }

    default org.geolatte.geom.Point<G2D> toGeolattePoint(SimplePoint point) {
        G2D g = g(point.getX(), point.getY());
        return point(JtsUtil.WGS84, g);
    }

    default Point toGisPoint(SimplePoint point) {
        Coordinate coordinate = new CoordinateXY(point.getX(), point.getY());
        return JtsUtil.geometryFactory4326.createPoint(coordinate);
    }

}
