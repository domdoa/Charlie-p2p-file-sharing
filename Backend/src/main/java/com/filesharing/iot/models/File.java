package com.filesharing.iot.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class File {
    private String fileId;
    private String fileName;
    private String filePath;
    private String fileSize;

}
