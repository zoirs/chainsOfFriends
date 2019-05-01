package ru.chernyshev.chainsOfFriends;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.vk.api.sdk.objects.friends.UserXtrLists;

import java.util.List;
import java.util.Objects;

/**
 * GetFieldsResponse object
 */
public class GetFieldsResponseOverride {
    /**
     * Total friends number
     */
    @SerializedName("count")
    private Integer count;

    @SerializedName("items")
    private List<UserX> items;

    public Integer getCount() {
        return count;
    }

    public GetFieldsResponseOverride setCount(Integer count) {
        this.count = count;
        return this;
    }

    public List<UserX > getItems() {
        return items;
    }

    public GetFieldsResponseOverride setItems(List<UserX > items) {
        this.items = items;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, items);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetFieldsResponseOverride getFieldsResponse = (GetFieldsResponseOverride) o;
        return Objects.equals(count, getFieldsResponse.count) &&
                Objects.equals(items, getFieldsResponse.items);
    }

    @Override
    public String toString() {
        final Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String toPrettyString() {
        final StringBuilder sb = new StringBuilder("GetFieldsResponse{");
        sb.append("count=").append(count);
        sb.append(", items=").append(items);
        sb.append('}');
        return sb.toString();
    }
}
