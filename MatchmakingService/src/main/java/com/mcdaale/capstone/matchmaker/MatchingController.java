package com.mcdaale.capstone.matchmaker;

import com.mcdaale.capstone.matchmaker.request.MatchRequestJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

/**
 * Controller to handle match requests.
 */
@Controller
public class MatchingController {
    private static final String TAG = MatchingController.class.getSimpleName();
    private final SimpMessagingTemplate messagingTemplate;
    private final MatchmakingService matchmakingService;

    /**
     * Autowire the messaging template and our custom service.
     * @param messagingTemplate To send messages back to client.
     * @param matchmakingService Our service.
     */
    @Autowired
    public MatchingController(SimpMessagingTemplate messagingTemplate, MatchmakingService matchmakingService) {
        this.messagingTemplate = messagingTemplate;
        this.matchmakingService = matchmakingService;
    }

    /**
     * init match request from users who just opened the socket.
     * @param message String to process.
     * @param principal not sure...
     * @return Response to the user.
     * @throws Exception our method throws a generic exception,,, todo revise this.
     */
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

    /**
     * Test mapping.
     * @return sring "test"
     */
    @GetMapping("/test")
    public String test() {
        System.out.println("Matchmaker application responding to test!");
        return "test";
    }
}