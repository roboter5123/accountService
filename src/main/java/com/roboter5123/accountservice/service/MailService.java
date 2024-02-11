package com.roboter5123.accountservice.service;
import jakarta.mail.MessagingException;

public interface MailService {

    void sendActivationMail(String email, String activationToken) throws MessagingException;
}
