package xyz.lonelyleaf.gis.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.lonelyleaf.gis.entity.GpsEntity;
import xyz.lonelyleaf.gis.repo.GpsRepo;

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
