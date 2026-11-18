package com.example.campusrideshare.messaging;

import com.example.campusrideshare.config.MessagingProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class RideStatusLoggingConsumer {

    private static final Logger logger = LoggerFactory.getLogger(RideStatusLoggingConsumer.class);

    private final MessagingProperties properties;

    public RideStatusLoggingConsumer(MessagingProperties properties) {
        this.properties = properties;
    }

    @KafkaListener(
            topics = "${campus-ride.messaging.ride-status-topic}",
            groupId = "${campus-ride.messaging.logging-group}")
    public void logRideStatus(RideStatusEvent event) {
        logger.info("Ride status event [{}] {}: {} -> {}",
                event.getStatus(), event.getRideId(),
                event.getDeparture(), event.getDestination());
        logger.debug("Event detail: {}", event.toStatusEmail());
    }
}
