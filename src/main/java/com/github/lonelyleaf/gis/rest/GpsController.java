package com.github.lonelyleaf.gis.rest;

import com.github.lonelyleaf.gis.dto.GpsDto;
import com.github.lonelyleaf.gis.dto.SimplePoint;
import com.github.lonelyleaf.gis.entity.GpsEntity;
import com.github.lonelyleaf.gis.mapper.GpsMapper;
import com.github.lonelyleaf.gis.service.GpsService;
import com.vividsolutions.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/gps")
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


}
