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

import com.qcw.parksys.vo.PositionVo;
import com.qcw.parksys.service.CarParkService;
import com.qcw.parksys.common.utils.PageUtils;
import com.qcw.parksys.common.utils.R;



/**
 * 
 *
 * @author qinfeng
 * @email zuihou57124@gmail.com
 * @date 2020-09-30 08:48:38
 */
@RestController
@RequestMapping("parksys/carpark")
public class CarParkController {
    @Autowired
    private CarParkService carParkService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("parksys:carpark:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = carParkService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("parksys:carpark:info")
    public R info(@PathVariable("id") Integer id){
		PositionVo carPark = carParkService.getById(id);

        return R.ok().put("carPark", carPark);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("parksys:carpark:save")
    public R save(@RequestBody PositionVo carPark){
		carParkService.save(carPark);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("parksys:carpark:update")
    public R update(@RequestBody PositionVo carPark){
		carParkService.updateById(carPark);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("parksys:carpark:delete")
    public R delete(@RequestBody Integer[] ids){
		carParkService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
