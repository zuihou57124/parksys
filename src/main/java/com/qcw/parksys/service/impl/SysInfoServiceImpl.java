package com.qcw.parksys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcw.parksys.common.myconst.MyConst;
import com.qcw.parksys.common.utils.PageUtils;
import com.qcw.parksys.common.utils.Query;
import com.qcw.parksys.dao.OrderDao;
import com.qcw.parksys.dao.SysInfoDao;
import com.qcw.parksys.entity.*;
import com.qcw.parksys.service.*;
import com.qcw.parksys.vo.SysInfoVo;
import com.qcw.parksys.vo.UserBooksVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service("sysInfoService")
public class SysInfoServiceImpl extends ServiceImpl<SysInfoDao, SysInfoEntity> implements SysInfoService {

    @Autowired
    PositionService positionService;

    @Autowired
    TypeService typeService;

    @Autowired
    UserService userService;

    @Autowired
    VipService vipService;

    @Autowired
    SpaceService spaceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SysInfoEntity> page = this.page(
                new Query<SysInfoEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    /**
     * @param params
     * @return
     * 按照用户id查询系统消息
     */
    @Override
    public PageUtils getSysInfoList(Map<String, Object> params) {

        Object userId = params.get("userId");

        QueryWrapper<SysInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        wrapper.eq("readed",0);

        IPage<SysInfoEntity> page = this.page(
                new Query<SysInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }


}