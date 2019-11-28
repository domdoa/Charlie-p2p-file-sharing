package com.filesharing.iot.repository;

import com.filesharing.iot.models.Peer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PeerRepository {
    private List<Peer> peers = new ArrayList<>();

    public void save(Peer peer){
        Peer james = peers.stream()
                .filter(p -> p.getUser_id().longValue() == peer.getUser_id().longValue())
                .findAny()
                .orElse(null);
        if(james == null) {
            peers.add(peer);
        }
    }

    public List<Peer> getPeers(){
        return peers;
    }

    public void remove(Peer peer){
        peers = peers.stream()
                .filter(p -> p.getUser_id().longValue() != peer.getUser_id().longValue())
                .collect(Collectors.toList());
    }

    public Peer findByEmail(String email){
        return peers.stream()
                .filter(p -> p.getEmail().equals(email))
                .findAny()
                .orElse(null);
    }
}