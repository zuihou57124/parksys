package com.qcw.parksys.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcw.parksys.common.utils.PageUtils;
import com.qcw.parksys.common.utils.Query;

import com.qcw.parksys.dao.PositionDao;
import com.qcw.parksys.entity.PositionEntity;
import com.qcw.parksys.service.PositionService;


@Service("positionService")
public class PositionServiceImpl extends ServiceImpl<PositionDao, PositionEntity> implements PositionService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PositionEntity> page = this.page(
                new Query<PositionEntity>().getPage(params),
                new QueryWrapper<PositionEntity>()
        );

        return new PageUtils(page);
    }

}