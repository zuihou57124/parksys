package com.qcw.parksys.vo;

import lombok.Data;

import java.util.Date;

/**
 * 用户订单 vo
 */
@Data
public class UserOrderVo {

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
     * 租用时长
     */
    private Integer duration;

    /**
     * 车位状态
     */
    private Integer spaceStatus;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 应付总额
     */
    private Float totalPayable;

    /**
     * 实付总额
     */
    private Float totalReal;

    /**
     * 订单创建日期
     */
    private Date createTime;

    /**
     * 支付日期
     */
    private Date payTime;

    /**
     * 车位到期时间
     */
    private Date validTime;

    /**
     * 订单到期状态(0 即将过期，1 已经过期 ，3时间足够)
     */
    private Integer validSatus;

}
