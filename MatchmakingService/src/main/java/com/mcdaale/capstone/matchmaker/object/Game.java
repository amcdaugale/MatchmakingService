package com.mcdaale.capstone.matchmaker.object;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Object to represent a game.
 * TODO: Rework this to be prepopulated to DB.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    /**
     * Id of this game.
     */
    private Long gameId;
    /**
     * Use a TimeUnit duration in ms for when the matching should timeout.
     */
    private long matchingTimeout;
    /**
     * Minimum players in the match to create a match.
     */
    private int minPlayers;
    /**
     * Maximum number of player allowed in game matching.
     */
    private int maxPlayers;
}
