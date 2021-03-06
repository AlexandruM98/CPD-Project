package com.example.demo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import org.springframework.cloud.gcp.pubsub.integration.outbound.PubSubMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;

@Component
public class Config {


    @Bean
    @ServiceActivator(inputChannel = "sportOutputChannel")
    public MessageHandler messageSenderToSport(PubSubTemplate pubsubTemplate) {
        return new PubSubMessageHandler(pubsubTemplate, "Sport");
    }

    @Bean
    @ServiceActivator(inputChannel = "cookingOutputChannel")
    public MessageHandler messageSenderToCooking(PubSubTemplate pubsubTemplate) {
        return new PubSubMessageHandler(pubsubTemplate, "Cooking");
    }

    @Bean
    @ServiceActivator(inputChannel = "newsOutputChannel")
    public MessageHandler messageSenderToNews(PubSubTemplate pubsubTemplate) {
        return new PubSubMessageHandler(pubsubTemplate, "News");
    }

    @Bean
    public PubSubInboundChannelAdapter sportChannelAdapter(
            @Qualifier("sportInputChannel") MessageChannel inputChannel,
            PubSubTemplate pubSubTemplate) {
        PubSubInboundChannelAdapter adapter =
                new PubSubInboundChannelAdapter(pubSubTemplate, "Sport-sub");
        adapter.setOutputChannel(inputChannel);

        return adapter;
    }

    @Bean
    public MessageChannel sportInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel sportOutputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel cookingOutputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel newsOutputChannel() {
        return new DirectChannel();
    }

    @ServiceActivator(inputChannel = "sportInputChannel")
    public void messageReceiver2(String payload) {
        if (!Controller.publishedMessage.equals(payload)) {
            System.out.println("Message arrived! Sport Payload: " + payload);
        }
    }

}
