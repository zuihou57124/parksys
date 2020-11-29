package com.qcw.parksys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qcw.parksys.common.utils.PageUtils;
import com.qcw.parksys.vo.PositionVo;

import java.util.Map;

/**
 * 
 *
 * @author qinfeng
 * @email zuihou57124@gmail.com
 * @date 2020-09-30 08:48:38
 */
public interface CarParkService extends IService<PositionVo> {

    PageUtils queryPage(Map<String, Object> params);
}

