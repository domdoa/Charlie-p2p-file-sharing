package com.filesharing.iot.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
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
    @Column(name = "password", nullable = false)
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "groups", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<User> users;
}
