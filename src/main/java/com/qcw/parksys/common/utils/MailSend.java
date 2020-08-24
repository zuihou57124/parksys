package com.qcw.parksys.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailSend {

    @Autowired
    JavaMailSender javaMailSender;

    /**
     * @param from
     * @param to
     * @param subject
     * @param text
     * @return
     * 发送注册验证码到指定邮箱
     */
    public boolean sendRegisterCode(String from,String to,String subject,String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        text = "您好,您的验证码是: \n"+text+",\n5分钟内有效";
        message.setText(text);
        message.setSubject(subject);

        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
