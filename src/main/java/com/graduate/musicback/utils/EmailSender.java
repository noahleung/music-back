package com.graduate.musicback.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ValidateCode validateCode;

    public String sendRegistCode (String sendTo) throws MessagingException {
        String code = validateCode.getValidateCode();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        messageHelper.setSubject("肇院听用户注册");
        messageHelper.setFrom("536323173@qq.com");
        messageHelper.setTo(sendTo);
        messageHelper.setText("你的注册验证码是:<h3>"+code+"</h3>", true);
        javaMailSender.send(messageHelper.getMimeMessage());
        return code;
    }

    public void sendMail(String subject,String sendTo,String text){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("536323173@qq.com");
        message.setTo(sendTo);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);

    }
}
