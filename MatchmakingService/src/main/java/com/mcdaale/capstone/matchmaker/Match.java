package com.mcdaale.capstone.matchmaker;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Match extends Game {
    @Id
    private Long id;
    private int minPlayers;
    private int maxPlayers;
    private long timeStart;

}
