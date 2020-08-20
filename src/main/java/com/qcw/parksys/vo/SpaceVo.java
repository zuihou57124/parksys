package com.qcw.parksys.vo;


import lombok.Data;

import java.util.Date;

/**
 * 车位 vo
 */
@Data
public class SpaceVo {


    private Integer id;

    /**
     * 车位所在地区
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

}
