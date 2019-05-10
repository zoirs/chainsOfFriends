package ru.chernyshev.chainsOfFriends.model;

import com.vk.api.sdk.objects.users.UserXtrCounters;

public class User {
    private final Integer id;
    private final String name;
    private final String lastName;
    private final String photo;

    public User(UserXtrCounters user) {
        this.id = user.getId();
        this.name = user.getFirstName();
        this.lastName = user.getLastName();
        this.photo = user.getPhoto200Orig().toString();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoto() {
        return photo;
    }
}
