package models;

import java.util.List;

public class User {

    private String id;
    private String email;
    private List<Group> groups;

    public User() {}

    public User(String id, String email, List<Group> groups) {
        this.id = id;
        this.email = email;
        this.groups = groups;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
