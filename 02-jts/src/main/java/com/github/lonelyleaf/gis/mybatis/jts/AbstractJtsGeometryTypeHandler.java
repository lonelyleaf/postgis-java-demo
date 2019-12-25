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

package com.github.lonelyleaf.gis.mybatis.jts;

import com.github.lonelyleaf.gis.db.jts.JtsGeometry;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.locationtech.jts.geom.Geometry;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unchecked")
public abstract class AbstractJtsGeometryTypeHandler<T extends Geometry> extends BaseTypeHandler<T> {

    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, new JtsGeometry(parameter));
    }

    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        JtsGeometry jtsGeometry = (JtsGeometry) rs.getObject(columnName);
        if (jtsGeometry == null) {
            return null;
        }
        return (T) jtsGeometry.getGeometry();
    }

    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        JtsGeometry jtsGeometry = (JtsGeometry) rs.getObject(columnIndex);
        if (jtsGeometry == null) {
            return null;
        }
        return (T) jtsGeometry.getGeometry();
    }

    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        JtsGeometry jtsGeometry = (JtsGeometry) cs.getObject(columnIndex);
        if (jtsGeometry == null) {
            return null;
        }
        return (T) jtsGeometry.getGeometry();
    }

}
