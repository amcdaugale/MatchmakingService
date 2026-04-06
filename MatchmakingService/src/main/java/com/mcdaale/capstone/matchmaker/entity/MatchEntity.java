package com.mcdaale.capstone.matchmaker.entity;

import com.mcdaale.capstone.matchmaker.object.Match;
import com.mcdaale.capstone.matchmaker.object.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.ArrayList;

/**
 * This is an entity to resemble a match Object to store in a DB.
 * Uses Lombock to auto generate getters and setters.
 */
@Entity
@Data
public class MatchEntity {
    /**
     * Match identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchid;
    /**
     * The game identifier.
     */
    private Long gameid;
    /**
     * The list of user id's in match.
     */
    private ArrayList<Long> userId;
    /**
     * The time the match was created.
     */
    private long timeStart;

    /**
     * Generate the Match object this data represents.
     * @return a match object reconstructed form data from this object.
     */
    public Match generateMatch() {
        ArrayList<User> users = new ArrayList<>();
        for (long userId : this.userId){
            users.add(new User(userId));
        }
        return new Match(matchid, users, timeStart);
    }
}
