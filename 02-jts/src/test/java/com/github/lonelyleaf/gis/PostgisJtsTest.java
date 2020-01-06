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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.lonelyleaf.gis.entity.GpsEntity;
import com.github.lonelyleaf.gis.repo.GpsRepo;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JtsGisDemoApplication.class)
@Slf4j
public class PostgisJtsTest {

    @Autowired
    GpsRepo gpsRepo;

    @Test
    public void testReadData() {
        QueryWrapper<GpsEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("time");
        wrapper.eq("dev_id", "0004r");
        wrapper.last("limit 100");
        List<GpsEntity> list = gpsRepo.selectList(wrapper);
        for (GpsEntity entity : list) {
            System.out.println(entity);
        }
    }

}
