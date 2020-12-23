package com.github.lonelyleaf.gis.rest;

import com.github.lonelyleaf.gis.dto.FeatureCollections;
import com.github.lonelyleaf.gis.dto.GpsDto;
import com.github.lonelyleaf.gis.entity.GpsEntity;
import com.github.lonelyleaf.gis.mapper.GpsMapper;
import com.github.lonelyleaf.gis.service.GpsService;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import org.geolatte.geom.G2D;
import org.geolatte.geom.MultiPoint;
import org.geolatte.geom.Point;
import org.geolatte.geom.json.GeoJsonFeature;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/gps")
@Validated
@Api(description = "轨迹数据")
public class GpsController {

    private final GpsService gpsService;
    private final GpsMapper gpsMapper;

    public GpsController(GpsService gpsService, GpsMapper gpsMapper) {
        this.gpsService = gpsService;
        this.gpsMapper = gpsMapper;
    }

    @GetMapping("/history")
    public List<GpsDto> history(
            @RequestParam(value = "devId", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String devId,
            @RequestParam(value = "bTime", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date bTime,
            @RequestParam(value = "eTime", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date eTime) {
        return gpsService.history(devId, bTime, eTime).stream()
                .map(gpsMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/history/geojson")
    public MultiPoint<G2D> historyGeojson(
            @RequestParam(value = "devId", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String devId,
            @RequestParam(value = "bTime", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date bTime,
            @RequestParam(value = "eTime", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date eTime) {
        List<GpsEntity> history = gpsService.history(devId, bTime, eTime);
        List<Point<G2D>> features = history.stream().map(GpsEntity::getLocation)
                .collect(Collectors.toList());

        Point<G2D>[] array = new Point[features.size()];
        MultiPoint<G2D> multiPoint = new MultiPoint<>(features.toArray(array));
        return multiPoint;
    }


    @GetMapping("/history/geojson/points")
    public FeatureCollections<G2D, Integer> historyGeojsonPoints(
            @RequestParam(value = "devId", required = false, defaultValue = "0004r")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String devId,
            @RequestParam(value = "bTime", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date bTime,
            @RequestParam(value = "eTime", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date eTime) {

        List<GpsEntity> history = gpsService.history(devId, bTime, eTime);
        List<GeoJsonFeature<G2D, Integer>> features = history.stream().map(gpsEntity -> {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("devId", gpsEntity.getDevId());
            return new GeoJsonFeature<G2D, Integer>(gpsEntity.getLocation(), gpsEntity.getGid(), map);
        }).collect(Collectors.toList());
        return new FeatureCollections<G2D, Integer>(features);
    }


//    @GetMapping("/line")
//    public GpsLine line(
//            @NotEmpty
//            @RequestParam(value = "devId", required = false)
//            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String devId,
//            @RequestParam(value = "bTime", required = false)
//            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date bTime,
//            @RequestParam(value = "eTime", required = false)
//            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date eTime) {
//
//        List<GpsEntity> history = gpsService.history(devId, bTime, eTime);
//        GpsLine gpsLine = new GpsLine();
//        gpsLine.setDevId(devId);
//        gpsLine.setStart(bTime);
//        gpsLine.setEnd(eTime);
//
//        Coordinate[] points = history.stream()
//                .map(entity -> entity.getLocation().getCoordinate())
//                .toArray(Coordinate[]::new);
//
//        gpsLine.setLine(JtsUtil.geometryFactory4326.createLineString(points));
//        return gpsLine;
//    }

//    @GetMapping("/history/simple")
//    public List<SimplePoint> simpleHistory(
//            @RequestParam(value = "devId", required = false)
//            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String devId,
//            @RequestParam(value = "bTime", required = false)
//            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date bTime,
//            @RequestParam(value = "eTime", required = false)
//            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date eTime) {
//        List<GpsEntity> history = gpsService.history(devId, bTime, eTime);
//        return history.stream()
//                .map(gpsEntity -> new SimplePoint(gpsEntity.getLocation()))
//                .collect(Collectors.toList());
//    }

    @PostMapping
    public String simpleHistory(@RequestBody GpsDto gpsDto) {
        if (gpsDto.getTime() == null) {
            gpsDto.setTime(new Date());
        }
        if (Strings.isNullOrEmpty(gpsDto.getDevId())) {
            throw new IllegalArgumentException("devId不能为空");
        }

        GpsEntity entity = gpsMapper.toEntity(gpsDto);
        gpsService.save(entity);
        return "ok";
    }

}
