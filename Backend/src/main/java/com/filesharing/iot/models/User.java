package com.filesharing.iot.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Table(name = "app_user")
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(unique = true, nullable = false, name = "user_id")
    private Long user_id;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    List<Group> groups;

    //TODO: file indexes a
//    private List<File> fileIndexes;

}
