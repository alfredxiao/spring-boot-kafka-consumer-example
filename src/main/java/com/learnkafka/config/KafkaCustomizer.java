package com.learnkafka.config;

import com.learnkafka.errorhandling.SimpleContainerErrorHandler;
import com.learnkafka.errorhandling.SimpleKafkaListenerErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.config.AbstractKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.LoggingErrorHandler;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Objects;

@Component
@Slf4j
public class KafkaCustomizer {

    public KafkaCustomizer(AbstractKafkaListenerContainerFactory<?, ?, ?> factory,
                           KafkaTemplate<Object, Object> template,
                           @Autowired(required = false) @Qualifier("kafkaConsumerRetryTemplate") RetryTemplate retryTemplate) {
        log.info("Customizing KafkaListenerContainerFactory...");
        factory.getContainerProperties().setOnlyLogRecordMetadata(false);
        factory.getContainerProperties().setDeliveryAttemptHeader(true);
//        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
//        factory.setErrorHandler(new SeekToCurrentErrorHandler(new FixedBackOff(0, 2)));
//        factory.setErrorHandler(new SimpleContainerErrorHandler());

        if (Objects.nonNull(retryTemplate)) {
            log.info("Enabling retryTemplate and LoggingErrorHandler");
            factory.setRetryTemplate(retryTemplate);
            factory.setErrorHandler(new LoggingErrorHandler());

            factory.setRecoveryCallback(context -> {
                log.info("Inside the recovery callback");

                // not throwing exception means RECOVERED, will NOT move on to error handler
                return null;
            });
        }
    }

}
