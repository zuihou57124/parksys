package com.qcw.parksys.vo;

import lombok.Data;

/**
 * 用户预约车位vo
 */
@Data
public class BookParkVo {

    //用户id
    private Integer userId;

    //车位id
    private Integer spaceId;

    //预约时长
    private Integer duration;

}
