package com.github.lonelyleaf.gis.rest;

import com.github.lonelyleaf.gis.dto.FeatureCollections;
import com.github.lonelyleaf.gis.service.RoadService;
import io.swagger.annotations.Api;
import org.geolatte.geom.G2D;
import org.geolatte.geom.json.GeoJsonFeature;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/roads")
@Validated
@Api(description = "路网数据")
public class RoadController {

    private final RoadService roadService;

    public RoadController(RoadService roadService) {
        this.roadService = roadService;
    }

    @GetMapping("")
    public FeatureCollections<G2D, Integer> history() {
        List<GeoJsonFeature<G2D, Integer>> features = roadService.findAll().stream().map(roadEntity -> {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("name", roadEntity.getName());
            return new GeoJsonFeature<G2D, Integer>(roadEntity.getGeom(), roadEntity.getGid(), map);
        }).collect(Collectors.toList());
        return new FeatureCollections<G2D, Integer>(features);
    }

}
