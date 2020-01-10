/*
 * Copyright [2019] [lonelyleaf]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.lonelyleaf.gis.rest;

import com.github.lonelyleaf.gis.entity.DistrictEntity;
import com.github.lonelyleaf.gis.service.DistrictService;
import com.github.lonelyleaf.gis.util.JtsUtil;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.locationtech.jts.geom.Point;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/district")
@Validated
@Api(description = "行政区与边界")
public class DistrictController {

    private final DistrictService districtService;

    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

//    @GetMapping
//    @ApiResponses(
//            {@ApiResponse(code = 200, message = "geom=false时，不会携带geom字段")}
//    )
//    public List<? extends DistrictEntity> list(
//            @ApiParam("行政区名，支持like")
//            @RequestParam(name = "name", required = false) String name,
//            @ApiParam("层级，省，市，区县。多个用逗号分隔")
//            @RequestParam(name = "layers", required = false) String layersStr,
//            @ApiParam("是否携带geom对象,geom数据量很大，一般查询可以不携带")
//            @RequestParam(name = "geom", required = false, defaultValue = "false") boolean geom) {
//        List<String> layers = Collections.emptyList();
//        if (!Strings.isNullOrEmpty(layersStr)) {
//            layers = Arrays.asList(layersStr.split(","));
//        }
//        return districtService.findBy(name, layers, geom);
//    }
//
//    @GetMapping("/within")
//    public List<? extends DistrictEntity> within(
//            @ApiParam("点，x和y，逗号分隔")
//            @RequestParam(name = "point", required = false) String pointStr,
//            @ApiParam("是否携带geom对象,geom数据量很大，一般查询可以不携带")
//            @RequestParam(name = "geom", required = false, defaultValue = "false") boolean geom) {
//        String[] pointArray = pointStr.split(",");
//        Point point = JtsUtil.newPoint(Double.parseDouble(pointArray[0]), Double.parseDouble(pointArray[1]));
//        return districtService.withIn(point, geom);
//    }

}
