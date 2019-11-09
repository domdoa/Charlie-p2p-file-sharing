package com.filesharing.iot.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class File {
    private String id;
    private String name;
    private String ext;
    private String md5Sign;
    private String size;
    private User user;
}
