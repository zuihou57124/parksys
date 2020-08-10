package com.qcw.parksys.service.impl;

import com.qcw.parksys.common.myconst.MyConst;
import com.qcw.parksys.entity.*;
import com.qcw.parksys.service.*;
import com.qcw.parksys.vo.BookParkVo;
import com.qcw.parksys.vo.SpaceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcw.parksys.common.utils.PageUtils;
import com.qcw.parksys.common.utils.Query;

import com.qcw.parksys.dao.SpaceDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("spaceService")
public class SpaceServiceImpl extends ServiceImpl<SpaceDao, SpaceEntity> implements SpaceService {

    @Autowired
    PositionService positionService;

    @Autowired
    TypeService typeService;

    @Autowired
    UserService userService;

    @Autowired
    VipService vipService;

    @Autowired
    OrderService orderService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpaceEntity> page = this.page(
                new Query<SpaceEntity>().getPage(params),
                new QueryWrapper<SpaceEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 获取车位列表（默认所有,可以按条件查询）
     */
    @Override
    public PageUtils getSpaceList(Map<String, Object> params) {

        QueryWrapper<SpaceEntity> wrapper = new QueryWrapper<>();
        String position_name = (String) params.get("position");
        String type_name = (String) params.get("type");
        Integer price = (Integer) params.get("price");
        String status = (String) params.get("status");

        //参数是否携带车位地区信息
        if(!StringUtils.isEmpty(position_name)){
            PositionEntity positionEntity = positionService.getOne(
                    new QueryWrapper<PositionEntity>()
                    .eq("position_name", position_name));
            wrapper.eq("position_id",positionEntity.getId());
        }

        //参数是否携带车位类型信息
        if(!StringUtils.isEmpty(type_name)){
            TypeEntity typeEntity = typeService.getOne(
                    new QueryWrapper<TypeEntity>()
                            .eq("type_name", type_name));
            wrapper.eq("type_id",typeEntity.getId());
        }

        //参数是否携带车位状态信息
        if(!StringUtils.isEmpty(status)){
            wrapper.eq("status",status);
        }

        //参数是否携带车位价格信息(先搞个升序，降序后面再说)
//        if(!StringUtils.isEmpty(type_name)){
//            TypeEntity typeEntity = typeService.getOne(
//                    new QueryWrapper<TypeEntity>()
//                            .orderByAsc("price"));
//            wrapper.eq("type_id",typeEntity.getId());
//        }

        IPage<SpaceEntity> page = this.page(
                new Query<SpaceEntity>().getPage(params),
                wrapper
        );

        //封装 space vo
        PageUtils pageUtils = new PageUtils(page);
        List<SpaceEntity> spaceEntityList = (List<SpaceEntity>) pageUtils.getList();
        List<SpaceVo> spaceVoList = spaceEntityList.stream()
                .map((item) -> {
                    SpaceVo spaceVo = new SpaceVo();
                    spaceVo.setId(item.getId());

                    PositionEntity position = positionService.getOne(new QueryWrapper<PositionEntity>()
                            .eq("id", item.getPositionId()));
                    spaceVo.setPosition(position.getPositionName());
                    TypeEntity typeEntity = typeService.getOne(new QueryWrapper<TypeEntity>()
                            .eq("id", item.getTypeId()));
                    spaceVo.setPrice(typeEntity.getPrice());
                    spaceVo.setType(typeEntity.getTypeName());
                    spaceVo.setStatus(item.getStatus());
                    spaceVo.setNextTime(item.getNextTime());

                    return spaceVo;
                }).collect(Collectors.toList());

        pageUtils.setList(spaceVoList);

        return pageUtils;
    }

    /**
     * @param bookParkVo
     * @return
     * 用户预约车位
     */
    @Override
    @Transactional
    public Boolean bookPark(BookParkVo bookParkVo) {

        if(bookParkVo.getUserId()==null){
            return false;
        }

        SpaceEntity space = this.getOne(new QueryWrapper<SpaceEntity>().eq("id", bookParkVo.getSpaceId()));
        UserEntity user = userService.getOne(new QueryWrapper<UserEntity>().eq("id", bookParkVo.getUserId()));
        TypeEntity type = typeService.getOne(new QueryWrapper<TypeEntity>().eq("id", space.getTypeId()));
        VipEntity vip = vipService.getOne(new QueryWrapper<VipEntity>().eq("vip_level", user.getVipLevel()));

        //重新判断预约是车位是否 处于空闲状态
        if(space.getStatus()!=MyConst.SpaceStatus.AVALIABLE.getCode()){
            return false;
        }

        //订单金额
        int totalMoney = bookParkVo.getDuration()*type.getPrice();
        space.setStatus(MyConst.SpaceStatus.BOOKED.getCode());

        //更新车位到期时间(预约不应该设置到期时间,因为还没有付,只有预约失效时间)
        //TODO 这里应该考虑到期之前用户续费的问题，先留着
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, bookParkVo.getDuration());
        Date nextTime = calendar.getTime();
        //space.setNextTime(nextTime);

        //构建订单项
        //构建订单项时，如果是预约，不该填充金额，到期时间等字段
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUserId(user.getId());
        orderEntity.setSpaceId(space.getId());
        //orderEntity.setDuration(bookParkVo.getDuration());
        //orderEntity.setTotalPayable(totalMoney);
        orderEntity.setStatus(MyConst.OrderStatus.CREATED.getCode());
        orderEntity.setCreateTime(new Date());
        orderEntity.setValidStatus(3);
        //获取对应会员等级的优惠价格
        if(vip!=null){
            //orderEntity.setTotalReal((int) (totalMoney*((double)vip.getDiscount()/10)));
        }

        orderService.save(orderEntity);

        return this.updateById(space);
    }

}