package com.iot.desktop.models;

import java.util.List;

public class User {

    private Long user_id;
    private String password;
    private String email;
    private List<Group> groups;

    public User() {}

    public User(Long user_id, String email, List<Group> groups) {
        this.user_id = user_id;
        this.email = email;
        this.groups = groups;
    }

    public User(Long user_id, String password, String email, List<Group> groups) {
        this.user_id = user_id;
        this.password = password;
        this.email = email;
        this.groups = groups;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
