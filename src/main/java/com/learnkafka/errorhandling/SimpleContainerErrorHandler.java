package com.learnkafka.errorhandling;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.lang.Nullable;

@Slf4j
public class SimpleContainerErrorHandler implements ErrorHandler {

    @Override
    public void handle(Exception exception, @Nullable ConsumerRecord<?, ?> record) {
        log.info("ERROR seen in SimpleContainerErrorHandler, record is {}, error is {} ", record, exception);
    }
}
