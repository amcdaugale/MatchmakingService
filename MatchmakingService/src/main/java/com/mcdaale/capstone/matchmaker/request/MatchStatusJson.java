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

    /**
     * Status of the matching.
     */
    public enum MatchStatus {
        MATCHING,
        MATCHED,
        MATCH_FAILED
    }

    /**
     * The id of the matching.
     */
    @SerializedName("matchId")
    private long matchId;
    /**
     * The count of players in match.
     */
    @SerializedName("playerCount")
    private int playerCount;

    /**
     * The min players to make match.
     */
    @SerializedName("minPlayerCount")
    private int minPlayerCount;

    /**
     * The maximum number of players allowed in the match.
     */
    @SerializedName("maxPlayerCount")
    private int maxPlayerCount;
    /**
     * The id of the game object.
     */
    @SerializedName("gameId")
    private long gameId;
    /**
     * The time the match was initiated.
     */
    @SerializedName("startTime")
    private long startTime;
    /**
     * Current status of the match.
     */
    @SerializedName("matchStatus")
    private int matchStatus;

    /**
     * A constuctor to set all values.
     * @param matchId The id of the matching.
     * @param playerCount The min players to make match.
     * @param minPlayerCount The min players to make match.
     * @param maxPlayerCount The maximum number of players allowed in the match.
     * @param gameId The id of the game object.
     * @param startTime The time the match was initiated.
     * @param matchStatus Current status of the match.
     */
    public MatchStatusJson(long matchId, int playerCount, int minPlayerCount, int maxPlayerCount, long gameId, long startTime, int matchStatus) {
        this.matchId = matchId;
        this.playerCount = playerCount;
        this.minPlayerCount = minPlayerCount;
        this.maxPlayerCount = maxPlayerCount;
        this.gameId = gameId;
        this.startTime = startTime;
        this.matchStatus = matchStatus;
    }

    /**
     * Convert this object to a json string.
     * @return A string representing a serialised version of this object.
     */
    public String toJsonString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /**
     * Convert a json sting to this object.
     * @param jsonString String json version of this class.
     * @return this object, else null.
     */
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
