package com.mcdaale.capstone.matchmaker;

import com.mcdaale.capstone.matchmaker.object.Match;
import com.mcdaale.capstone.matchmaker.request.MatchResponseJson;
import com.mcdaale.capstone.matchmaker.request.MatchStatusJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MatchmakingService {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MatchRepository matcheRepo;
    List<Match> match;

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

    public long newUser(long userId, long gameId) {

        Match matchMade = null;
        long matchId = 0;

        for (Match match : matcheRepo.findAll()) {
            if (match.getGameId() == gameId) {
                match.addUser(userId);
                matchId = match.getMatchid();

                matchMade = match;
                matcheRepo.save(match);
                break;
            }
        }

        if (null == matchMade) {
            matchMade = new Match();
            //matchMade.setMatchid(1L);
            matchMade.setGameId(gameId);
            matchMade.addUser(userId);
            matcheRepo.save(matchMade);

            matchId = matchMade.getMatchid();
        }

        MatchResponseJson matchResponseJson = new MatchResponseJson(matchId, userId, System.currentTimeMillis());
        sendUserMessage(userId, matchResponseJson.toJsonString());

        checkMatchForConditions(matchMade);
        return matchId;
    }

    @Scheduled(fixedRate = 1000)
    private void runTaskWithDelay() {
        checkMatchesForConditions();
    }

    private void checkMatchForConditions(Match match) {
        MatchStatusJson.MatchStatus matchStatus = match.getMatchStatus();
        String matchStatusString;

        Log.d(TAG, "Checking match status for match %s %s", match.getMatchid(), match.getMatchStatus().toString());
        switch (matchStatus) {
            case MATCHED:

                Log.d(TAG, "Matched");
                break;
            case MATCHING:
                Log.d(TAG, "Matching....");
                break;
            case MATCH_FAILED:
                Log.d(TAG, "MatchFailed");
                break;
                default:
        }
        matchStatusString = new MatchStatusJson(match.getMatchid(), match.getLength(), match.getMinPlayers(),
                match.getMaxPlayers(), match.getGameId(), match.getTimeStart(), matchStatus.ordinal()).toJsonString();
        sendMatchBroadcast(match.getMatchid(), matchStatusString);
    }

    public void checkMatchesForConditions() {
        if (matcheRepo.findAll().isEmpty()) {
            Log.d(TAG, "No matches found");
        } else {
            for (Match match : matcheRepo.findAll()) {
                checkMatchForConditions(match);
            }

        }
    }

    public void sendUserMessage(long userId, String message) {
        messagingTemplate.convertAndSend("/topic/u" + userId,  message);
    }

    public void sendMatchBroadcast(long matchId, String message) {
        messagingTemplate.convertAndSend("/topic/m" + matchId, message);
    }
}
