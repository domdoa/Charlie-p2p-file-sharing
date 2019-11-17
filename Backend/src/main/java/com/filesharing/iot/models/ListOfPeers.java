package com.filesharing.iot.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ListOfPeers {
    private List<Peer> peers = new ArrayList<>();
}
