package com.roboter5123.accountservice.service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    @Value("${frontend.url}")
    private String frontendUrl;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendActivationMail(String email, String activationToken) throws MessagingException {
        String htmlMsg = "<p>Please click this link to activate your account: <a href=\"" + frontendUrl + "/accounts/"+ email +"/activated?token=" + activationToken + "\">Link</a></p>";
        String subject = "Activate your automatic feeder account";
        sendMail(email, htmlMsg, subject);
    }

    public void sendMail(String email, String htmlMessage, String subject) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(htmlMessage, true);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setFrom("automatic.feeder.service@gmail.com");
        mailSender.send(mimeMessage);
    }
}
