package com.iot.desktop.dtos;

import com.iot.desktop.models.Group;

public class File {
    private String email;
    private String name;
    private String ext;
    private String md5Sign;
    private String size;
    private Group group;

    public File() {}

    public File(String email, String name, String ext, String md5Sign, String size, Group group) {
        this.email = email;
        this.name = name;
        this.ext = ext;
        this.md5Sign = md5Sign;
        this.size = size;
        this.group = group;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getMd5Sign() {
        return md5Sign;
    }

    public void setMd5Sign(String md5Sign) {
        this.md5Sign = md5Sign;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
