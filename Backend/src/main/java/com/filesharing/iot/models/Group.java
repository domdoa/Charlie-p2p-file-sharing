package com.filesharing.iot.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "groups")
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(unique = true, nullable = false, name = "group_id")
    private long group_id;
    @Column(unique = true, name = "name", nullable = false)
    private String name;
    @Column(name = "invite")
    public String inviteString;

    @JsonIgnore
    @ManyToMany(mappedBy = "groups", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<User> users = new ArrayList<>();

    public void addUser(User user){
        users.add(user);
    }
}
