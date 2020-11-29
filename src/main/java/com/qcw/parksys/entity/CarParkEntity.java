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
 * @date 2020-09-30 08:48:38
 */
@Data
@TableName("car_park")
public class CarParkEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;

	/**
	 * 省市区 id
	 */
	private Integer geoId;

	/**
	 * 停车场名称
	 */
	private String name;

	/**
	 * 停车场图片
	 */
	private String imrUrl;

	/**
	 * 车位总数
	 */
	private Integer num;

	/**
	 * 剩余车位
	 */
	private Integer rest;

}
