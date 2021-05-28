package com.example.demo;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.stereotype.Component;

@Component
@MessagingGateway(defaultRequestChannel = "sportOutputChannel")
public interface PubsubOutboundGateway {

    @Gateway(requestChannel = "sportOutputChannel")
    void sendToSportTopic(String text);

    @Gateway(requestChannel = "cookingOutputChannel")
    void sendToCookingTopic(String text);

    @Gateway(requestChannel = "newsOutputChannel")
    void sendToNewsTopic(String text);
}
