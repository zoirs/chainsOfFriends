package ru.chernyshev.chainsOfFriends;

import com.google.gson.annotations.SerializedName;
import com.vk.api.sdk.objects.friends.UserXtrLists;
import com.vk.api.sdk.objects.users.UserFull;

public class UserX extends UserXtrLists {

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public String getCanAccessClosed() {
        return canAccessClosed;
    }

    public void setCanAccessClosed(String canAccessClosed) {
        this.canAccessClosed = canAccessClosed;
    }

    @SerializedName("is_closed")
    private Boolean closed;

    @SerializedName("can_access_closed")
    private String canAccessClosed;

//    public String getNickname() {
//        return isClosed;
//    }
//
//    public UserFull setNickname(String isClosed) {
//        this.isClosed = isClosed;
//        return this;
//    }

}
