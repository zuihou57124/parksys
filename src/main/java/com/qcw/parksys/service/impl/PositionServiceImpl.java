package com.qcw.parksys.service.impl;

import com.qcw.parksys.entity.GeoPosition;
import com.qcw.parksys.entity.SpaceEntity;
import com.qcw.parksys.entity.TypeEntity;
import com.qcw.parksys.service.GeoPositionService;
import com.qcw.parksys.vo.PositionVo;
import com.qcw.parksys.vo.SpaceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcw.parksys.common.utils.PageUtils;
import com.qcw.parksys.common.utils.Query;

import com.qcw.parksys.dao.PositionDao;
import com.qcw.parksys.entity.PositionEntity;
import com.qcw.parksys.service.PositionService;
import org.springframework.util.StringUtils;


@Service("positionService")
public class PositionServiceImpl extends ServiceImpl<PositionDao, PositionEntity> implements PositionService {

    @Autowired
    GeoPositionService geoPositionService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PositionEntity> page = this.page(
                new Query<PositionEntity>().getPage(params),
                new QueryWrapper<PositionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils getParkList(Map<String, Object> params) {

        QueryWrapper<PositionEntity> wrapper = new QueryWrapper<>();
        //各种条件查询
        String position_name = (String) params.get("position_name");
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

            wrapper.in("geo_id",geoids);
        }

        //参数是否携带停车场名称 (模糊查找)
        if(!StringUtils.isEmpty(position_name)){
            PositionEntity positionEntity = this.getOne(
                    new QueryWrapper<PositionEntity>()
                            .eq("position_name", position_name));
            wrapper.like("position_id",positionEntity.getId());
        }

        IPage<PositionEntity> page = this.page(
                new Query<PositionEntity>().getPage(params),
                wrapper
        );

        //封装 positionVo
        PageUtils pageUtils = new PageUtils(page);
        List<PositionEntity> positionList = (List<PositionEntity>) pageUtils.getList();
        List<PositionVo> positionVos = positionList.stream()
                .map((item) -> {
                    PositionVo positionVo = new PositionVo();
                    positionVo.setId(item.getId());

                    //获取 省市区 信息
                    GeoPosition geo = geoPositionService.getById(item.getGeoId());

                    positionVo.setProvince(geo.getPro());
                    positionVo.setCity(geo.getCity());
                    positionVo.setRegion(geo.getRegion());

                    positionVo.setName(item.getPositionName());
                    positionVo.setGeoId(item.getGeoId());
                    positionVo.setNum(item.getNum());
                    positionVo.setRest(item.getRest());
                    positionVo.setImgUrl(item.getImgUrl());

                    return positionVo;

                }).collect(Collectors.toList());

        pageUtils.setList(positionVos);

        return pageUtils;

    }

}