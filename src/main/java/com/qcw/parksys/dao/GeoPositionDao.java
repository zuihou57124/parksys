package com.qcw.parksys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qcw.parksys.entity.GeoPosition;
import com.qcw.parksys.entity.PositionEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author qinfeng
 * @email zuihou57124@gmail.com
 * @date 2020-08-03 09:33:17
 */
@Mapper
public interface GeoPositionDao extends BaseMapper<GeoPosition> {
	
}
