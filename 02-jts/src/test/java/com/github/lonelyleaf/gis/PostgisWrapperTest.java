/*
 * Copyright [2020] [lonelyleaf]
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

package com.github.lonelyleaf.gis;

import com.github.lonelyleaf.gis.entity.DistrictEntity;
import com.github.lonelyleaf.gis.mybatis.wrapper.PostgisWrapper;
import com.github.lonelyleaf.gis.repo.DistrictRepo;
import com.github.lonelyleaf.gis.repo.GisRepo;
import com.github.lonelyleaf.gis.util.JtsUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JtsGisDemoApplication.class)
@Slf4j
public class PostgisWrapperTest {

    @Autowired
    DistrictRepo districtRepo;
    @Autowired
    GisRepo gisRepo;

    private DistrictEntity guiyangDistrict = new DistrictEntity(2, "贵阳市", "市", "112520100000000");
    private DistrictEntity zunyiDistrict = new DistrictEntity(4, "遵义市", "市", "112520300000000");

    @Test
    public void testStWithIn() {
        Point point = JtsUtil.newPoint(106.677, 26.572);
        PostgisWrapper<DistrictEntity> wrapper = new PostgisWrapper<>();
        wrapper.eq("layer", "市");
        wrapper.stWithIn(point, "geom");

        List<DistrictEntity> list = districtRepo.selectList(wrapper);
        for (DistrictEntity entity : list) {
            log.info(entity.toString());
        }
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(guiyangDistrict, list.get(0));
    }

    @Test
    public void testStDistance() {
        Point a = JtsUtil.newPoint(106.677, 26.572);
        Point c = JtsUtil.newPoint(106.433, 29.524);
        //这里返回的距离是度，并且并未计算两点的球面距离
        double distance1 = gisRepo.stDistance(a, c);
        Assert.assertEquals(2.9620668459709028, distance1, 0.000001);
        log.info("距离{}", distance1);
        //这里返回的距离是米
        double distance2 = gisRepo.stDistanceGeography(a, c);
        Assert.assertEquals(328019.76934956, distance2, 0.000000001);
        log.info("距离{}", distance2);
        //这里返回的距离是米
        double distance3 = gisRepo.stDistanceGeography(a, c);
        Assert.assertEquals(328019.76934956, distance3, 0.000000001);
        log.info("距离{}", distance2);
    }

    @Test
    public void testStDWithIn() {
        //这个点是重庆
        Point a = JtsUtil.newPoint(106.433, 29.524);
        PostgisWrapper<DistrictEntity> wrapper = new PostgisWrapper<>();
        wrapper.eq("layer", "市");
//        wrapper.stDWithInGeography(a, "geom",328019);
        //遵义的北部距重庆不到100km
        wrapper.stDWithInGeography(a, "geom", 100000);

        List<DistrictEntity> list = districtRepo.selectList(wrapper);
        for (DistrictEntity entity : list) {
            log.info(entity.toString());
        }
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(zunyiDistrict, list.get(0));
    }

}
