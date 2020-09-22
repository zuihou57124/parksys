package com.qcw.parksys.vo;


import lombok.Data;

/**
 * 支付vo
 */
@Data
public class PayOrderVo {

    String subuject;

    Float total;

    Integer orderId;

    Boolean changed = false;

}
