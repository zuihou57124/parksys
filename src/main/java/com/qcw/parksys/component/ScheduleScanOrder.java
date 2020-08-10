package com.qcw.parksys.component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qcw.parksys.common.myconst.MyConst;
import com.qcw.parksys.entity.OrderEntity;
import com.qcw.parksys.entity.SpaceEntity;
import com.qcw.parksys.entity.SysInfoEntity;
import com.qcw.parksys.service.OrderService;
import com.qcw.parksys.service.SpaceService;
import com.qcw.parksys.service.SysInfoService;
import com.qcw.parksys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScheduleScanOrder {

    @Autowired
    OrderService orderService;

    @Autowired
    SpaceService spaceService;

    @Autowired
    UserService userService;

    @Autowired
    SysInfoService sysInfoService;

    /**
     * 定时扫描订单项,超过30分钟未支付的订单会被自动取消
     */
    @Transactional
    @Scheduled(cron = "*/30 * * * * ?") //测试使用 30秒 的间隔
    public void scanOrderAndCancel(){

        //获取所有新建状态的订单(即待付款的订单项)
        List<OrderEntity> orderList = orderService.list(new QueryWrapper<OrderEntity>().eq("status", MyConst.OrderStatus.CREATED.getCode()));

        orderList = orderList.stream().peek((order) -> {
            //订单创建时间
            Date createTime = order.getCreateTime();
            //当前时间
            Date currTime = new Date();
            long timestamp = currTime.getTime() - createTime.getTime();
            //间隔大于等于30分钟,取消订单
            if (timestamp / (1000) >= 300) {
                order.setStatus(MyConst.OrderStatus.TOKEN.getCode());
                Integer spaceId = order.getSpaceId();
                SpaceEntity space = spaceService.getOne(new QueryWrapper<SpaceEntity>().eq("id", spaceId));
                space.setStatus(MyConst.SpaceStatus.AVALIABLE.getCode());
                space.setNextTime(null);
                spaceService.updateById(space);
            }
        }).collect(Collectors.toList());

        orderService.updateBatchById(orderList);

    }

    /**
     * 定时扫描订单项,获取预约即将到期或者车位即将到期的订单,封装成消息实体类，保存到数据库
     */
    @Transactional
    @Scheduled(cron = "*/15 * * * * ?") //测试使用 30秒 的间隔
    public void scanWillOrderAndToSysInfoAndSaveToMysql(){

        List<SysInfoEntity> sysInfoList = orderService.getWillValidOrdersByUserIdAndToSysInfo(null);
        //保存到数据库
        sysInfoService.saveBatch(sysInfoList);

    }

    /**
     * 定时扫描订单项,获取预约已经到期到期或者车位已经到期的订单,封装成消息实体类，保存到数据库
     */
    @Transactional
    @Scheduled(cron = "*/15 * * * * ?") //测试使用 30秒 的间隔
    public void scanValidOrderAndToSysInfoAndSaveToMysql(){

        List<SysInfoEntity> sysInfoList = orderService.getValidOrdersByUserIdAndToSysInfo(null);
        //保存到数据库
        sysInfoService.saveBatch(sysInfoList);

    }

}
