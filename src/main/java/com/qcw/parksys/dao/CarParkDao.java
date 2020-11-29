package com.qcw.parksys.dao;

import com.qcw.parksys.vo.PositionVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author qinfeng
 * @email zuihou57124@gmail.com
 * @date 2020-09-30 08:48:38
 */
@Mapper
public interface CarParkDao extends BaseMapper<PositionVo> {
	
}
