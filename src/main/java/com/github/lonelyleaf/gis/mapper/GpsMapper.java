package com.github.lonelyleaf.gis.mapper;


import com.github.lonelyleaf.gis.dto.GpsDto;
import com.github.lonelyleaf.gis.dto.SimplePoint;
import com.github.lonelyleaf.gis.entity.GpsEntity;

@org.mapstruct.Mapper
public interface GpsMapper {

    GpsDto toDto(GpsEntity entity);

    default SimplePoint toSimple(org.postgis.Point point) {
        return new SimplePoint(point);
    }

}
