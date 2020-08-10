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

import com.qcw.parksys.entity.PositionEntity;
import com.qcw.parksys.service.PositionService;
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
@RequestMapping("parksys/position")
public class PositionController {
    @Autowired
    private PositionService positionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("parksys:position:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = positionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("parksys:position:info")
    public R info(@PathVariable("id") Integer id){
		PositionEntity position = positionService.getById(id);

        return R.ok().put("position", position);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("parksys:position:save")
    public R save(@RequestBody PositionEntity position){
		positionService.save(position);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("parksys:position:update")
    public R update(@RequestBody PositionEntity position){
		positionService.updateById(position);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("parksys:position:delete")
    public R delete(@RequestBody Integer[] ids){
		positionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
