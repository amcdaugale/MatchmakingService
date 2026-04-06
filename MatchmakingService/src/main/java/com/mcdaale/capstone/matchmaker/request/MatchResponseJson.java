package com.mcdaale.capstone.matchmaker.request;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.mcdaale.capstone.matchmaker.Log;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchResponseJson {
    private static final String TAG = MatchResponseJson.class.getSimpleName();

    @SerializedName("matchId")
    private long matchId;
    @SerializedName("gameId")
    private long gameId;
    @SerializedName("startTime")
    private long startTime;

    public MatchResponseJson(long matchId, long gameId, long startTime) {
        /**
         * Id of match user was joined to.
         */
        this.matchId = matchId;
        /**
         * Id of the game getting matched for..
         */
        this.gameId = gameId;
        /**
         * Time the match was created.
         */
        this.startTime = startTime;
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
    public static MatchResponseJson fromJsonString(String jsonString) {
        MatchResponseJson matchResponseJson = null;
        Gson gson = new Gson();
        try {
            matchResponseJson = gson.fromJson(jsonString, MatchResponseJson.class);
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "Failed to convert json: %s", e.getMessage());
        }

        return matchResponseJson;
    }
}
