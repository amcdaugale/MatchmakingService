package com.mcdaale.capstone.matchmaker;

import com.mcdaale.capstone.matchmaker.entity.MatchEntity;
import com.mcdaale.capstone.matchmaker.object.Match;
import com.mcdaale.capstone.matchmaker.object.User;
import com.mcdaale.capstone.matchmaker.request.MatchResponseJson;
import com.mcdaale.capstone.matchmaker.request.MatchStatusJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class MatchmakingService {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MatchRepository matcheRepo;

    MatchRepoService matchRepoService = new MatchRepoService() {
        @Override
        public Match saveMatch(Match match) {
            MatchEntity matchEntity = new MatchEntity();
            ArrayList<Long> userId = new ArrayList<>();
            for (User user : match.getUsers()) {
                userId.add(user.getId());
            }
            matchEntity.setGameid(match.getGameId());
            matchEntity.setMatchid(match.getMatchid());
            matchEntity.setUserId(userId);
            matchEntity.setTimeStart(match.getTimeStart());
            matcheRepo.save(matchEntity);
            return null;
        }

        @Override
        public List<Match> getMatchList() {
            ArrayList<Match> match = new ArrayList<>();

            for (MatchEntity matchEntity : matcheRepo.findAll()) {
                match.add(matchEntity.generateMatch());
            }

            return match.stream().toList();
        }

        @Override
        public Match updateMatch(Match match) {
            MatchEntity matchEntity = new MatchEntity();
            ArrayList<Long> userId = new ArrayList<>();
            for (User user : match.getUsers()) {
                userId.add(user.getId());
            }
            matchEntity.setGameid(match.getGameId());
            matchEntity.setMatchid(match.getMatchid());
            matchEntity.setUserId(userId);
            matchEntity.setTimeStart(match.getTimeStart());
            matcheRepo.save(matchEntity);
            return null;
        }

        @Override
        public void deleteMatch(Match match) {
            matcheRepo.deleteById(match.getMatchid());
        }
    };

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

        for (Match match : matchRepoService.getMatchList()) {
            if (match.getGameId() == gameId) {
                match.addUser(userId);
                matchId = match.getMatchid();

                matchMade = match;
                matchRepoService.saveMatch(match);
                break;
            }
        }

        if (null == matchMade) {
            matchMade = new Match();
            //matchMade.setMatchid(1L);
            matchMade.setGameId(gameId);
            matchMade.addUser(userId);
            matchRepoService.saveMatch(matchMade);

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
            for (Match match : matchRepoService.getMatchList()) {
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
