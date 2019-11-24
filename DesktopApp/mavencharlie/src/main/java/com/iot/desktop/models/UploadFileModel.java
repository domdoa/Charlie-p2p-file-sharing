package com.iot.desktop.models;

import java.util.Date;

public class UploadFileModel {
    private String fileName;
    private String size;
    private Date date;

    public UploadFileModel(){ }

    public UploadFileModel(String fileName, String size, Date date) {
        this.fileName = fileName;
        this.size = size;
        this.date = date;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
