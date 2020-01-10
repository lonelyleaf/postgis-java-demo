package com.github.lonelyleaf.gis.repo;


import com.github.lonelyleaf.gis.entity.GpsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * GPS相关
 */
public interface GpsRepo extends JpaRepository<GpsEntity, Integer>, QuerydslPredicateExecutor<GpsEntity> {
}
