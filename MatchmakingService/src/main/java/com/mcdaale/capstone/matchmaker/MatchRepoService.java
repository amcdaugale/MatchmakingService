package com.mcdaale.capstone.matchmaker;

import com.mcdaale.capstone.matchmaker.object.Match;

import java.util.List;

/**
 * Interface to prototype methods to call to access/ store/ amd convert the entity to one of our data types.
 */
public interface MatchRepoService {
    /**
     * Save match to repository.
     * @param match the match object to save to persistence.
     */
    void saveMatch(Match match);

    /**
     * Get a list of matches form persistent data.
     * @return list of matchings.
     */
    List<Match> getMatchList();

    /**
     * Update a match entry in repository.
     * @param match the match object to update in persistence.
     */
    void updateMatch(Match match);

    /**
     * Remove a match object form persistence.
     * @param match the match object to update in persistence.
     */
    void deleteMatch(Match match);
}
