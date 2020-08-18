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
@TableName("position")
public class PositionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 地区id
	 */
	@TableId
	private Integer id;
	/**
	 * 地区名称
	 */
	private String positionName;

	/**
	 * 省市区
	 */
	private String geoPosition;

	/**
	 * 车位总数
	 */
	private Integer num;

	/**
	 * 剩余车位
	 */
	private Integer rest;

}
