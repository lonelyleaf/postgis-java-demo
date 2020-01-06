package com.github.lonelyleaf.gis.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.lonelyleaf.gis.entity.GpsEntity;
import com.github.lonelyleaf.gis.util.JtsUtil;
import com.google.common.base.Strings;
import org.locationtech.jts.geom.MultiPoint;
import org.springframework.stereotype.Service;
import com.github.lonelyleaf.gis.repo.GpsRepo;

import java.util.Date;
import java.util.List;

@Service
public class GpsService extends ServiceImpl<GpsRepo, GpsEntity> {

    public List<GpsEntity> history(String devId, Date bTime, Date eTime) {
        QueryWrapper<GpsEntity> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("time");
        if (!Strings.isNullOrEmpty(devId)) {
            wrapper.eq("dev_id", devId);
        }
        if (bTime != null) {
            wrapper.ge("time", bTime);
        }
        if (eTime != null) {
            wrapper.le("time", eTime);
        }
        return list(wrapper);
    }

}
