package com.qcw.parksys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * 
 * @author qinfeng
 * @email zuihou57124@gmail.com
 * @date 2020-08-03 09:33:17
 */
@Data
@TableName("space")
public class SpaceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 车位id
	 */
	@TableId
	private Integer id;

	/**
	 * 车位地区id
	 */
	private String positionId;

	/**
	 * 所属停车场 id
	 */
	private Integer parkId;

	/**
	 * 车位类型id
	 */
	private String typeId;
	/**
	 * 车位状态
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
	 * 车位号(与车位id不同,车位号是所在地区或停车场的编号)
	 */
	private Integer numPosition;

	/**
	 * 是否可以打折
	 */
	private Integer ableDiscount = 0;

	/**
	 * 是否开启打折  0 不开启 1开启
	 */
	private Integer isDiscount = 0;

	/**
	 * 打几折  1--原价
	 */
	private Float discount;

	/**
	 * 优惠到期时间
	 */
	private Date stopTime;

}
