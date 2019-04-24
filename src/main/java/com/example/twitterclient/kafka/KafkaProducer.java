package com.example.twitterclient.kafka;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaProducer {
    private final Log log = LogFactory.getLog(KafkaProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String payload) {
        log.info("sending payload=" + payload);
        kafkaTemplate.send(topic, payload);
    }

    public void send(String payload) {
        this.send(kafkaTemplate.getDefaultTopic(), payload);
    }
}
