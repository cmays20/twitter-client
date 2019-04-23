package com.example.twitterclient.twitter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterStream;

public class TwitterMessageProducer extends MessageProducerSupport {
    private final Log log = LogFactory.getLog(TwitterMessageProducer.class);

    private final TwitterStream twitterStream;

    private long[] follows;
    private String[] terms;
    private String[] languages;
    private double[][] locations;

    private StatusListener statusListener;
    private FilterQuery filterQuery;

    public TwitterMessageProducer(TwitterStream twitterStream, MessageChannel outputChannel) {
        this.twitterStream = twitterStream;
        setOutputChannel(outputChannel);
    }

    @Override
    protected void onInit() {
        super.onInit();

        statusListener = new StatusListener();

        long[] followsArray = null;

        if(follows == null) {
            follows = new long[0];
        }

        if(terms == null) {
            terms = new String[0];
        }

        filterQuery = new FilterQuery(0)
                .follow(follows)
                .track(terms)
                .language(languages)
                .locations(locations);
    }

    @Override
    public void doStart() {
        twitterStream.addListener(statusListener);
        twitterStream.filter(filterQuery);
    }

    @Override
    public void doStop() {
        twitterStream.cleanUp();
        twitterStream.clearListeners();
    }

    public void setFollows(long[] follows) {
        this.follows = follows;
    }

    public void setTerms(String[] terms) {
        this.terms = terms;
    }

    StatusListener getStatusListener() {
        return statusListener;
    }

    FilterQuery getFilterQuery() {
        return filterQuery;
    }

    public void setLanguages(String[] languages) {
        this.languages = languages;
    }

    public void setLocations(double[][] locations) {
        this.locations = locations;
    }

    class StatusListener extends StatusAdapter {

        @Override
        public void onStatus(Status status) {
            sendMessage(MessageBuilder.withPayload(status).build());
        }

        @Override
        public void onException(Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        @Override
        public void onStallWarning(StallWarning warning) {
            log.warn(warning.toString());
        }

    }
}
