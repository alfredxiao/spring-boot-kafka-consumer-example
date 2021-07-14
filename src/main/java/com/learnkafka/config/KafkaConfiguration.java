package com.learnkafka.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.backoff.BackOff;

import java.util.Objects;

@Configuration
@EnableKafka
public class KafkaConfiguration {
    @Bean
    public RetryTemplate kafkaConsumerRetryTemplate() {
        return RetryTemplate.builder()
                .maxAttempts(5)
                .notRetryOn(IllegalArgumentException.class)
                .traversingCauses()
                .exponentialBackoff(1000L, 1.1, 20_000L, true)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(name = "kafkaConsumerRetryTemplate")
    public BackOff kafkaConsumerBackOff() {
        ExponentialBackOffWithMaxRetries backOff = new ExponentialBackOffWithMaxRetries(5);
        backOff.setInitialInterval(1_000L);
        backOff.setMultiplier(2.0);
        backOff.setMaxInterval(10_000L);

        return backOff;
    }

    /* Boot will autowire this into the container factory. */
    @Bean
    public SeekToCurrentErrorHandler errorHandler(DeadLetterPublishingRecoverer deadLetterPublishingRecoverer,
                                                  @Autowired(required = false) @Qualifier("kafkaConsumerBackOff") BackOff backOff) {
        return Objects.isNull(backOff) ?
                new SeekToCurrentErrorHandler(deadLetterPublishingRecoverer) :
                new SeekToCurrentErrorHandler(deadLetterPublishingRecoverer, backOff);
    }

    /* Configure the {@link DeadLetterPublishingRecoverer} to publish poison pill bytes to a dead letter topic: "*.DLT" */
    @Bean
    public DeadLetterPublishingRecoverer dltPublisher(KafkaTemplate<?, ?> bytesTemplate) {
        return new DeadLetterPublishingRecoverer(bytesTemplate);
    }
}
