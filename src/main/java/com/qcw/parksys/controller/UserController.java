package com.qcw.parksys.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qcw.parksys.common.myconst.MyConst;
import com.qcw.parksys.entity.GeoPosition;
import com.qcw.parksys.entity.SysInfoEntity;
import com.qcw.parksys.vo.UserVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.qcw.parksys.entity.UserEntity;
import com.qcw.parksys.service.UserService;
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
@RequestMapping("parksys/user")
public class UserController {

    @Autowired
    private UserService userService;

    private static ThreadLocal<String> code = new ThreadLocal<>();

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("parksys:user:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = userService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 登陆
     */
    @PostMapping("login")
    public R login(@RequestBody UserVo userVo){

        //验证码出错
        if(!userVo.getCode().equals(UserController.code.get())){
            //return R.error(MyConst.MemberEnum.USER_LOGIN_CODE.getCode(),MyConst.MemberEnum.USER_LOGIN_CODE.getMsg());
        }

        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("username",userVo.getUsername());
        wrapper.eq("password",userVo.getPassword());
        UserEntity user = userService.getOne(wrapper);
        if(user!=null){
            return R.ok().put("user", user);
        }
        //用户名或密码错误
        return R.error(MyConst.MemberEnum.USER_LOGIN_EXCEPTION.getCode(),MyConst.MemberEnum.USER_LOGIN_EXCEPTION.getMsg());
    }

    /**
     * 获取验证码
     */
    @RequestMapping("getcode")
    public R getCode(){

        String code = UUID.randomUUID().toString().substring(0,4);
        //MyIntercepter.code.set(code);
        System.out.println(UserController.code.get());

        return R.ok().put("code",code);
    }

    /**
     * 获取车位到期，预约到期等信息 (即将到期)
     */
    @RequestMapping("sysinfo")
    public R getSysInfo(@RequestParam Map<String,Object> params){

        List<SysInfoEntity> sysInfos = userService.getSysInfos(params);

        return R.ok().put("data",sysInfos);
    }

    /**
     * 获取用户地理位置
     * 通过ip获得=>如果用户使用代理,可能不正确,精确到区县
     * 通过浏览器端获取地理位置=>可以获取经纬度
     */
    @RequestMapping("currposition")
    public R currPositionByIp(String ip){

        GeoPosition currPostion = userService.getCurrPostionByIp(ip);
        return R.ok().put("data",currPostion);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("parksys:user:info")
    public R info(@PathVariable("id") Integer id){
		UserEntity user = userService.getById(id);

        return R.ok().put("user", user);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("parksys:user:save")
    public R save(@RequestBody UserEntity user){
		userService.save(user);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("parksys:user:update")
    public R update(@RequestBody UserEntity user){
		userService.updateById(user);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("parksys:user:delete")
    public R delete(@RequestBody Integer[] ids){
		userService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
