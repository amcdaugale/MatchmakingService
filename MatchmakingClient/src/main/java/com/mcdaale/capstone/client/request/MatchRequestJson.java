package com.mcdaale.capstone.client.request;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.mcdaale.capstone.client.Log;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchRequestJson {
    private static final String TAG = MatchRequestJson.class.getSimpleName();

    @SerializedName("userId")
    private long userId;
    @SerializedName("gameId")
    private long gameId;

    public MatchRequestJson(long userId, long gameId) {
        this.userId = userId;
        this.gameId = gameId;
    }

    public String toJsonString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

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
