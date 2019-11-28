package com.iot.desktop.models;

import com.iot.desktop.dtos.File;

import java.util.ArrayList;
import java.util.List;

public class Peer extends User{

    private String ipAddress;
    private int port;
    private List<File> fileList = new ArrayList<>();
    private int springPort;

    public Peer() {}

    public Peer(String ipAddress, int port){
        this.ipAddress = ipAddress;
        this.port = port;
    }
    public Peer(String ipAddress, int port, int springPort){
        this.ipAddress = ipAddress;
        this.port = port;
        this.springPort = springPort;
    }

    public int getSpringPort() {
        return springPort;
    }

    public void setSpringPort(int springPort) {
        this.springPort = springPort;
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

    public List<File> getFileList() {
        return fileList;
    }

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }
}
