package com.qcw.parksys.vo;

import lombok.Data;

import java.util.Date;

/**
 * 发送邮件 vo
 */
@Data
public class MailSendVo {

    private String id;//邮件id

    private String from;//邮件发送人

    private String to;//邮件接收人（多个邮箱则用逗号","隔开）

    private String subject;//邮件主题

    private String text;//邮件内容

    private Date sentDate;//发送时间

    private String cc;//抄送（多个邮箱则用逗号","隔开）

    private String bcc;//密送（多个邮箱则用逗号","隔开）

    private String status;//状态

    private String error;//报错信息

}
