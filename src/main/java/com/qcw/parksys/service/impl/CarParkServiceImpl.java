package com.qcw.parksys.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcw.parksys.common.utils.PageUtils;
import com.qcw.parksys.common.utils.Query;

import com.qcw.parksys.dao.CarParkDao;
import com.qcw.parksys.vo.PositionVo;
import com.qcw.parksys.service.CarParkService;


@Service("carParkService")
public class CarParkServiceImpl extends ServiceImpl<CarParkDao, PositionVo> implements CarParkService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PositionVo> page = this.page(
                new Query<PositionVo>().getPage(params),
                new QueryWrapper<PositionVo>()
        );

        return new PageUtils(page);
    }

}