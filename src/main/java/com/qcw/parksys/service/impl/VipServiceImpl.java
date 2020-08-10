package com.qcw.parksys.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcw.parksys.common.utils.PageUtils;
import com.qcw.parksys.common.utils.Query;

import com.qcw.parksys.dao.VipDao;
import com.qcw.parksys.entity.VipEntity;
import com.qcw.parksys.service.VipService;


@Service("vipService")
public class VipServiceImpl extends ServiceImpl<VipDao, VipEntity> implements VipService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<VipEntity> page = this.page(
                new Query<VipEntity>().getPage(params),
                new QueryWrapper<VipEntity>()
        );

        return new PageUtils(page);
    }

}