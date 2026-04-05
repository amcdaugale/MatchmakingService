package com.mcdaale.capstone.matchmaker;

import com.mcdaale.capstone.matchmaker.object.Match;

import java.util.List;

public interface MatchRepoService {
    Match saveMatch(Match match);
    List<Match> getMatchList();
    Match updateMatch(Match match);
    void deleteMatch(Match match);
}
