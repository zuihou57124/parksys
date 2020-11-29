package com.qcw.parksys.vo;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 车位 vo
 */
@Data
public class SpaceVo implements Serializable {


    private Integer id;

    /**
     * 车位所在省市区
     */
    private String province;

    private String city;

    private String region;

    /**
     * 所属停车场 id
     */
    private Integer parkId;

    /**
     * 所属停车场 name
     */
    private String parkName;

    /**
     * 车位所在地点(详细)
     */
    private String position;

    /**
     * 车位价格 元/小时
     */
    private Integer price;

    /**
     * 车位类型(货车，小车，摩托车等)
     */
    private String type;

    /**
     * 车位状态 (是否被占用，处于维修状态等)
     */
    private Integer status;

    /**
     * 下次到期时间
     */
    private Date nextTime;

    /**
     * 图片地址
     */
    private String img;

    /**
     * 是否可以打折
     */
    private Integer ableDiscount;

    /**
     * 是否开启打折  0 不开启 1开启
     */
    private Integer isDiscount;

    /**
     * 打几折  1--原价
     */
    private Float discount;

    /**
     * 优惠到期时间
     */
    private Date stopTime;

}
