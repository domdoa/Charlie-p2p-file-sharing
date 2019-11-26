package com.iot.desktop.dtos;

import com.iot.desktop.models.FileMetadata;
import com.iot.desktop.models.Peer;

import java.util.List;

public class FilePeers {
    FileMetadata fileMetadata;
    List<Peer> peers;

    public  FilePeers() {}

    public FilePeers(FileMetadata fileMetadata, List<Peer> peers) {
        this.fileMetadata = fileMetadata;
        this.peers = peers;
    }

    public FileMetadata getFileMetadata() {
        return fileMetadata;
    }

    public void setFileMetadata(FileMetadata fileMetadata) {
        this.fileMetadata = fileMetadata;
    }

    public List<Peer> getPeers() {
        return peers;
    }

    public void setPeers(List<Peer> peers) {
        this.peers = peers;
    }
}
