package xyz.lonelyleaf.gis.rest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Strings;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.lonelyleaf.gis.dto.SimplePoint;
import xyz.lonelyleaf.gis.entity.GpsEntity;
import xyz.lonelyleaf.gis.service.GpsService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/gps")
public class GpsController {

    private final GpsService gpsService;

    public GpsController(GpsService gpsService) {
        this.gpsService = gpsService;
    }

    @GetMapping("/history")
    public List<GpsEntity> history(
            @RequestParam(value = "devId", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String devId,
            @RequestParam(value = "bTime", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date bTime,
            @RequestParam(value = "eTime", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date eTime) {
        return gpsService.history(devId, bTime, eTime);
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
