package com.qcw.parksys.service.impl;

import com.qcw.parksys.common.myconst.MyConst;
import com.qcw.parksys.entity.*;
import com.qcw.parksys.service.*;
import com.qcw.parksys.vo.BackMoneyVo;
import com.qcw.parksys.vo.SysInfoVo;
import com.qcw.parksys.vo.UserBooksVo;
import com.qcw.parksys.vo.UserOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcw.parksys.common.utils.PageUtils;
import com.qcw.parksys.common.utils.Query;

import com.qcw.parksys.dao.OrderDao;
import org.springframework.transaction.annotation.Transactional;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

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
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * @param params
     * @return 查询用户预约情况
     */
    @Override
    public PageUtils getUserBooks(Map<String, Object> params) {

        QueryWrapper<OrderEntity> wrapper = new QueryWrapper<>();
        Object userId = params.get("userId");
        if (userId != null) {
            wrapper.eq("user_id", userId);
        }
        //查询出已预约或者预约已失效的记录
        wrapper.eq("status", MyConst.OrderStatus.CREATED.getCode())
                .or()
                .eq("status", MyConst.OrderStatus.TOKEN.getCode());


        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                wrapper
        );

        PageUtils pageUtils = new PageUtils(page);

        List<OrderEntity> orderEntityList = (List<OrderEntity>) pageUtils.getList();
        List<UserBooksVo> userBooksVos = orderEntityList.stream().map((item) -> {

            UserBooksVo userBooksVo = new UserBooksVo();
            //设置各种id
            userBooksVo.setOrderId(item.getId());
            userBooksVo.setUserId(item.getUserId());
            userBooksVo.setSpaceId(item.getSpaceId());

            //设置预约失效时间
            TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(item.getCreateTime());
            calendar.add(Calendar.MINUTE, 5);
            Date validTime = calendar.getTime();
            userBooksVo.setValidTime(validTime);
            //System.out.println(userBooksVo.getValidTime());

            //设置车位状态
            SpaceEntity space = spaceService.getOne(new QueryWrapper<SpaceEntity>().eq("id", item.getSpaceId()));
            userBooksVo.setSpaceStatus(space.getStatus());

            //设置预约状态
            userBooksVo.setStatus(item.getStatus());

            //设置车位所属地区
            PositionEntity position = positionService.getOne(new QueryWrapper<PositionEntity>().eq("id", space.getPositionId()));
            userBooksVo.setPosition(position.getPositionName());

            //设置车位类型,价格
            TypeEntity type = typeService.getOne(new QueryWrapper<TypeEntity>().eq("id", space.getTypeId()));
            userBooksVo.setType(type.getTypeName());
            userBooksVo.setPrice(type.getPrice());


            return userBooksVo;
        }).collect(Collectors.toList());

        pageUtils.setList(userBooksVos);

        return pageUtils;
    }

    /**
     * @param params
     * @return 付款
     */
    @Override
    @Transactional
    public Integer pay(Map<String, Object> params) {

        //付款需要传入或有的参数:   用户信息，车位信息，订单信息,会员信息
        Integer userId = (Integer) params.get("userId");
        Integer spaceId = (Integer) params.get("spaceId");
        Integer orderId = (Integer) params.get("orderId");

        UserEntity user = userService.getById(userId);
        SpaceEntity space = spaceService.getById(spaceId);
        VipEntity vip = vipService.getOne(new QueryWrapper<VipEntity>().eq("vip_level", user.getVipLevel()));
        TypeEntity type = typeService.getById(space.getTypeId());
        OrderEntity order = this.getById(orderId);

        //重新计算预约是否失效
        long timestamp = new Date().getTime() - order.getCreateTime().getTime();
        if (timestamp / (1000) >= 300) {
            return 50003;
        }
        //重新获取车位的实时信息，可能会出现预约失效后已被其他用户预约的情况
//        if(!space.getStatus().equals(MyConst.SpaceStatus.AVALIABLE.getCode())){
//            return false;
//        }

        //时长
        Integer duration = (Integer) params.get("duration");
        //应付总额
        Integer total = type.getPrice() * duration;
        //实付总额(打折等优惠)
        Integer realPay = total;
        //用户余额是否足够
        if (user.getMoney() < total) {
            return 50002;
        }
        //扣除用户余额和更新用户累计花费
        user.setMoney(user.getMoney() - total);
        user.setTotalCost(user.getTotalCost() + realPay);

        //修改订单信息
        order.setStatus(MyConst.OrderStatus.DONE.getCode());
        order.setTotalPayable(total);
        order.setTotalReal(realPay);
        order.setValidStatus(3);
        order.setDuration(duration);
        //设置支付时间
        order.setPayTime(new Date());

        //修改车位到期时间 状态等等
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, duration);
        Date nextTime = calendar.getTime();
        space.setNextTime(nextTime);
        space.setStatus(MyConst.SpaceStatus.TOKEN.getCode());

        userService.updateById(user);
        spaceService.updateById(space);
        this.updateById(order);

        return 50001;
    }

    /**
     * @param params
     * @return 获取即将到期的订单 ,并且封装成消息实体类
     */
    @Override
    public List<SysInfoEntity> getWillValidOrdersByUserIdAndToSysInfo(Map<String, Object> params) {

        //Object userId = params.get("userId");

        //首先获取指定用户的所有订单
        QueryWrapper<OrderEntity> wrapper = new QueryWrapper<>();
        //wrapper.eq("user_id",userId);
        //只扫描系统之前未扫描过的订单,防止消息重复(这里会出问题,先注释)
        //wrapper.eq("scaned",0);

        /** 扫描即将过期的订单
         * 这里应该注意,扫描即将过期的订单时,条件应该是 时间足够 的订单，
         * 即valid_status = 3,如果判断即将过期，再设置成 0，即将过期
         **/
        wrapper.eq("valid_status", 3);
        //这里打算分页获取,先注释
//        IPage<OrderEntity> page = this.page(
//                new Query<OrderEntity>().getPage(params),
//                wrapper
//        );
        //List<OrderEntity> orderList = page.getRecords();
        List<OrderEntity> orderList = this.list(wrapper);
        //订单被扫描后,scaned设置为1
        orderList = orderList.stream().peek((item) -> item.setScaned(1)).collect(Collectors.toList());
        this.updateBatchById(orderList);

        List<SysInfoEntity> sysInfoVos = orderList.stream().map((item) -> {
            SysInfoEntity sysInfo = null;
            SpaceEntity space = spaceService.getById(item.getSpaceId());
            //预约即将失效时
            if (item.getStatus().equals(MyConst.OrderStatus.CREATED.getCode())) {
                long timestamp = new Date().getTime() - item.getCreateTime().getTime();
                System.out.println("间隔:  " + timestamp / 1000);
                //计算预约时间和当前时间的时间差
                if (timestamp / 1000 >= 200 && timestamp / 1000 <= 300) {
                    sysInfo.setCreateTime(new Date());
                    sysInfo = new SysInfoEntity();
                    sysInfo.setUserId(item.getUserId());
                    PositionEntity position = positionService.getById(space.getPositionId());
                    TypeEntity type = typeService.getById(space.getTypeId());
                    String info = "尊敬的用户,您预约的 " + position.getPositionName() + " 的" + type.getTypeName() + " 的预约即将在100秒内失效," + "请尽快前往支付";
                    sysInfo.setInfo(info);
                    sysInfo.setTitle("预约即将失效的通知");
                    //消息实体封装好后, readed 初始值为 0 ,即未读
                    sysInfo.setReaded(0);

                    //设置订单即将过期,下次定时任务可以不用扫描，减少开销
                    item.setValidStatus(0);
                    this.updateById(item);

                    System.out.println("预约即将失效啦");
                }
                return sysInfo;
            }
            //车位即将到期时
            if (item.getStatus().equals(MyConst.OrderStatus.DONE.getCode())) {
                long timestamp = new Date().getTime() - space.getNextTime().getTime();
                System.out.println("间隔:  " + timestamp / 1000);
                //计算车位到期时间和当前时间的时间差
                if (timestamp / 1000 >= 600 && timestamp / 1000 <= 1200) {
                    sysInfo.setCreateTime(new Date());
                    sysInfo = new SysInfoEntity();
                    sysInfo.setUserId(item.getUserId());
                    PositionEntity position = positionService.getById(space.getPositionId());
                    TypeEntity type = typeService.getById(space.getTypeId());
                    String info = "尊敬的用户,您租借的 " + position.getPositionName() + " 的" + type.getTypeName() + " 的车位即将在10分钟内到期," + "请尽快前往续费";
                    sysInfo.setInfo(info);
                    sysInfo.setTitle("车位即将到期的通知");
                    //消息实体封装好后, readed 初始值为 0 ,即未读
                    sysInfo.setReaded(0);

                    //设置订单即将过期,下次定时任务可以不用扫描，减少开销
                    item.setValidStatus(0);
                    this.updateById(item);
                    System.out.println("车位即将到期啦");
                }
                return sysInfo;
            }
            return null;
        }).collect(Collectors.toList());

        sysInfoVos = sysInfoVos.stream().filter(Objects::nonNull).collect(Collectors.toList());

        return sysInfoVos;
    }

    /**
     * @param params
     * @return 查询用户订单
     */
    @Override
    public PageUtils getUserOrders(Map<String, Object> params) {

        QueryWrapper<OrderEntity> wrapper = new QueryWrapper<>();
        Object userId = params.get("userId");
        if (userId != null) {
            wrapper.eq("user_id", userId);
        }

        //查询出订单
        ArrayList<Integer> status = new ArrayList<>();
        status.add(40002);
        status.add(40003);
        status.add(40004);
        wrapper.in("status", status);

        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                wrapper
        );

        PageUtils pageUtils = new PageUtils(page);

        List<OrderEntity> orderEntityList = (List<OrderEntity>) pageUtils.getList();
        List<UserOrderVo> userOrderVos = orderEntityList.stream().map((item) -> {

            UserOrderVo userOrderVo = new UserOrderVo();
            //设置各种id
            userOrderVo.setOrderId(item.getId());
            userOrderVo.setUserId(item.getUserId());
            userOrderVo.setSpaceId(item.getSpaceId());

            //设置车位到期时间(这里可以不用计算,直接使用车位的信息)
//            TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(item.getCreateTime());
//            calendar.add(Calendar.HOUR,item.getDuration());
//            Date validTime = calendar.getTime();
//            userOrderVo.setValidTime(validTime);
            //System.out.println(userBooksVo.getValidTime());

            //设置车位状态和到期时间
            SpaceEntity space = spaceService.getOne(new QueryWrapper<SpaceEntity>().eq("id", item.getSpaceId()));
            userOrderVo.setSpaceStatus(space.getStatus());
            userOrderVo.setValidTime(space.getNextTime());
            userOrderVo.setDuration(item.getDuration());
            //设置订单状态
            userOrderVo.setStatus(item.getStatus());
            userOrderVo.setValidSatus(item.getValidStatus());

            //设置自订单创建日期和支付日期
            userOrderVo.setCreateTime(item.getCreateTime());
            userOrderVo.setPayTime(item.getPayTime());

            //设置应付总额和实付总额
            userOrderVo.setTotalPayable(item.getTotalPayable());
            userOrderVo.setTotalReal(item.getTotalReal());

            //设置车位所属地区
            PositionEntity position = positionService.getOne(new QueryWrapper<PositionEntity>().eq("id", space.getPositionId()));
            userOrderVo.setPosition(position.getPositionName());

            //设置车位类型,价格
            TypeEntity type = typeService.getOne(new QueryWrapper<TypeEntity>().eq("id", space.getTypeId()));
            userOrderVo.setType(type.getTypeName());
            userOrderVo.setPrice(type.getPrice());

            return userOrderVo;
        }).collect(Collectors.toList());

        pageUtils.setList(userOrderVos);

        return pageUtils;
    }

    /**
     * @param params
     * @return 获取已经到期的订单或预约 ,并且封装成消息实体类
     * 车位过期后,更新车位状态
     */
    @Override
    @Transactional
    public List<SysInfoEntity> getValidOrdersByUserIdAndToSysInfo(Map<String, Object> params) {

        //Object userId = params.get("userId");
        //首先获取指定用户的所有订单
        QueryWrapper<OrderEntity> wrapper = new QueryWrapper<>();
        //wrapper.eq("user_id",userId);
        //只扫描系统之前未扫描过的订单,防止消息重复(这里会出问题,先注释)
        //wrapper.eq("scaned",0);
        //扫描即将过期的订单
        wrapper.eq("valid_status", 0);
        List<OrderEntity> orderList = this.list(wrapper);
        //订单被扫描后,scaned设置为1
        orderList = orderList.stream().peek((item) -> item.setScaned(1)).collect(Collectors.toList());
        this.updateBatchById(orderList);

        List<SysInfoEntity> sysInfoVos = orderList.stream().map((item) -> {
            SysInfoEntity sysInfo = null;
            SpaceEntity space = spaceService.getById(item.getSpaceId());
            //预约已经失效
            if (item.getStatus().equals(MyConst.OrderStatus.CREATED.getCode())) {
                long timestamp = item.getCreateTime().getTime() - new Date().getTime();
                System.out.println("间隔:  " + timestamp / 1000);
                //计算到期时间和当前时间的时间差
                if (timestamp <= 0) {
                    sysInfo = new SysInfoEntity();
                    sysInfo.setUserId(item.getUserId());
                    sysInfo.setCreateTime(new Date());
                    PositionEntity position = positionService.getById(space.getPositionId());
                    TypeEntity type = typeService.getById(space.getTypeId());
                    String info = "尊敬的用户,您预约的 " + position.getPositionName() + " 的" + type.getTypeName() + " 已经失效," + "请重新预约";
                    sysInfo.setInfo(info);
                    sysInfo.setTitle("预约失效通知");
                    //消息实体封装好后, readed 初始值为 0 ,即未读
                    sysInfo.setReaded(0);

                    //设置订单已经过期,下次定时任务可以不用扫描，减少开销
                    item.setValidStatus(1);
                    this.updateById(item);

                    System.out.println("预约已经失效啦");
                }
                return sysInfo;
            }
            //车位已经到期时
            if (item.getStatus().equals(MyConst.OrderStatus.DONE.getCode())) {
                long timestamp = space.getNextTime().getTime() - new Date().getTime();
                System.out.println("间隔:  " + timestamp / 1000);
                //计算车位到期时间和当前时间的时间差
                if (timestamp <= 0) {

                    //如果车位已经到期,重新设置车位的状态
                    space.setStatus(MyConst.SpaceStatus.AVALIABLE.getCode());
                    space.setNextTime(null);
                    spaceService.updateById(space);

                    sysInfo = new SysInfoEntity();
                    sysInfo.setUserId(item.getUserId());
                    sysInfo.setCreateTime(new Date());
                    PositionEntity position = positionService.getById(space.getPositionId());
                    TypeEntity type = typeService.getById(space.getTypeId());
                    String info = "尊敬的用户,您租借的 " + position.getPositionName() + " 的" + type.getTypeName() + " 已经到期," + "请在5分钟内将车移除车位";
                    sysInfo.setInfo(info);
                    sysInfo.setTitle("车位到期的通知");
                    //消息实体封装好后, readed 初始值为 0 ,即未读
                    sysInfo.setReaded(0);

                    //设置订单已经过期,下次定时任务可以不用扫描，减少开销
                    item.setValidStatus(1);
                    this.updateById(item);
                    System.out.println("车位已经到期啦");
                }
                return sysInfo;
            }
            return null;
        }).collect(Collectors.toList());

        sysInfoVos = sysInfoVos.stream().filter(Objects::nonNull).collect(Collectors.toList());

        return sysInfoVos;
    }

    @Override
    @Transactional
    public Integer backMoney(BackMoneyVo backMoneyVo) {

        UserEntity user = userService.getById(backMoneyVo.getUserId());
        OrderEntity order = this.getById(backMoneyVo.getOrderId());
        SpaceEntity space = spaceService.getById(backMoneyVo.getSpaceId());

        Date nextTime = space.getNextTime();
        Date curr = new Date();
        long timestamp = nextTime.getTime() - curr.getTime();
        //车位已到期，不能退款
        if (timestamp <= 0) {
            return MyConst.BackMoneyEnum.VALID.getCode();
        }
        //计算剩余时间求出退款金额
        timestamp = (int) (timestamp / 1000 / 3600);
        float restTime = (float) timestamp / order.getDuration();
        Integer backMoney = (int) (restTime * 0.9 * order.getTotalReal());
        //退款和扣除积分
        user.setMoney(user.getMoney() + backMoney);
        user.setIntegral(user.getIntegral() - backMoney);
        //设置订单状态
        order.setStatus(MyConst.OrderStatus.BACKED.getCode());
        order.setValidStatus(1);
        //设置车位状态
        space.setNextTime(null);
        space.setStatus(MyConst.SpaceStatus.AVALIABLE.getCode());

        this.updateById(order);
        spaceService.updateById(space);
        userService.updateById(user);

        return MyConst.BackMoneyEnum.SUCCESS.getCode();
    }


}