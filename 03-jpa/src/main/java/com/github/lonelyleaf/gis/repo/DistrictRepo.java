package com.github.lonelyleaf.gis.repo;


import com.github.lonelyleaf.gis.entity.DistrictEntity;
import com.github.lonelyleaf.gis.entity.GpsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * 行政区仓储
 */
public interface DistrictRepo extends JpaRepository<DistrictEntity, Integer>, QuerydslPredicateExecutor<DistrictEntity> {

}
