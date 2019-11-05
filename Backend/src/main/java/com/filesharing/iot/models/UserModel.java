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
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(unique = true, nullable = false, name = "user_id")
    private long user_id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    //TODO: finish models
//    private List<File> fileIndexes;

}
