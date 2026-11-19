package com.example.campusrideshare.services;

import com.example.campusrideshare.config.EmailProperties;
import com.example.campusrideshare.data.UserRepository;
import com.example.campusrideshare.messaging.RideStatusEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class RideStatusEmailService {
    private static final Logger logger = LoggerFactory.getLogger(RideStatusEmailService.class);
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final EmailProperties emailProperties;

    public RideStatusEmailService(JavaMailSender jms, UserRepository ur, EmailProperties ep) {
        mailSender = jms;
        userRepository = ur;
        emailProperties = ep;
    }

    public void sendMessage(RideStatusEvent event) {
        if (event.getDriverEmail() == null || event.getDriverEmail().isBlank()) {
            logger.warn("No driver email for ride {} — email skipped", event.getRideId());
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(emailProperties.getFromAddress());
        message.setSubject("Campus Ride Status: " + event.getStatus());
        message.setTo(event.getDriverEmail());
        message.setText(event.toStatusEmail());

        try {
            mailSender.send(message);
            logger.info("Ride status email sent for event {} for ride {}",
                    event.getEventId(), event.getRideId());
        } catch (MailException ex) {
            logger.error("Ride status email failed for {}: {}", event.getRideId(), ex.getMessage());
        }
    }

}
