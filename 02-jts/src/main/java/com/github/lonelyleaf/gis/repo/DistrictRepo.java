package com.github.lonelyleaf.gis.repo;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.lonelyleaf.gis.entity.DistrictEntity;
import com.github.lonelyleaf.gis.entity.DistrictGeomEntity;
import org.apache.ibatis.annotations.Param;
import org.locationtech.jts.geom.Geometry;

import java.util.List;

/**
 * 行政区仓储
 */
public interface DistrictRepo extends BaseMapper<DistrictEntity> {

}
