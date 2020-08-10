package com.qcw.parksys.vo;

import lombok.Data;

import java.util.Date;

/**
 * 用户预约情况 vo
 */
@Data
public class UserBooksVo {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 车位id
     */
    private Integer spaceId;

    /**
     * 订单id
     */
    private Integer orderId;

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
     * 预约状态
     */
    private Integer status;

    /**
     * 车位状态
     */
    private Integer spaceStatus;

    /**
     * 预约失效时间
     */
    private Date validTime;

}
