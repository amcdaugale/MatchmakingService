package com.mcdaale.capstone.matchmaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MatchingController {
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
        String username = (principal != null) ? principal.getName() : "Anonymous";

        System.out.printf("Matchmaker application responding to %s!", username);
        System.out.println("STD: Hello, " + message + "!");
        Log.d("GreetingController", "From log: %S", message);

        sendCustomMessage("SEND_MSG:Hello, " + message + "!");


        matchmakingService.printMessage("Hello from controller, in matchmaking service.");
        return "RETURN: Hello, " + message + "!";
    }

    public void sendUserMessage(int userId, String message) {
        messagingTemplate.convertAndSend("/topic/u" + userId, "System notification: " + message);
    }

    public void sendMatchBroadcast(int matchId, String message) {
        messagingTemplate.convertAndSend("/topic/m" + matchId , "System notification: " + message);
    }

    public void sendCustomMessage(String payload) {
        messagingTemplate.convertAndSend("/topic/greetings", "System notification: " + payload);
    }

    public void sendCustomMessage(String payload, String username) {
        messagingTemplate.convertAndSendToUser(username, "/queue/private", "Hello " + username);
    }

    @GetMapping("/test")
    public String test() {
        System.out.println("Matchmaker application responding to test!");
        return "test";
    }
}