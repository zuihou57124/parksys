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
@TableName("type")
public class TypeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 车位类型id
	 */
	@TableId
	private Integer id;
	/**
	 * 类型名称
	 */
	private String typeName;
	/**
	 * 价格  元/小时
	 */
	private Integer price;

}
