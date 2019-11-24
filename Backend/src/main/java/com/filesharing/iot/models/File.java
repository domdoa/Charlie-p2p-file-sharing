package com.filesharing.iot.models;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class File {
    private long id;
    private long user_id;
    private String name;
    private String ext;
    private String md5Sign;
    private String size;
    private Group group;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return  Objects.equals(name, file.name) &&
                Objects.equals(ext, file.ext) &&
                Objects.equals(md5Sign, file.md5Sign) &&
                Objects.equals(size, file.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user_id, name, ext, md5Sign, size);
    }


}
