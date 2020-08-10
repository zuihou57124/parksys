package com.qcw.parksys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("geoposition")
public class GeoPosition {

    private Integer id;

    private String ip;

    //省份
    private String pro;

    //城市
    private String city;

    //区域
    private String region;

}
