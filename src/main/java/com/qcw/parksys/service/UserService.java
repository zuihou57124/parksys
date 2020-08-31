package com.qcw.parksys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qcw.parksys.common.utils.PageUtils;
import com.qcw.parksys.entity.GeoPosition;
import com.qcw.parksys.entity.SysInfoEntity;
import com.qcw.parksys.entity.UserEntity;
import com.qcw.parksys.vo.SysInfoVo;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author qinfeng
 * @email zuihou57124@gmail.com
 * @date 2020-08-03 09:33:17
 */
public interface UserService extends IService<UserEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SysInfoEntity> getSysInfos(Map<String, Object> params);

    GeoPosition getCurrPostionByIp(String ip);

    List<SysInfoEntity> updateUserInfo();
}

