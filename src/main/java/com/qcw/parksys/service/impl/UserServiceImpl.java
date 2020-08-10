package com.qcw.parksys.service.impl;

import com.qcw.parksys.entity.OrderEntity;
import com.qcw.parksys.entity.SysInfoEntity;
import com.qcw.parksys.service.OrderService;
import com.qcw.parksys.service.SpaceService;
import com.qcw.parksys.vo.SysInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcw.parksys.common.utils.PageUtils;
import com.qcw.parksys.common.utils.Query;

import com.qcw.parksys.dao.UserDao;
import com.qcw.parksys.entity.UserEntity;
import com.qcw.parksys.service.UserService;


@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {


    @Autowired
    OrderService orderService;

    @Autowired
    SpaceService spaceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserEntity> page = this.page(
                new Query<UserEntity>().getPage(params),
                new QueryWrapper<UserEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * @param params
     * @return
     * 获取车位到期，预约到期等信息列表
     */
    @Override
    public List<SysInfoEntity> getSysInfos(Map<String, Object> params) {

        //获取即将到期的订单信息 vo
        List<SysInfoEntity> sysInfos  = orderService.getWillValidOrdersByUserIdAndToSysInfo(params);

        return sysInfos;
    }

}