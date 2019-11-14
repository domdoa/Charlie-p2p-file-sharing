package com.filesharing.iot.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class File {
    private long id;
    private long user_id;
    private String name;
    private String extension;
    private String md5Sign;
    private Integer size;
}
