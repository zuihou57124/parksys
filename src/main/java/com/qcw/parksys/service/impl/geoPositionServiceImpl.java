package com.qcw.parksys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcw.parksys.common.utils.PageUtils;
import com.qcw.parksys.common.utils.Query;
import com.qcw.parksys.dao.GeoPositionDao;
import com.qcw.parksys.dao.PositionDao;
import com.qcw.parksys.entity.GeoPosition;
import com.qcw.parksys.entity.PositionEntity;
import com.qcw.parksys.service.GeoPositionService;
import com.qcw.parksys.service.PositionService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("geoPositionService")
public class geoPositionServiceImpl extends ServiceImpl<GeoPositionDao, GeoPosition> implements GeoPositionService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<GeoPosition> page = this.page(
                new Query<GeoPosition>().getPage(params),
                new QueryWrapper<GeoPosition>()
        );

        return new PageUtils(page);
    }

}