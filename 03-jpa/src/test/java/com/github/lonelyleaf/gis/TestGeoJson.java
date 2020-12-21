package com.github.lonelyleaf.gis;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

@Slf4j
class TestGeoJson {

    @Test
    public void test() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JtsModule());


        GeometryFactory gf = new GeometryFactory();
        Point point = gf.createPoint(new Coordinate(1.2345678, 2.3456789));
        String geojson = mapper.writeValueAsString(point);
        log.info(geojson);
    }

}
