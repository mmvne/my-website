package com.example.mywebsite.components;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
public class MailComponents {

    private final JavaMailSender javaMailSender;

    public MailComponents(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }


    public boolean sendMail(String email, String subject, String text){
        // html이 가능한 메일 보내기
        boolean result = false;

        MimeMessagePreparator msg = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                mimeMessageHelper.setTo(email);
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setText(text,true);
            }
        };

        try {
            javaMailSender.send(msg);
            result = true;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return result;
    }

}
