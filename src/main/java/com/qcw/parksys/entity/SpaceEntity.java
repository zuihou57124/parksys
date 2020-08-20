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

}
