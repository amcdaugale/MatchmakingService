package com.mcdaale.capstone.matchmaker;

import com.mcdaale.capstone.matchmaker.request.MatchRequestJson;
import com.mcdaale.capstone.matchmaker.request.MatchResponseJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MatchingController {
    private static final String TAG = MatchingController.class.getSimpleName();
    private final SimpMessagingTemplate messagingTemplate;
    private final MatchmakingService matchmakingService;


    @Autowired
    public MatchingController(SimpMessagingTemplate messagingTemplate, MatchmakingService matchmakingService) {
        this.messagingTemplate = messagingTemplate;
        this.matchmakingService = matchmakingService;
    }


    @MessageMapping("/matchInit") // /app/matchInit
    @SendTo("/topic/greetings") // /topic/greetings
    public String initMatch(String message, Principal principal) throws Exception {
        Thread.sleep(1000); // simulated delay
        Log.d(TAG, "From log: %S", message);
        MatchRequestJson matchRequestJson = MatchRequestJson.fromJsonString(message);

        if (null == matchRequestJson) {
            return "invalid match request";
        }

        matchmakingService.newUser(matchRequestJson.getUserId(),  matchRequestJson.getGameId());

        return "Welcome player " + matchRequestJson.getUserId();
    }

    @GetMapping("/test")
    public String test() {
        System.out.println("Matchmaker application responding to test!");
        return "test";
    }
}