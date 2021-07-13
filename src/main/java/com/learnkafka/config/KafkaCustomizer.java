package com.learnkafka.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.config.AbstractKafkaListenerContainerFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaCustomizer {

    public KafkaCustomizer(AbstractKafkaListenerContainerFactory<?, ?, ?> factory) {
        log.info("Customizing KafkaListenerContainerFactory...");
        factory.getContainerProperties().setOnlyLogRecordMetadata(false);
//        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
    }
}
