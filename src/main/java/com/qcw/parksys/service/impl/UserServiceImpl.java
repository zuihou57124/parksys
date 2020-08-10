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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcw.parksys.common.utils.PageUtils;
import com.qcw.parksys.common.utils.Query;

import com.qcw.parksys.dao.UserDao;
import com.qcw.parksys.entity.UserEntity;
import com.qcw.parksys.service.UserService;
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

}