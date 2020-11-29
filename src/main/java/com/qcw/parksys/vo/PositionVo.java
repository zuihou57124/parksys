package com.qcw.parksys.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 
 * 
 * @author qinfeng
 * @email zuihou57124@gmail.com
 * @date 2020-09-30 08:48:38
 */
@Data
public class PositionVo {

	private Integer id;

	/**
	 * 省市区 id
	 */
	private Integer geoId;

	/**
	 * 车位所在省市区
	 */
	private String province;

	private String city;

	private String region;

	/**
	 * 停车场名称
	 */
	private String name;

	/**
	 * 停车场图片
	 */
	private String imgUrl;

	/**
	 * 车位总数
	 */
	private Integer num;
	/**
	 * 剩余车位
	 */
	private Integer rest;

	/**
	 * 车位列表
	 */
	private List<SpaceVo> spaceVoList;

}
