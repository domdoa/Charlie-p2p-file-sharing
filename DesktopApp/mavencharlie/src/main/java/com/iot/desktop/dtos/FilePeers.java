package com.iot.desktop.dtos;

import com.iot.desktop.models.Peer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilePeers implements Serializable {
    File fileMetadata;
    List<Peer> peerList = new ArrayList<>();

}
