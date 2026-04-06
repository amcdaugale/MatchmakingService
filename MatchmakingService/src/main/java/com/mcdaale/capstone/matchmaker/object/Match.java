package com.mcdaale.capstone.matchmaker.object;

import com.mcdaale.capstone.matchmaker.request.MatchStatusJson;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@Builder
public class Match extends Game {
    private static final long MATCHING_DEFAULT_TIMEOUT_MS = TimeUnit.MINUTES.toMillis(5);

    private Long matchid;
    private ArrayList<User> users;
    private long timeStart;

    public Match(Long matchid, ArrayList<User> users, long timeStart) {
        this();
        this.matchid = matchid;
        this.users = users;
    }

    public Match() {
        super();
        this.setTimeStart(System.currentTimeMillis());
        super.setMatchingTimeout(MATCHING_DEFAULT_TIMEOUT_MS);
        users = new ArrayList<>();
        super.setMaxPlayers(4);
        super.setMinPlayers(2);
    }

    public void addUser(long userId) {
        User user = new User(userId);
        users.add(user);
    }

    public int getLength() {
        return users.size();
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - timeStart;
    }

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
