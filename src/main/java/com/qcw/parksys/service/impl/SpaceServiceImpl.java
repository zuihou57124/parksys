package com.qcw.parksys.service.impl;

import com.qcw.parksys.common.myconst.MyConst;
import com.qcw.parksys.entity.*;
import com.qcw.parksys.service.*;
import com.qcw.parksys.vo.BookParkVo;
import com.qcw.parksys.vo.SpaceVo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    @Autowired
    GeoPositionService geoPositionService;

    @Autowired
    RabbitTemplate rabbitTemplate;

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
    //@Cacheable(value = "spaceList",key = "'spaceList:page:'+#params.get('page')")
    public PageUtils getSpaceList(Map<String, Object> params) {

        QueryWrapper<SpaceEntity> wrapper = new QueryWrapper<>();
        String position_name = (String) params.get("position");
        String type_name = (String) params.get("type");
        Integer price = (Integer) params.get("price");
        String status = (String) params.get("status");

        //省市区筛选
        String pro = (String) params.get("pro");
        String city = (String) params.get("city");
        String region = (String) params.get("region");

        //参数是否携带省市区信息
        if(!StringUtils.isEmpty(pro)){

            QueryWrapper<GeoPosition> wr = new QueryWrapper<>();
            wr.eq("pro",pro);

            if(!StringUtils.isEmpty(city)){
                wr.eq("city",city);

            }if(!StringUtils.isEmpty(region)){
                wr.eq("region",region);
            }

            List<Integer> geoids = geoPositionService.list(wr
                    )
                    .stream()
                    .map(GeoPosition::getId).collect(Collectors.toList());

            if(geoids.size()==0){

                return null;
            }

            List<PositionEntity> positions = positionService.list(new QueryWrapper<PositionEntity>()
                    .in("geo_id", geoids));

            List<Integer> ids = positions.stream().map(PositionEntity::getId).collect(Collectors.toList());

            wrapper.in("position_id",ids);
        }

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

                    //获取地点信息
                    PositionEntity position = positionService.getOne(new QueryWrapper<PositionEntity>()
                            .eq("id", item.getPositionId()));

                    //获取 省市区 信息
                    GeoPosition geo = geoPositionService.getById(position.getGeoId());
                    spaceVo.setProvince(geo.getPro());
                    spaceVo.setCity(geo.getCity());
                    spaceVo.setRegion(geo.getRegion());

                    spaceVo.setPosition(position.getPositionName());
                    TypeEntity typeEntity = typeService.getOne(new QueryWrapper<TypeEntity>()
                            .eq("id", item.getTypeId()));
                    spaceVo.setPrice(typeEntity.getPrice());
                    spaceVo.setType(typeEntity.getTypeName());
                    spaceVo.setStatus(item.getStatus());
                    spaceVo.setNextTime(item.getNextTime());
                    spaceVo.setImg(item.getImg());
                    spaceVo.setIsDiscount(item.getIsDiscount());
                    spaceVo.setDiscount(item.getDiscount());
                    spaceVo.setStopTime(item.getStopTime());

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
    @CacheEvict(value = "spaceList",allEntries = true)
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
        //更新车位到期时间(预约有效期为5分钟,设置5分钟)
        //TODO 这里应该考虑到期之前用户续费的问题，先留着
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE,5);
        Date nextTime = calendar.getTime();
        space.setNextTime(nextTime);

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

        orderEntity.setQrCodeUrl("");

        orderService.save(orderEntity);
        //订单自动关闭--rabbit
        rabbitTemplate.convertAndSend(
                "order-event-exchange","order.create.event",orderEntity
                ,message -> {
                        //5分钟后过期
                        message.getMessageProperties().setExpiration(String.valueOf(60000*5));
                        return message;
                    }
                );

        //预约即将到期--rabbit
        rabbitTemplate.convertAndSend(
                "order-event-exchange","order.createwillvaliddelay.event",orderEntity
                ,message -> {
                    //3分钟后过期
                    message.getMessageProperties().setExpiration(String.valueOf(60000*3));
                    return message;
                }
        );

        //预约已经到期--rabbit
        rabbitTemplate.convertAndSend(
                "order-event-exchange","order.createvaliddelay.event",orderEntity
                ,message -> {
                    //5分钟后过期
                    message.getMessageProperties().setExpiration(String.valueOf(60000*5-2000));
                    return message;
                }
        );


        return this.updateById(space);
    }

    /**
     * 随机打折
     */
    @Override
    public void discount() {

        QueryWrapper<SpaceEntity> wr = new QueryWrapper<>();
        wr.eq("is_discount",0);
        wr.and((w)-> w.eq("able_discount",1));

        List<SpaceEntity> list = this.list(wr);
        List<SpaceEntity> collect = list.stream().map((item) -> {

            Random random = new Random();
            if(random.nextInt(10)<5){
                return null;
            }

            item.setIsDiscount(1);
            float discount;
            discount = (float) 0.7;
            item.setDiscount(discount);

            //设置优惠时长
            TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.HOUR,1);
            Date stopTime = calendar.getTime();
            item.setStopTime(stopTime);

            rabbitTemplate.convertAndSend("order-event-exchange","order.creatediscount.event",item,
                    message -> {
                        message.getMessageProperties().setExpiration(String.valueOf(1000*60*60));
                        return message;
                    }
                    );

            return item;
        })
         .filter(Objects::nonNull)
         .collect(Collectors.toList());

        this.updateBatchById(collect);

    }

    /**
     * @param space 取消打折或者优惠
     */
    @Override
    @Transactional
    public void canceldiscount(SpaceEntity space) {

        space.setIsDiscount(0);
        this.updateById(space);

    }

}