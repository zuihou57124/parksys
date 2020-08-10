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
@TableName("vip")
public class VipEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * vip_id
	 */
	@TableId
	private Integer id;
	/**
	 * vip等级描述
	 */
	private String vipName;
	/**
	 * vip等级
	 */
	private Integer vipLevel;
	/**
	 * 打几折
	 */
	private Integer discount;
	/**
	 * 需要消费多少金额才能达到该vip等级
	 */
	private Integer total;

}
