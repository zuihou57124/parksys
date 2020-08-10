package com.qcw.parksys.controller;

import com.qcw.parksys.common.myconst.MyConst;
import com.qcw.parksys.common.utils.PageUtils;
import com.qcw.parksys.common.utils.R;
import com.qcw.parksys.entity.OrderEntity;
import com.qcw.parksys.service.OrderService;
import com.qcw.parksys.service.SysInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 
 *
 * @author qinfeng
 * @email zuihou57124@gmail.com
 * @date 2020-08-03 09:33:17
 */
@RestController
@RequestMapping("parksys")
public class SysInfoController {

    @Autowired
    SysInfoService sysInfoService;

    /**
     * @param params
     * @return
     * 按照用户id查询系统消息
     */
    @PostMapping("getsysinfo")
    public R getSysInfoByUserId(@RequestBody Map<String, Object> params){

        PageUtils page = sysInfoService.getSysInfoList(params);

        return R.ok().put("page",page);
    }

}
