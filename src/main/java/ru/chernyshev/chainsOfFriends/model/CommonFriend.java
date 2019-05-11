package ru.chernyshev.chainsOfFriends.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommonFriend {
    @JsonProperty
    private int id;

    @JsonProperty
    private int[] common_friends;

    @JsonProperty
    private int common_count;

    public int getId() {
        return id;
    }

    public int[] getCommon_friends() {
        return common_friends;
    }

    public int getCommon_count() {
        return common_count;
    }
}
