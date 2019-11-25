package com.filesharing.iot.controller;

import com.filesharing.iot.Chord.Constants;
import com.filesharing.iot.models.File;
import com.filesharing.iot.models.ForeignPC;
import com.filesharing.iot.models.ListOfPeers;
import com.filesharing.iot.models.Peer;
import com.filesharing.iot.repository.FileRepository;
import com.filesharing.iot.repository.ForeignPcRepository;
import com.filesharing.iot.repository.PeerRepository;
import com.filesharing.iot.utils.Utils;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/peers")
public class PeerController {
    @Autowired
    PeerRepository peerRepository;
    @Autowired
    ForeignPcRepository foreignPcRepository;
    @Autowired
    RestTemplate restTemplate;

    List<Peer> peersList = new ArrayList<>();

    @PostMapping
    public ResponseEntity addPeer(@RequestBody Peer peer) {
        peerRepository.save(peer);
        peersList.add(peer);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/getAllPeersWithFile")
    public ListOfPeers getAllPeersWithFile(@RequestBody File fileToGet) {
        ListOfPeers listToReturnWithPeers = new ListOfPeers();
        List<Peer> peers = peerRepository.getPeers();
        for (Peer p : peers) {
            List<File> filesOfThePeer = p.getFileList();
            for(File f : filesOfThePeer){
                if(f.getMd5Sign().equals(fileToGet.getMd5Sign())) {
                    listToReturnWithPeers.getPeers().add(p);
                    break;
                }
            }

        }
        return listToReturnWithPeers;
    }

    @PostMapping("/getAllPeersWithAFileFromAllServers")
    public ListOfPeers getAllPeersWithAFileFromAllServers(@RequestBody File fileToGet) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<ForeignPC> foreignPCS = Utils.readFromFile(foreignPcRepository.getFileName());
        System.out.println(foreignPCS);
        ListOfPeers peersList = new ListOfPeers();
        for (ForeignPC foreignPC : foreignPCS) {
            String foreignPCAddress = foreignPC.getInetSocketAddress().getHostName();
            foreignPCAddress = foreignPCAddress.substring(1);
            if (!(foreignPCAddress.equals(Constants.localAddress) &&
                    foreignPC.getSpringPort().equals(Constants.currentSpringPort))) {
            ListOfPeers listOfPeersFromServer = restTemplate.postForObject("http://" + foreignPCAddress + ":" + foreignPC.getSpringPort() + "/peers/getAllPeersWithFile", fileToGet, ListOfPeers.class);
            peersList.getPeers().addAll(listOfPeersFromServer.getPeers());
            }
        }
        peersList.getPeers().addAll(this.getAllPeersWithFile(fileToGet).getPeers());
        return peersList;
    }

    @GetMapping("/files")
    public ResponseEntity<List<File>> getAllFileMetaDatas() {

        List<File> files = new ArrayList<>();
        peerRepository.getPeers().forEach(el -> files.addAll(el.getFileList()));
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @GetMapping("/file")
    public ResponseEntity<File> getAllFileMetadata(@RequestParam long file_id) {
        File file = new File();
        List<File> files = new ArrayList<>();

        peerRepository.getPeers().forEach(el -> files.addAll(el.getFileList()));
        for (File f : files) {
            if (f.getId() == file_id)
                file = f;
        }
        return new ResponseEntity<>(file, HttpStatus.OK);
    }


    // C U D files
    @PostMapping("/files")
    public ResponseEntity addFilesToPeer(@RequestBody List<File> files, @RequestParam long peer_id) {
        Peer peer = peerRepository.getPeers().stream()
                .filter(el -> el.getUser_id().equals(peer_id))
                .findFirst()
                .orElse(null);
        if (peer != null) {
            peer.addFiles(files);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/files")
    public ResponseEntity removeFileFromPeer(@RequestBody File file, @RequestParam long peer_id) {
        Peer peer = peerRepository.getPeers().stream()
                .filter(el -> el.getUser_id().equals(peer_id))
                .findFirst()
                .orElse(null);
        if (peer != null) {
            peer.removeFile(file);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/file")
    public ResponseEntity updateFileOfPeer(@RequestBody File file, @RequestBody String fileName, @RequestParam long peer_id) {
        Peer peer = peerRepository.getPeers().stream()
                .filter(el -> el.getUser_id().equals(peer_id))
                .findFirst()
                .orElse(null);
        if (peer != null) {
            peer.updateFile(fileName, file);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
