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

package com.github.lonelyleaf.gis.service;

import com.github.lonelyleaf.gis.entity.DistrictEntity;
import com.github.lonelyleaf.gis.entity.DistrictGeomEntity;
import com.github.lonelyleaf.gis.repo.DistrictRepo;
import com.google.common.base.Strings;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistrictService  {

    @Autowired
    DistrictRepo districtRepo;

//    @SuppressWarnings("unchecked")
//    public List<? extends DistrictEntity> findBy(String name, List<String> layers, boolean geom) {
//        QueryWrapper<? extends DistrictEntity> wrapper = new QueryWrapper<>();
//        if (!Strings.isNullOrEmpty(name)) {
//            wrapper.like("name", name);
//        }
//        if (layers != null && layers.size() != 0) {
//            wrapper.in("layer", layers);
//        }
//
//        if (geom) {
//            return districtGeomRepo.selectList((Wrapper<DistrictGeomEntity>) wrapper);
//        } else {
//            return list((Wrapper<DistrictEntity>) wrapper);
//        }
//    }
//
//    public List<? extends DistrictEntity> withIn(Point point, boolean geom) {
//        PostgisWrapper<? extends DistrictEntity> wrapper = new PostgisWrapper<>();
//        wrapper.stDWithIn(point, "geom", 5);
//
//        if (geom) {
//            return districtGeomRepo.selectList((Wrapper<DistrictGeomEntity>) wrapper);
//        } else {
//            return list((Wrapper<DistrictEntity>) wrapper);
//        }
//    }

}
