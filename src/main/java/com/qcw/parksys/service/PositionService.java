package com.qcw.parksys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qcw.parksys.common.utils.PageUtils;
import com.qcw.parksys.entity.PositionEntity;

import java.util.Map;

/**
 * 
 *
 * @author qinfeng
 * @email zuihou57124@gmail.com
 * @date 2020-08-03 09:33:17
 */
public interface PositionService extends IService<PositionEntity> {

    PageUtils queryPage(Map<String, Object> params);


    PageUtils getParkList(Map<String, Object> params);
}

