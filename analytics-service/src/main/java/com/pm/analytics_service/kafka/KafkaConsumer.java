package com.pm.analytics_service.kafka;

import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;
import patient.events.PatientEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class KafkaConsumer {

    private final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "patient", groupId = "analytics-service")
    public void consumeEvent(byte[] event) {
        
        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);
            logger.info("Received patient event: patientId: {}, patientName: {}, patientEmail: {}", patientEvent.getPatientId(), patientEvent.getName(), patientEvent.getEmail());
        } catch (Exception e) {
            logger.error("Failed to parse patient event", e);
        }

    }
}
