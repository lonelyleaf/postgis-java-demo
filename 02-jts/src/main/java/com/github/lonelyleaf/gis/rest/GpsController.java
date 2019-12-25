package com.github.lonelyleaf.gis.rest;

import com.github.lonelyleaf.gis.dto.GpsDto;
import com.github.lonelyleaf.gis.dto.GpsHistory;
import com.github.lonelyleaf.gis.dto.SimplePoint;
import com.github.lonelyleaf.gis.entity.GpsEntity;
import com.github.lonelyleaf.gis.mapper.GpsMapper;
import com.github.lonelyleaf.gis.service.GpsService;
import com.github.lonelyleaf.gis.util.JtsUtil;
import com.google.common.base.Strings;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/gps")
@Validated
public class GpsController {

    private final GpsService gpsService;
    private final GpsMapper gpsMapper;

    public GpsController(GpsService gpsService, GpsMapper gpsMapper) {
        this.gpsService = gpsService;
        this.gpsMapper = gpsMapper;
    }

    @GetMapping("/history")
    public GpsHistory history(
            @NotEmpty
            @RequestParam(value = "devId", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String devId,
            @RequestParam(value = "bTime", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date bTime,
            @RequestParam(value = "eTime", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date eTime) {


        List<GpsEntity> history = gpsService.history(devId, bTime, eTime);
        GpsHistory gpsHistory = new GpsHistory();
        gpsHistory.setDevId(devId);
        gpsHistory.setStart(bTime);
        gpsHistory.setEnd(eTime);

        Point[] points = history.stream()
                .map(GpsEntity::getLocation)
                .toArray(Point[]::new);

        MultiPoint multiPoint = JtsUtil.geometryFactory4326.createMultiPoint(points);
        gpsHistory.setPoints(multiPoint);
        return gpsHistory;
    }

    @GetMapping("/history/simple")
    public List<SimplePoint> simpleHistory(
            @RequestParam(value = "devId", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String devId,
            @RequestParam(value = "bTime", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date bTime,
            @RequestParam(value = "eTime", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date eTime) {
        List<GpsEntity> history = gpsService.history(devId, bTime, eTime);
        return history.stream()
                .map(gpsEntity -> new SimplePoint(gpsEntity.getLocation()))
                .collect(Collectors.toList());
    }

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
