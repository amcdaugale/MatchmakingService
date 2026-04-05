package com.mcdaale.capstone.matchmaker.request;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.mcdaale.capstone.matchmaker.Log;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchStatusJson {
    private static final String TAG = MatchStatusJson.class.getSimpleName();

    public enum MatchStatus {
        MATCHING,
        MATCHED,
        MATCH_FAILED
    }

    @SerializedName("matchId")
    private long matchId;
    @SerializedName("playerCount")
    private int playerCount;
    @SerializedName("minPlayerCount")
    private int minPlayerCount;
    @SerializedName("maxPlayerCount")
    private int maxPlayerCount;
    @SerializedName("gameId")
    private long gameId;
    @SerializedName("startTime")
    private long startTime;
    @SerializedName("matchStatus")
    private int matchStatus;

    public MatchStatusJson(long matchId, int playerCount, int minPlayerCount, int maxPlayerCount, long gameId, long startTime, int matchStatus) {
        this.matchId = matchId;
        this.playerCount = playerCount;
        this.minPlayerCount = minPlayerCount;
        this.maxPlayerCount = maxPlayerCount;
        this.gameId = gameId;
        this.startTime = startTime;
        this.matchStatus = matchStatus;
    }

    public String toJsonString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static MatchStatusJson fromJsonString(String jsonString) {
        MatchStatusJson matchRequestJson = null;
        Gson gson = new Gson();
        try {
            matchRequestJson = gson.fromJson(jsonString, MatchStatusJson.class);
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "Failed to convert json: %s", e.getMessage());
        }

        return matchRequestJson;
    }
}
