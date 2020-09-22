package com.qcw.parksys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qcw.parksys.common.utils.IPUtils;
import com.qcw.parksys.component.UserAgentInterceptor;
import com.qcw.parksys.entity.GeoPosition;
import com.qcw.parksys.entity.OrderEntity;
import com.qcw.parksys.entity.SysInfoEntity;
import com.qcw.parksys.service.OrderService;
import com.qcw.parksys.service.SpaceService;
import com.qcw.parksys.vo.SysInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcw.parksys.common.utils.PageUtils;
import com.qcw.parksys.common.utils.Query;

import com.qcw.parksys.dao.UserDao;
import com.qcw.parksys.entity.UserEntity;
import com.qcw.parksys.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;


@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {


    @Autowired
    OrderService orderService;

    @Autowired
    SpaceService spaceService;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserEntity> page = this.page(
                new Query<UserEntity>().getPage(params),
                new QueryWrapper<UserEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * @param params
     * @return
     * 获取车位到期，预约到期等信息列表
     */
    @Override
    public List<SysInfoEntity> getSysInfos(Map<String, Object> params) {

        //获取即将到期的订单信息 vo
        List<SysInfoEntity> sysInfos  = orderService.getWillValidOrdersByUserIdAndToSysInfo(params);

        return sysInfos;
    }

    @Override
    public GeoPosition getCurrPostionByIp(String ip) {

        //获取用户ip地址
        //String ip = IPUtils.getIpAddr(request);
        //请求参数
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("ip",ip);
        params.add("accessKey","alibaba-inc");
        //请求地址
        //淘宝接口
        String uri="http://ip.taobao.com/outGetIpInfo";
        //其他接口
        String uri2="http://whois.pconline.com.cn/ipJson.jsp?ip="+ip+"&json=true";
        HttpHeaders headers = new HttpHeaders();
        //以表单方式提交数据
        //headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //以json方式提交数据
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MultiValueMap<String,String>> reEntity = new HttpEntity<>(params,headers);
        //JSONObject json = restTemplate.exchange(uri2, HttpMethod.GET, reEntity,JSONObject.class).getBody();
        String jsonstr = restTemplate.getForObject(uri2, String.class);
        if(jsonstr==null || StringUtils.isEmpty(jsonstr)){
            return null;
        }
        //LinkedHashMap<String,String> data = json.getObject("data", LinkedHashMap.class);
        //GeoPosition geoPosition = JSONObject.parseObject(JSONObject.toJSONString(json),GeoPosition.class);
        GeoPosition geoPosition = JSONObject.parseObject(jsonstr,GeoPosition.class);

        return geoPosition;
    }

    /**
     * @return
     * 定时更新用户信息
     */
    @Override
    public List<SysInfoEntity> updateUserInfo() {

        List<SysInfoEntity> sysInfos = new ArrayList<>();

        List<UserEntity> list = this.list();

        list.forEach((user)->{
            SysInfoEntity sysInfo= new SysInfoEntity();
            switch (user.getTotalCost().intValue()/1000) {
                case 0:
                    if(user.getVipLevel()!=0){
                        sysInfo.setCreateTime(new Date());
                        sysInfo.setUserId(user.getId());
                        sysInfo.setTitle("会员等级变化通知");
                        sysInfo.setInfo("您好,您的会员等级由 VIP"+user.getVipLevel()+"变为 VIP0");
                        user.setVipLevel(0);
                        user.setUpdateTime(new Date());
                        this.updateById(user);
                    }
                    break;
                case 1:
                    if(user.getVipLevel()!=2){
                        sysInfo.setCreateTime(new Date());
                        sysInfo.setUserId(user.getId());
                        sysInfo.setTitle("会员等级变化通知");
                        sysInfo.setInfo("您好,您的会员等级由 VIP"+user.getVipLevel()+"变为 VIP2");
                        user.setVipLevel(2);
                        user.setUpdateTime(new Date());
                        this.updateById(user);
                    }
                    break;
                case 2:
                    if(user.getVipLevel()!=3){
                        sysInfo.setCreateTime(new Date());
                        sysInfo.setUserId(user.getId());
                        sysInfo.setTitle("会员等级变化通知");
                        sysInfo.setInfo("您好,您的会员等级由 VIP"+user.getVipLevel()+"变为 VIP3");
                        user.setVipLevel(3);
                        user.setUpdateTime(new Date());
                        this.updateById(user);
                    }
                    break;
                case 3:
                    if(user.getVipLevel()!=4){
                        sysInfo.setCreateTime(new Date());
                        sysInfo.setUserId(user.getId());
                        sysInfo.setTitle("会员等级变化通知");
                        sysInfo.setInfo("您好,您的会员等级由 VIP"+user.getVipLevel()+"变为 VIP4");
                        user.setVipLevel(4);
                        user.setUpdateTime(new Date());
                        this.updateById(user);
                    }
                    break;
            }
            sysInfo.setReaded(0);
            if(sysInfo.getUserId()!=null){
                sysInfos.add(sysInfo);
            }
        });

        return sysInfos;
    }

    @Override
    @Transactional
    public UserEntity changeHeadUrl(Map<String, Object> params) {

        Integer userId = (Integer) params.get("userId");
        String headImg = (String) params.get("headImg");

        UserEntity user = this.getById(userId);
        user.setHeadImg(headImg);
        boolean b = this.updateById(user);
        if(b){
            return user;
        }

        return null;
    }

    /**
     * @param params
     * @return
     * 修改密码
     */
    @Override
    @Transactional
    public int updatepsd(Map<String, String> params) {

        String userId = params.get("userId");
        String originPsd = params.get("originPsd");
        String newPsd = params.get("newPsd");

        UserEntity user = this.getById(Integer.valueOf(userId));

        if(StringUtils.isEmpty(newPsd)){
            //请输入新密码
            return 1;
        }
        if(StringUtils.isEmpty(originPsd)){
            //请输入旧密码
            return 2;
        }

        if(newPsd.length()<6){
            //新密码长度至少为6位
            return 3;
        }

        if(!user.getPassword().equals(originPsd)){
            //旧密码不匹配
            return 4;
        }

        if(newPsd.equals(user.getPassword())){
            //新密码不能与旧密码相同
            return 5;
        }

        //修改密码
        user.setPassword(newPsd);
        this.updateById(user);

        return 0;
    }

}