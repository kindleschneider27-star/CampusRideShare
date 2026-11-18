package com.example.campusrideshare.messaging;

import com.example.campusrideshare.services.RideStatusEmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class RideStatusEmailConsumer {
    RideStatusEmailService emailService;

    public RideStatusEmailConsumer(RideStatusEmailService rs) {
        emailService = rs;
    }

    @KafkaListener(topics="${campus-ride.messaging.ride-status-topic}",
            groupId = "${campus-ride.messaging.email-group}")

    public void receive(RideStatusEvent event) {
        emailService.sendMessage(event);
    }
}
