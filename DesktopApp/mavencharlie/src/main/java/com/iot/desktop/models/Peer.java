package com.iot.desktop.models;

import com.iot.desktop.dtos.File;

import java.util.ArrayList;
import java.util.List;

public class Peer extends User{

    private String ipAddress;
    private int port;
    private List<FileMetadata> fileList = new ArrayList<>();

    public Peer() {}

    public Peer(String ipAddress, int port){
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<FileMetadata> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileMetadata> fileList) {
        this.fileList = fileList;
    }
}
