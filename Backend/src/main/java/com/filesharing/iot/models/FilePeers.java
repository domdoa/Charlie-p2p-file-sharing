package com.filesharing.iot.models;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class FilePeers implements Serializable {
    File fileMetadata;
    List<Peer> peerList = new ArrayList<>();

    public FilePeers(File fileMetadata, List<Peer> peerList) {
        this.fileMetadata = fileMetadata;
        this.peerList = peerList;
    }
}
