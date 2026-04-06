package com.mcdaale.capstone.matchmaker.object;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    //
    private Long gameId;
    private long matchingTimeout;
    private int minPlayers;
    private int maxPlayers;
}
