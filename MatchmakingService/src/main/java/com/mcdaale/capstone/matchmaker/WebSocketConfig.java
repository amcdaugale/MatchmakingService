package com.mcdaale.capstone.matchmaker;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * The Message broker for the matchmaker service.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Cofigure the broker.
     * @param config configuration to add brokers.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        System.out.println("Matchmaker application broker configured!");
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Register socket endpoints for clients to open up a socket too.
     * @param registry registry to add any new endpoints..
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        System.out.println("Matchmaker registering endpoints!");
        // entrypoint for websocket.
        registry.addEndpoint("/ws-raw").setAllowedOrigins("*");

    }
}
