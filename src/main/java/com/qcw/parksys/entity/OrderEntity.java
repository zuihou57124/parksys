package com.qcw.parksys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("order_")
public class OrderEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 订单id
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 支付宝订单号
	 */
	private String outTradeNo;

	/**
	 * 用户id
	 */
	private Integer userId;
	/**
	 * 车位id
	 */
	private Integer spaceId;
	/**
	 * 租车位时长 (小时)
	 */
	private Integer duration;
	/**
	 * 应付总额
	 */
	private Float totalPayable;
	/**
	 * 实付总额
	 */
	private Float totalReal;

	/**
	 * 订单状态
	 */
	private Integer status;

	/**
	 * 订单创建时间
	 */
	private Date createTime;

	/**
	 * 支付日期
	 */
	private Date payTime;

	/**
	 * 是否已被系统定时任务扫描  (0 未扫描,1 已扫描 默认 0)
	 */
	private Integer scaned;

	/**
	 * 过期状态 (到期状态 0 即将到期,1 已经到期, 3 时间足够)
	 */
	private Integer validStatus;

	/**
	 * 支付宝二维码
	 */
	private String qrCodeUrl;


}
