package com.learnkafka.errorhandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
// This error handler is a listener error handler, not container error handler
// SeekToCurrentErrorHandler is a container error handler
public class SimpleKafkaListenerErrorHandler implements KafkaListenerErrorHandler {
    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException exception) {
        log.info("in SimpleKafkaListenerErrorHandler.handleError, message is {}, exception is {} ", message, exception);
        // if (exception != null) throw exception;
        return null;
    }
}
