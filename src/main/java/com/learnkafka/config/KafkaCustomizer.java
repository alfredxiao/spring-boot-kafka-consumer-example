package com.learnkafka.config;

import com.learnkafka.errorhandling.SimpleContainerErrorHandler;
import com.learnkafka.errorhandling.SimpleKafkaListenerErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.config.AbstractKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.backoff.FixedBackOff;

@Component
@Slf4j
public class KafkaCustomizer {

    public KafkaCustomizer(AbstractKafkaListenerContainerFactory<?, ?, ?> factory, KafkaTemplate<Object, Object> template) {
        log.info("Customizing KafkaListenerContainerFactory...");
        factory.getContainerProperties().setOnlyLogRecordMetadata(false);
        factory.getContainerProperties().setDeliveryAttemptHeader(true);
//        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
//        factory.setErrorHandler(new SimpleContainerErrorHandler());
//        factory.setErrorHandler(new SeekToCurrentErrorHandler(new FixedBackOff(0, 2)));
    }
}
