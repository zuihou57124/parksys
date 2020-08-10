package com.qcw.parksys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qcw.parksys.common.utils.PageUtils;
import com.qcw.parksys.entity.OrderEntity;
import com.qcw.parksys.entity.SysInfoEntity;
import com.qcw.parksys.vo.SysInfoVo;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author qinfeng
 * @email zuihou57124@gmail.com
 * @date 2020-08-03 09:33:17
 */
public interface SysInfoService extends IService<SysInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);


    PageUtils getSysInfoList(Map<String, Object> params);
}

