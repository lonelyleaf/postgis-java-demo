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

package com.github.lonelyleaf.gis.repo;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.Point;

/**
 * 详细内容请参考
 * <p>
 * https://postgis.net/docs/manual-dev/reference.html
 */
@org.apache.ibatis.annotations.Mapper
public interface GisRepo {

    /**
     * @return 返回单位与srid有关，如果是wgs84，那么单位会是度
     */
    @Select("select ST_Distance(#{a},#{b})")
    double stDistance(@Param("a") Geometry a, @Param("b") Geometry b);

    /**
     * @return 返回的单位是米
     */
    @Select("select ST_Distance(#{a}::geography,#{b}::geography)")
    double stDistanceGeography(@Param("a") Geometry a, @Param("b") Geometry b);

    /**
     * https://postgis.net/docs/ST_DistanceSphere.html
     */
    @Select("select ST_DistanceSphere(#{a},#{b})")
    double stDistanceSphere(@Param("a") Geometry a, @Param("b") Geometry b);

    /**
     * https://postgis.net/docs/ST_DistanceSphere.html
     */
    @Select("select ST_LineMerge(#{a})")
    LineString stLineMerge(@Param("a") MultiLineString a);

    @Select("with measure as (select roads.gid                                          as road_gid,\n" +
            "                        ST_Distance(#{point}, roads.geom)                  as distance,\n" +
            "                        ST_LineLocatePoint(roads.geom::geometry, #{point}) AS measure\n" +
            "                 from t_zunyi_roads roads\n" +
            "                 where st_dwithin(roads.geom, #{point}, #{roadDistance}, false)\n" +
            "                 order by roads.gid, distance\n" +
            "                 limit 1)\n" +
            "select ST_LineInterpolatePoint(roads.geom::geometry, measure.measure)\n" +
            "from measure\n" +
            "         left join t_zunyi_roads roads on measure.road_gid = roads.gid")
    Point bindRoad(@Param("point") Point point,@Param("roadDistance") double roadDistance);

}
