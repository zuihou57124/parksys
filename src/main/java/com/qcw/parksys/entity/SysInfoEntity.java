package com.qcw.parksys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author qinfeng
 * @email zuihou57124@gmail.com
 * @date 2020-08-03 09:33:17
 */
@Data
@TableName("sysinfo")
public class SysInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 消息id
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 用户id
	 */
	private Integer userId;

	/**
	 * 消息标题
	 */
	private String title;

	/**
	 * 消息内容
	 */
	private String info;

	/**
	 * 用户是否已读(0 已读,1 未读)
	 */
	private Integer readed;

	/**
	 * 生成时间
	 */
	private Date createTime;

}
