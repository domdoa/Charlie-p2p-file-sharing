package com.filesharing.iot.models;

import java.util.ArrayList;
import java.util.List;

public class FilePeers {
    File fileMetadata;
    List<Peer> peerList = new ArrayList<>();

    public FilePeers(File fileMetadata, List<Peer> peerList) {
        this.fileMetadata = fileMetadata;
        this.peerList = peerList;
    }
}
