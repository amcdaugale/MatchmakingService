package com.mcdaale.capstone.matchmaker.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchRequestJson {
    private String id;
    private String matchId;
    private String gameId;
    private long startTime;
}
