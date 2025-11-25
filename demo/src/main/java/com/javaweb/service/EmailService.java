package com.javaweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtp(String to, String otp) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Mã OTP đặt lại mật khẩu");
        msg.setText("Mã OTP của bạn là: " + otp + "\nOTP có hiệu lực trong 1 phút.");

        mailSender.send(msg);
    }
}
