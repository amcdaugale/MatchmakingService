package com.mcdaale.capstone.matchmaker.object;

import com.mcdaale.capstone.matchmaker.request.MatchStatusJson;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Object to represent a match.
 */
@Getter
@Setter
@Builder
public class Match extends Game {
    /**
     * The default timeout for a matching.
     */
    private static final long MATCHING_DEFAULT_TIMEOUT_MS = TimeUnit.MINUTES.toMillis(5);

    /**
     * The id of the match.
     * @implNote clients should subscribe to for /topic/m(matchid) for match staus.
     */
    private Long matchid;
    /**
     * A list of users in the match.
     */
    private ArrayList<User> users;
    /**
     * The time Match was first created.
     */
    private long timeStart;

    /**
     * Constructor to use the match id and user id to construct this object.
     * @param matchid The id of thid match.
     * @param users the users who have joined this match.
     * @param timeStart The time the match was created.
     */
    public Match(Long matchid, ArrayList<User> users, long timeStart) {
        this();
        this.matchid = matchid;
        this.users = users;
    }

    /**
     * Empty constructor to initialize values.
     */
    public Match() {
        super();
        this.setTimeStart(System.currentTimeMillis());
        super.setMatchingTimeout(MATCHING_DEFAULT_TIMEOUT_MS);
        users = new ArrayList<>();
        super.setMaxPlayers(4);
        super.setMinPlayers(2);
    }

    /**
     * Add a user to the Match.
     * TODO: Make automic reference.
     * @param userId The id of the user to add to match.
     */
    public void addUser(long userId) {
        User user = new User(userId);
        users.add(user);
    }

    /**
     * Return the length of the user array. This can be used to tell haw many users have joined the match.
     * @return number of players who have joined the match.
     */
    public int getLength() {
        return users.size();
    }

    /**
     * Calculate the time that has passed since the match was created.
     * @return The time that has passed since the match was created.
     */
    public long getElapsedTime() {
        return System.currentTimeMillis() - timeStart;
    }

    /**
     * Return the current matching status of this match.
     * @return One of the {@link MatchStatusJson.MatchStatus} values.
     */
    public MatchStatusJson.MatchStatus getMatchStatus() {
        MatchStatusJson.MatchStatus status = MatchStatusJson.MatchStatus.MATCHING;

        if (getLength() == getMaxPlayers()) {
            // match made
            status = MatchStatusJson.MatchStatus.MATCHED;
        } else if (getElapsedTime() >= getMatchingTimeout()) {
            if (getLength() >= getMinPlayers()) {
                // match made
                status = MatchStatusJson.MatchStatus.MATCHED;
            } else {
                // match failed
                status = MatchStatusJson.MatchStatus.MATCH_FAILED;
            }
        }
        return status;
    }
}
