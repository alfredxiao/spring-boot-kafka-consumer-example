package com.learnkafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learnkafka.service.LibraryEventsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

//@Component
@Slf4j
public class LibraryEventsConsumerReply {

    @Autowired
    LibraryEventsService libraryEventsService;

    @KafkaListener(
            topics = {"library-events"}
    )
    @SendTo("library-events-reply")
    public String onMessage(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        log.info("Received ConsumerRecord : {} ", consumerRecord);
        libraryEventsService.processLibraryEvent(consumerRecord);
        return consumerRecord.value();
    }
}
