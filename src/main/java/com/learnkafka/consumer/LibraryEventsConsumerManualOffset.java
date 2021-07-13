package com.learnkafka.consumer;

import com.learnkafka.service.LibraryEventsService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

//@Component
@Slf4j
public class LibraryEventsConsumerManualOffset implements AcknowledgingMessageListener<Integer, String> {
    @Autowired
    LibraryEventsService libraryEventsService;

    @SneakyThrows
    @KafkaListener(
            topics = {"library-events"}
    )
    public void onMessage(@NotNull ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment) {
        log.info("Received ConsumerRecord : {} ", consumerRecord);
        libraryEventsService.processLibraryEvent(consumerRecord);
        acknowledgment.acknowledge();
    }
}
