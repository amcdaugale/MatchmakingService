package com.mcdaale.capstone.client;

//import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.TimeUnit;

public class MatchmakingClient {
    public static void main(String[] args) {
        int userId, gameId;
        if (args.length == 2 && isNumericString(args[0]) && isNumericString(args[1])) {
            userId = Integer.parseInt(args[0]);
            gameId = Integer.parseInt(args[1]);
        } else {
            System.err.println("Usage: MatchmakingClient (int)<clinetId> (int)<gameId>");
            System.exit(1);
        }
        String url = "ws://127.0.0.1:8080/ws-raw";

        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new StringMessageConverter());
//        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.afterPropertiesSet();
        stompClient.setTaskScheduler(taskScheduler);
        stompClient.setDefaultHeartbeat(new long[]{10000, 10000});

        ClientStompSessionHandler sessionHandler = new ClientStompSessionHandler();
        stompClient.connectAsync(url, sessionHandler);

        try {
            if (!sessionHandler.await(10, TimeUnit.SECONDS)) {
                System.err.println("Timed out waiting for WebSocket connection");
            }
        } catch (InterruptedException e) {
            System.err.println("Interrupted while waiting for WebSocket connection");
            System.err.println(e.getMessage());
        }

        taskScheduler.shutdown();
        System.exit(0);
    }

    private static boolean isNumericString(String arg) {
        boolean result = false;

        if (arg != null) {
            try {
                Double.parseDouble(arg);
                result = true;
            } catch (NumberFormatException e) {
                System.err.println("Non numeric string: " + e.getMessage());
            }
        }

        return result;
    }
}