package com.iot.desktop.models;

public class FileMetadata {
    private String email;
    private String name;
    private String ext;
    private String md5Sign;
    private long size;
    private Group group;

    public FileMetadata() {
    }

    public FileMetadata(String email, String name, String ext, String md5Sign, long size, Group group) {
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

    public String getFileName() {
        return name;
    }

    public void setFileName(String fileName) {
        this.name = fileName;
    }

    public String getExtension() {
        return ext;
    }

    public void setExtension(String extension) {
        this.ext = extension;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getMD5Signature() {
        return md5Sign;
    }

    public void setMD5Signature(String MD5Signature) {
        this.md5Sign = MD5Signature;
    }
}
