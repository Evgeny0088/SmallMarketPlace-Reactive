package com.marketplace.mail.sender.service;

public interface MailService {

    void send(String emailTo, String subject, String body);

}
