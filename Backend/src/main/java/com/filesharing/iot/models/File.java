package com.filesharing.iot.models;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class File {
    private String email;
    private String name;
    private String ext;
    private String md5Sign;
    private long size;
    private Group group;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return size == file.size &&
                Objects.equals(name, file.name) &&
                Objects.equals(ext, file.ext) &&
                Objects.equals(md5Sign, file.md5Sign) &&
                Objects.equals(group, file.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name, ext, md5Sign, size);
    }


}
