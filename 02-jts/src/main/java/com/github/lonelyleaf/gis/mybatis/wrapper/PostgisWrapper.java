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

package com.github.lonelyleaf.gis.mybatis.wrapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.locationtech.jts.geom.Geometry;

import java.util.ArrayList;
import java.util.List;

public class PostgisWrapper<T> extends QueryWrapper<T> {

    /**
     * 返回a是否完全在b图形中
     *
     * @param a 图形a,{@link Geometry}或表字段名
     * @param b 图形b,{@link Geometry}或表字段名
     * @see <a href="https://postgis.net/docs/ST_Within.html">ST_Within</a>
     */
    public QueryWrapper<T> stWithIn(Object a, Object b) {
        return gisFunction("ST_Within(%s,%s)", a, b);
    }

    /**
     * 返回a是否在b图形的指定范围中，要精确计算地理位置坐标关系，使用{@link #stDWithInGeography}
     *
     * @param a        图形a,{@link Geometry}或表字段名
     * @param b        图形b,{@link Geometry}或表字段名
     * @param distance 距离单位取决于srid
     * @see <a href="https://postgis.net/docs/ST_DWithin.html">ST_DWithin</a>
     */
    public QueryWrapper<T> stDWithIn(Object a, Object b, double distance) {
        return gisFunction("ST_DWithin(%s,%s,%s)", a, b, distance);
    }

    /**
     * 返回a是否在b图形的指定范围中，使用的是地理坐标进行计算。
     *
     * @param a        图形a,{@link Geometry}或表字段名
     * @param b        图形b,{@link Geometry}或表字段名
     * @param distance 距离单位取决于图像的srid
     * @see <a href="https://postgis.net/docs/ST_DWithin.html">ST_DWithin</a>
     */
    public QueryWrapper<T> stDWithInGeography(Object a, Object b, double distance) {
        return gisFunction("ST_DWithin(%s::geography,%s::geography,%s,true)", a, b, distance);
    }

    /**
     * 拼接postgis function的sql，string类型参数会当作表字段
     *
     * @param functionSql sql模板，使用的{@link String#format}
     * @param params      {@link Geometry}或{@link String}.string类型参数会当作表字段
     */
    private QueryWrapper<T> gisFunction(String functionSql, Object... params) {
        Object[] paramHolders = new Object[params.length];
        List<Object> paramsList = new ArrayList<>(params.length);
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param instanceof String) {
                //字符串都当作表的字段
                //可能被注入，暂时没其它好办法
                paramHolders[i] = param;
            } else {
                paramHolders[i] = "{" + paramsList.size() + "}";
                paramsList.add(param);
            }
        }
        String sql = String.format(functionSql, paramHolders);
        return apply(sql, paramsList.toArray());
    }

}
