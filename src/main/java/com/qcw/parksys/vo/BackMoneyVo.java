package com.qcw.parksys.vo;

import lombok.Data;

@Data
public class BackMoneyVo {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 车位id
     */
    private Integer spaceId;

}
