package com.banking.digital_banking_platform.banking.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    public void sendSuspiciousLoginAlert(String toEmail,
                                         String device,
                                         String ip,
                                         String location){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("New Login Detected");
        message.setText(
                "Dear User,\n\n"+
                        "we detected a new login on your account.\n\n"+
                        "Location:"+location+"\n\n"+
                        "IP Address:"+ip+"\n"+

                        "If this wasn't you,please reset your password immediately.\n\n"+


                        "Regards,\nSecurity Team"
        );
        mailSender.send(message);
    }
}
