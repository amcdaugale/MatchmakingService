package com.mcdaale.capstone.client;

import com.mcdaale.capstone.client.request.MatchRequestJson;
import com.mcdaale.capstone.client.request.MatchResponseJson;
import com.mcdaale.capstone.client.request.MatchStatusJson;
import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ClientStompSessionHandler extends StompSessionHandlerAdapter {
    private static final String TAG = ClientStompSessionHandler.class.getSimpleName();
    private final CountDownLatch latch = new CountDownLatch(1);

    private long userId;
    private long matchId;
    private long gameId;

    public ClientStompSessionHandler(long userId, long gameId) {
        this.userId = userId;
        this.gameId = gameId;
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        super.handleException(session, command, headers, payload, exception);
        Log.e(TAG, "Exception thrown: " + exception.getMessage());
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        super.handleTransportError(session, exception);
        Log.e(TAG, "Transport error: " + exception.getMessage());
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        super.afterConnected(session, connectedHeaders);
        Log.d(TAG, "Connected " + connectedHeaders.getDestination());
        subscribeUser(session);
        subscribeGreeting(session);

        MatchRequestJson matchRequestJson = new MatchRequestJson(userId, gameId);
        session.send("/app/matchInit", matchRequestJson.toJsonString());
    }

    private void subscribeGreeting(StompSession session) {
        session.subscribe("/topic/greeting", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
               return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Log.d(TAG, "Received frame " + headers.getDestination());
                Log.d(TAG, "Payload: " + payload);
            }
        });
    }

    private void subscribeUser(StompSession session) {
        session.subscribe("/topic/u" + userId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Log.d(TAG, "Received: " + payload.toString());
                MatchResponseJson matchResponseJson = MatchResponseJson.fromJsonString(payload.toString());

                if (null != matchResponseJson) {
                    subscribeMatch(session, matchResponseJson.getMatchId());
                }
            }
        });
    }

    private void subscribeMatch(StompSession session, long matchId) {
        session.subscribe("/topic/m" + matchId, new StompFrameHandler() {

            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Log.d(TAG, "Received: " + payload.toString());
                MatchStatusJson matchStatusJson = MatchStatusJson.fromJsonString(payload.toString());

                if (null != matchStatusJson) {
                    Log.d(TAG, "Players: %s/%s ", matchStatusJson.getPlayerCount(), matchStatusJson.getMaxPlayerCount());

                    if (matchStatusJson.isMatched()) {
                        Log.d(TAG, "Matched!");
                        latch.countDown();
                    }
                } else {
                    Log.e(TAG, "Malformed json.");
                }
            }
        });
    }



    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return latch.await(timeout, unit);
    }
}
