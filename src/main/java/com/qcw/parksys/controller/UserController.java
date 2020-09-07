package com.qcw.parksys.controller;

import java.util.*;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qcw.parksys.ValidGroups.UserRegister;
import com.qcw.parksys.common.myconst.MyConst;
import com.qcw.parksys.common.utils.MailSend;
import com.qcw.parksys.entity.GeoPosition;
import com.qcw.parksys.entity.SysInfoEntity;
import com.qcw.parksys.vo.UserVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.qcw.parksys.entity.UserEntity;
import com.qcw.parksys.service.UserService;
import com.qcw.parksys.common.utils.PageUtils;
import com.qcw.parksys.common.utils.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;


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

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    MailSend mailSend;

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
     * 登陆(使用邮箱或者用户名都可以)
     */
    @PostMapping("login")
    public R login(@RequestBody UserVo userVo, HttpServletRequest request){

        HttpSession session = request.getSession();
        String sessionId = session.getId();
        //获取redis中的验证码,与用户输入的验证码进行比较
        String redisCode = redisTemplate.opsForValue().get("user:" + sessionId);

        //验证码出错
        if(!userVo.getCode().equals(redisCode)){
            return R.error(MyConst.MemberEnum.USER_LOGIN_CODE.getCode(),MyConst.MemberEnum.USER_LOGIN_CODE.getMsg());
        }

        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("username",userVo.getUsername());
        wrapper.or()
               .eq("email",userVo.getEmail());
        wrapper.and((wrapper1)->{
            wrapper1.eq("password",userVo.getPassword());
        });

        UserEntity user = userService.getOne(wrapper);
        if(user!=null){

            //登录成功,把用户信息存放到session中
            session = request.getSession();
            session.setAttribute("user",user);
            //设置session有效期
            session.setMaxInactiveInterval(60*120);
            return R.ok().put("user", user);
        }
        //用户名或密码错误
        return R.error(MyConst.MemberEnum.USER_LOGIN_EXCEPTION.getCode(),MyConst.MemberEnum.USER_LOGIN_EXCEPTION.getMsg());
    }

    /**
     * 获取验证码
     */
    @RequestMapping("getcode")
    public R getCode(HttpServletRequest request){

        String code = UUID.randomUUID().toString().substring(0,4);
        //MyIntercepter.code.set(code);
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        //向redis存入验证码,使用sessionid来区分不同的用户,验证码2分钟内有效
        redisTemplate.opsForValue().set("user:"+sessionId,code,2, TimeUnit.MINUTES);

        System.out.println(redisTemplate.opsForValue().get("user:"+sessionId));

        return R.ok().put("code",code);
    }

    /**
     * 发送注册验证码(邮箱)
     */
    @RequestMapping("sendCodeToEmail")
    public R sendCodeToEmail(HttpServletRequest request,@RequestParam("email") String email){

        if(StringUtils.isEmpty(email)){
            return R.error().put("msg","请填写邮箱账号");
        }

        if(!email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")){
            return R.error().put("msg","邮箱格式错误");
        }

        //发送邮箱验证码判断邮箱是否已被注册
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("username",email);
        UserEntity user = userService.getOne(wrapper);
        if(user!=null){
            return R.error().put("msg","邮箱已被注册");
        }

        String code = UUID.randomUUID().toString().substring(0,5);
        //向redis存入验证码,使用邮箱账号来区分不同的用户,验证码5分钟内有效
        redisTemplate.opsForValue().set(email+":",code,5, TimeUnit.MINUTES);
        //向邮箱发送验证码
        boolean isSuccess = mailSend.sendRegisterCode("1545409483@qq.com", email, "验证码", code);
        if(!isSuccess){
            return R.error().put("msg","邮箱发送失败,请重试");
        }

        System.out.println(redisTemplate.opsForValue().get(email+":"));

        return R.ok().put("code",code);
    }


    /**
     * 注册
     */
    @PostMapping("register")
    @Transactional
    public R register(@RequestBody @Validated UserVo userVo, BindingResult bindingResult){

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0){

            FieldError fieldError = fieldErrors.get(0);
            return R.error().put("msg",fieldError.getDefaultMessage());

        }

        String redisCode = redisTemplate.opsForValue().get(userVo.getEmail() + ":");

        //验证码出错
        if(!userVo.getCode().equals(redisCode)){
            return R.error(MyConst.MemberEnum.USER_LOGIN_CODE.getCode(),MyConst.MemberEnum.USER_LOGIN_CODE.getMsg());
        }

        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("username",userVo.getUsername())
               .or()
               .eq("email",userVo.getEmail());

        UserEntity user = userService.getOne(wrapper);
        if(user!=null){
            return R.error().put("msg","用户名或邮箱已被注册");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setCreateTime(new Date());
        userEntity.setUpdateTime(new Date());
        //保存用户信息
        BeanUtils.copyProperties(userVo,userEntity);
        boolean save = userService.save(userEntity);
        if(save){
            //注册成功后，删除验证码
            redisTemplate.delete(userVo.getEmail() + ":");
        }

        return R.ok();
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
     * 判断session是否失效
     */
    @RequestMapping("loginvalid")
    public R loginValid(HttpServletRequest request){

        HttpSession session = request.getSession();
        UserEntity user = (UserEntity) session.getAttribute("user");
        if(user==null){
            return R.error().put("msg","登录已失效,请重新登录");
        }
        return R.ok().put("msg","success");
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
