package com.mcdaale.capstone.matchmaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MatchmakingService {
    private final SimpMessagingTemplate messagingTemplate;

    private static String TAG = MatchmakingService.class.getSimpleName();

    @Autowired
    public MatchmakingService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public String printMessage(String message) {
        Log.d(TAG, "Your message: %s", message);
        sendUserMessage(11, String.format("Your message was: %s", message));
        return message;
    }

    public void sendUserMessage(int userId, String message) {
        messagingTemplate.convertAndSend("/topic/u" + userId, "System notification: " + message);
    }

    public void sendMatchBroadcast(int matchId, String message) {
        messagingTemplate.convertAndSend("/topic/m" + matchId , "System notification: " + message);
    }
}
