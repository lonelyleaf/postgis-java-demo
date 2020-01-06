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
import com.github.lonelyleaf.gis.entity.DistrictGeomEntity;
import com.github.lonelyleaf.gis.mybatis.wrapper.PostgisWrapper;
import com.github.lonelyleaf.gis.repo.DistrictGeomRepo;
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
    DistrictGeomRepo districtGeomRepo;
    @Autowired
    GisRepo gisRepo;

    private DistrictEntity guiyangDistrict = new DistrictEntity(2, "贵阳市", "市", "112520100000000");
    private DistrictEntity zunyiDistrict = new DistrictEntity(4, "遵义市", "市", "112520300000000");

    /**
     * 查找该坐标在哪个市
     */
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

    /**
     * 查找贵阳市内的行政区
     */
    @Test
    public void testStWithIn2() {
        PostgisWrapper<DistrictGeomEntity> wrapper = new PostgisWrapper<>();
        wrapper.eq("layer", "市");
        wrapper.eq("name", "贵阳市");

        List<DistrictGeomEntity> geomEntities = districtGeomRepo.selectList(wrapper);
        Assert.assertEquals(1, geomEntities.size());
        DistrictGeomEntity guiyang = geomEntities.get(0);
        Assert.assertEquals("贵阳市", guiyang.getName());


        PostgisWrapper<DistrictEntity> wrapper1 = new PostgisWrapper<>();
        wrapper1.stWithIn("geom", guiyang.getGeom());
        List<DistrictEntity> list = districtRepo.selectList(wrapper1);
        for (DistrictEntity districtEntity : list) {
            log.info(districtEntity.toString());
        }
        String[] results = new String[]{"贵阳市", "南明区", "云岩区", "花溪区", "乌当区",
                "白云区", "观山湖区", "息烽县", "开阳县", "修文县", "清镇市"};

        for (String result : results) {
            DistrictEntity entity = list.stream().filter(input -> input.getName().equals(result)).findFirst().get();
            Assert.assertNotNull("数据不全，未包含 " + result, entity);
        }

    }

    @Test
    public void testStDistance() {
        Point a = JtsUtil.newPoint(106.677, 26.572);
        Point c = JtsUtil.newPoint(106.433, 29.524);
        //这里返回的距离单位是度，并且并未计算两点的球面距离
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

    @Test
    public void testBindRoad() {
        Point point = JtsUtil.newPoint(106.873251, 27.530133);
        Point lineRef = gisRepo.bindRoad(point, 30);
        log.info(lineRef.toString());
    }

}
