package com.github.lonelyleaf.gis.repo;

import com.github.lonelyleaf.gis.entity.RoadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoadRepo extends JpaRepository<RoadEntity, Integer> {
}
