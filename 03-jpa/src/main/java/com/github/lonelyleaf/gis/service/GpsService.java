package com.github.lonelyleaf.gis.service;

import com.github.lonelyleaf.gis.entity.GpsEntity;
import com.github.lonelyleaf.gis.repo.GpsRepo;
import com.google.common.collect.Lists;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.github.lonelyleaf.gis.entity.QGpsEntity.gpsEntity;

@Service
public class GpsService {

    @Autowired
    GpsRepo gpsRepo;

    public List<GpsEntity> history(String devId, Date bTime, Date eTime) {
        Predicate predicate = gpsEntity.devId.eq(devId);
        if (bTime != null) {
            predicate = ExpressionUtils.and(predicate, gpsEntity.time.gt(bTime));
        }
        if (eTime != null) {
            predicate = ExpressionUtils.and(predicate, gpsEntity.time.lt(eTime));
        }
        return Lists.newArrayList(gpsRepo.findAll(predicate));
    }

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    public List<GpsEntity> dWithIn(@NotNull Point point, double distance) {
        return Collections.emptyList();
//        jpaQueryFactory.select(gpsEntity).dis
//        new JPAQuery<GpsEntity>()
//        gpsEntity.lo
//        Predicate predicate = gpsEntity.devId.eq(devId);
//        if (bTime != null) {
//            predicate = ExpressionUtils.and(predicate, gpsEntity.time.gt(bTime));
//        }
//        if (eTime != null) {
//            predicate = ExpressionUtils.and(predicate, gpsEntity.time.lt(eTime));
//        }
//        return Lists.newArrayList(gpsRepo.findAll(predicate));
    }

    public void save(GpsEntity entity) {
        entity.setGid(null);
        gpsRepo.save(entity);
    }

}
