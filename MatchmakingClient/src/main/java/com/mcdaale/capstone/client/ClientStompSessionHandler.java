package com.mcdaale.capstone.client;

import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ClientStompSessionHandler extends StompSessionHandlerAdapter {
    private final CountDownLatch latch = new CountDownLatch(1);

    private int userId;
    private int matchId;

    public ClientStompSessionHandler(int userId) {
        userId = userId;
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
//        super.handleException(session, command, headers, payload, exception);
        System.err.println("Exception thrown: " + exception.getMessage());
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
//        super.handleTransportError(session, exception);
        System.err.println("Transport error: " + exception.getMessage());
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
//        super.afterConnected(session, connectedHeaders);
        System.out.println("Connected " + connectedHeaders.getDestination());

//        session.subscribe(connectedHeaders.getDestination(), this);
        session.subscribe("/topic/u" + userId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("Received: " + payload.toString());

                session.subscribe()
                //  latch.countDown();
            }
        });
        session.send("/app/matchInit", "Hi from Client...");
    }

    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return latch.await(timeout, unit);
    }
}
