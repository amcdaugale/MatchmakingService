package com.mcdaale.capstone.matchmaker.request;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.mcdaale.capstone.matchmaker.Log;
import lombok.Getter;
import lombok.Setter;

/**
 * Matching request from client.
 * TODO: Authenticate users, make sure they are who they say they are...
 */
@Getter
@Setter
public class MatchRequestJson {
    private static final String TAG = MatchRequestJson.class.getSimpleName();

    /**
     * Id of user creating match.
     */
    @SerializedName("userId")
    private long userId;
    /**
     * Id of gam to match for.
     */
    @SerializedName("gameId")
    private long gameId;

    /**
     * Constructor.
     * @param userId Id of user creating match.
     * @param gameId Id of gam to match for
     */
    public MatchRequestJson(long userId, long gameId) {
        this.userId = userId;
        this.gameId = gameId;
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
    public static MatchRequestJson fromJsonString(String jsonString) {
        MatchRequestJson matchRequestJson = null;
        Gson gson = new Gson();
        try {
            matchRequestJson = gson.fromJson(jsonString, MatchRequestJson.class);
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "Failed to convert json: %s", e.getMessage());
        }

        return matchRequestJson;
    }
}
