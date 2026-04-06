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

/**
 * This is the match making service. In charge of matching users with other users who just want to open up a socket
 * together, potentially to play a game together.
 */
@Service
public class MatchmakingService {
    /**
     * Object to send messages to user.
     */
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * The repository for persistence.
     * TODO: finalize db connections.
     */
    @Autowired
    private MatchRepository matcheRepo;

    /**
     * Implementation of repo service interface.
     */
    MatchRepoService matchRepoService = new MatchRepoService() {
        @Override
        public void saveMatch(Match match) {
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
        public void updateMatch(Match match) {
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
        }

        @Override
        public void deleteMatch(Match match) {
            matcheRepo.deleteById(match.getMatchid());
        }
    };

    private static String TAG = MatchmakingService.class.getSimpleName();

    /**
     * Init this service.
     * @param messagingTemplate Set the messaging template to talk to clients form service.
     */
    @Autowired
    public MatchmakingService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Match a new user requesting to get matched.
     * @param userId Userid of the user joining.
     * @param gameId Id of the game to be matched for.
     * @return id of the match user was added to.
     */
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

    /**
     * Run checks for matching on an interval of 1 seccond. We essentially want to create a heart beat.
     */
    @Scheduled(fixedRate = 1000)
    private void runTaskWithDelay() {
        checkMatchesForConditions();
    }

    /**
     * Check if match conditions have been met.
     * @param match Match to check if the criteria was met.
     */
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

    /**
     * Check all matches for weather or not a match has been created.
     */
    public void checkMatchesForConditions() {
        if (matcheRepo.findAll().isEmpty()) {
            Log.d(TAG, "No matches found");
        } else {
            for (Match match : matchRepoService.getMatchList()) {
                checkMatchForConditions(match);
            }

        }
    }

    /**
     * Send the user a message.
     * @param userId User to message.
     * @param message String to send.
     */
    public void sendUserMessage(long userId, String message) {
        messagingTemplate.convertAndSend("/topic/u" + userId,  message);
    }

    /**
     * Match to send message to.
     * @param matchId id of matching to message.
     * @param message String to send.
     */
    public void sendMatchBroadcast(long matchId, String message) {
        messagingTemplate.convertAndSend("/topic/m" + matchId, message);
    }
}
