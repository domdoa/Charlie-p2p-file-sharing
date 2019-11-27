package com.iot.desktop.models;

import java.util.List;

public class Group {

    private String id;
    private String name;
    private List<User> users;
    private String inviteString;

    public Group() {}

    public Group(String name, String inviteString) {
        this.name = name;
        this.inviteString = inviteString;
    }

    public Group(String id, String name, List<User> users) {
        this.id = id;
        this.name = name;
        this.users = users;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getInviteString() {
        return inviteString;
    }

    public void setInviteString(String inviteString) {
        this.inviteString = inviteString;
    }
}
