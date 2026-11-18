package com.example.campusrideshare.messaging;

import com.example.campusrideshare.config.MessagingProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class RideStatusEventProducer {
    private static final Logger logger = LoggerFactory.getLogger(RideStatusEventProducer.class);
    private final KafkaTemplate<String, RideStatusEvent> kafkaTemplate;
    private final MessagingProperties properties;

    public RideStatusEventProducer(KafkaTemplate<String, RideStatusEvent> template, MessagingProperties properties) {
        kafkaTemplate = template;
        this.properties = properties;
    }

    public void send(RideStatusEvent event) {
        logger.debug("Publishing {} event for ride {}", event.getStatus(), event.getRideId());
        kafkaTemplate.send(properties.getRideStatusTopic(), event.getRideId().toString(), event);
    }
}
