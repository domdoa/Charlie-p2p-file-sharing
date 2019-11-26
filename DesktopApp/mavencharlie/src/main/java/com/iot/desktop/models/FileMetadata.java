package com.iot.desktop.models;

public class FileMetadata {
    private String email;//owner, uploader
    private String groupId; // if null, then the file is public
    private String fileName;
    private String extension;
    private long size;
    private String MD5Signature;

    public FileMetadata() {
    }

    public FileMetadata(String email, String groupId, String fileName, String extension, long size, String MD5Signature) {
        this.email = email;
        this.groupId = groupId;
        this.fileName = fileName;
        this.extension = extension;
        this.size = size;
        this.MD5Signature = MD5Signature;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getMD5Signature() {
        return MD5Signature;
    }

    public void setMD5Signature(String MD5Signature) {
        this.MD5Signature = MD5Signature;
    }
}
