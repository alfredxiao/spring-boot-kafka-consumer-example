package com.learnkafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learnkafka.service.LibraryEventsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.DeliveryAttemptAware;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.TopicPartitionOffset;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LibraryEventsConsumerWithListenerErrorHandling {

    @Autowired
    LibraryEventsService libraryEventsService;

    @KafkaListener(
            topics = {"library-events"},
            errorHandler = "simpleKafkaListenerErrorHandler"
    )
    // @Header(KafkaHeaders.DELIVERY_ATTEMPT) Integer delivery,
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord/*, Acknowledgment acknowledgment*/) throws JsonProcessingException {
        log.info("Received ConsumerRecord, with delivery_attempt: {}, : {} ", -1, consumerRecord);
        libraryEventsService.processLibraryEvent(consumerRecord);
        //acknowledgment.acknowledge();
    }
}

/*
  1. if simpleKafkaListenerErrorHandler.handleError() does not throw the original exception or new exception,
  this record is considered PROCESSED and won't be attempted again. consumer will move on to next message

  2. delivery_attempt can be enabled in container factory. once enabled, it is in record headers, also available in
  message headers in the listener error handler.

  3. if simpleKafkaListenerErrorHandler.handleError() keeps throwing exception (for the same record), there will also
  be totally 10 attempts to delivery the message (default behaviour)
    - After 10 attempts, the offset commit for this record is going to happen regardless of ack-mode being BATCH (default)
  or MANUAL.

  4. when in MANUAL ack mode, it is dangerous to have the listener error handler not throwing the exception, because
  this is telling spring: 1. the record is HANDLED and considered PROCESSED; 2. but the record is NOT acknowledged.
  result is:
 */