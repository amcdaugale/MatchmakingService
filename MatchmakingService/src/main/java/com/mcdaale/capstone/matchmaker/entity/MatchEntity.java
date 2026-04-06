package com.mcdaale.capstone.matchmaker.entity;

import com.mcdaale.capstone.matchmaker.object.Match;
import com.mcdaale.capstone.matchmaker.object.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.ArrayList;

@Entity
@Data
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchid;
    private Long gameid;
    private ArrayList<Long> userId;
    private long timeStart;

    public Match generateMatch() {
        ArrayList<User> users = new ArrayList<>();
        for (long userId : this.userId){
            users.add(new User(userId));
        }
        return new Match(matchid, users, timeStart);
    }
}
