package com.qcw.parksys.controller;

import java.util.Arrays;
import java.util.Map;

import com.qcw.parksys.vo.BookParkVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.qcw.parksys.entity.SpaceEntity;
import com.qcw.parksys.service.SpaceService;
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
@RequestMapping("parksys/space")
public class SpaceController {
    @Autowired
    private SpaceService spaceService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("parksys:space:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spaceService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 获取车位列表（默认所有,可以按条件查询）
     */
    @RequestMapping("/spacelist")
    public R spaceList(@RequestParam Map<String, Object> params){
        PageUtils page = spaceService.getSpaceList(params);

        return R.ok().put("page", page);
    }

    /**
     * 预约车位
     */
    @PostMapping("/bookpark")
    public R bookPark(@RequestBody BookParkVo bookParkVo){
        Boolean flag = spaceService.bookPark(bookParkVo);
        if(!flag){
            return R.error(4161,"请先登录");
        }
        return R.ok();
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("parksys:space:info")
    public R info(@PathVariable("id") Integer id){
		SpaceEntity space = spaceService.getById(id);

        return R.ok().put("space", space);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("parksys:space:save")
    public R save(@RequestBody SpaceEntity space){
		spaceService.save(space);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("parksys:space:update")
    public R update(@RequestBody SpaceEntity space){
		spaceService.updateById(space);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("parksys:space:delete")
    public R delete(@RequestBody Integer[] ids){
		spaceService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
