package com.learnkafka.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.KafkaHeaders;

@Configuration
@EnableKafka
public class KafkaConfig {
    // a bean like below is NOT autowired into container factory
//    @Bean
//    KafkaListenerErrorHandler eh(DeadLetterPublishingRecoverer recoverer) {
//        return (msg, ex) -> {
//            if (msg.getHeaders().get(KafkaHeaders.DELIVERY_ATTEMPT, Integer.class) > 3) {
//                recoverer.accept(msg.getHeaders().get(KafkaHeaders.RAW_DATA, ConsumerRecord.class), ex);
//                return "FAILED";
//            }
//            throw ex;
//        };
//    }

    /* Boot will autowire this into the container factory. */
    @Bean
    public SeekToCurrentErrorHandler errorHandler(DeadLetterPublishingRecoverer deadLetterPublishingRecoverer) {
        return new SeekToCurrentErrorHandler(deadLetterPublishingRecoverer);
    }

    /** Configure the {@link DeadLetterPublishingRecoverer} to publish poison pill bytes to a dead letter topic:
     * "*.DLT".
     */
    @Bean
    public DeadLetterPublishingRecoverer publisher(KafkaTemplate bytesTemplate) {
        return new DeadLetterPublishingRecoverer(bytesTemplate);
    }
}
