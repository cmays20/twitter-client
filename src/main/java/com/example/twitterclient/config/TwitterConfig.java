package com.example.twitterclient.config;

import com.example.twitterclient.kafka.KafkaProducer;
import com.example.twitterclient.twitter.TwitterMessageProducer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;
import twitter4j.Status;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

@Configuration
public class TwitterConfig {
    Log log = LogFactory.getLog(TwitterConfig.class);

    @Bean
    TwitterStreamFactory twitterStreamFactory() {
        return new TwitterStreamFactory();
    }

    @Bean
    TwitterStream twitterStream(TwitterStreamFactory twitterStreamFactory) {
        return twitterStreamFactory.getInstance();
    }

    @Bean
    MessageChannel outputChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    TwitterMessageProducer twitterMessageProducer(
            TwitterStream twitterStream, MessageChannel outputChannel) {

        TwitterMessageProducer twitterMessageProducer =
                new TwitterMessageProducer(twitterStream, outputChannel);

        //twitterMessageProducer.setTerms(new String[]{"mesos", "DC/OS", "Mesosphere","k8s","kubernetes"});

        double[][] locaitons = {new double[]{-89.250121, 41.320313},new double[]{-87.229527, 42.453051}};
        twitterMessageProducer.setLocations(locaitons);
        twitterMessageProducer.setLanguages(new String[]{"en"});

        return twitterMessageProducer;
    }

    @Bean
    IntegrationFlow twitterFlow(MessageChannel outputChannel, KafkaProducer kafkaProducer) {
        return IntegrationFlows.from(outputChannel)
                .transform(Status::getText)
                .handle(m -> kafkaProducer.send(m.getPayload().toString()))
                .get();
    }
}
