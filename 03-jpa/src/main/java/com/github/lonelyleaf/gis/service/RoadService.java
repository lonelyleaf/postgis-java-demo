package com.github.lonelyleaf.gis.service;

import com.github.lonelyleaf.gis.entity.RoadEntity;
import com.github.lonelyleaf.gis.repo.RoadRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoadService {

    @Autowired
    RoadRepo roadRepo;

    public List<RoadEntity> findAll() {
        return roadRepo.findAll();
    }

}
