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
@TableName("user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 用户账号
	 */
	private String username;

	/**
	 * 用户邮箱
	 */
	private String email;

	/**
	 * 密码
	 */
	private String password;
	/**
	 * 账户状态
	 */
	private Integer status;
	/**
	 * 账户余额
	 */
	private Float money;
	/**
	 * vip等级
	 */
	private Integer vipLevel;
	/**
	 * 积分信息
	 */
	private Integer integral;
	/**
	 * 花费总额
	 */
	private Float totalCost;
	/**
	 * 最近一次修改账户日期
	 */
	private Date updateTime;
	/**
	 * 账户创建日期
	 */
	private Date createTime;

	/**
	 * 头像地址
	 */
	private String headImg;

}
