package com.qcw.parksys.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qcw.parksys.entity.VipEntity;
import com.qcw.parksys.service.VipService;
import com.qcw.parksys.common.utils.PageUtils;
import com.qcw.parksys.common.utils.R;



/**
 * 
 *
 * @author qinfeng
 * @email zuihou57124@gmail.com
 * @date 2020-08-03 09:33:17
 */
@RestController
@RequestMapping("parksys/vip")
public class VipController {
    @Autowired
    private VipService vipService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("parksys:vip:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = vipService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("parksys:vip:info")
    public R info(@PathVariable("id") Integer id){
		VipEntity vip = vipService.getById(id);

        return R.ok().put("vip", vip);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("parksys:vip:save")
    public R save(@RequestBody VipEntity vip){
		vipService.save(vip);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("parksys:vip:update")
    public R update(@RequestBody VipEntity vip){
		vipService.updateById(vip);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("parksys:vip:delete")
    public R delete(@RequestBody Integer[] ids){
		vipService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
