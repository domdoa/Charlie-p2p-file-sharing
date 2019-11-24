package com.filesharing.iot.controller;

import com.filesharing.iot.Chord.Constants;
import com.filesharing.iot.models.*;
import com.filesharing.iot.repository.ForeignPcRepository;
import com.filesharing.iot.repository.PeerRepository;
import com.filesharing.iot.repository.UserRepository;
import com.filesharing.iot.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    UserRepository userRepository;
    @Autowired
    RestTemplate restTemplate;

    @PostMapping
    public ResponseEntity addPeer(@RequestBody Peer peer) {
        peerRepository.save(peer);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity deletePeer(@RequestBody Peer peer) {
        peerRepository.remove(peer);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/getAllPeersWithFile")
    public ListOfPeers getAllPeersWithFile(@RequestBody File fileToGet) {

        ListOfPeers listToReturnWithPeers = new ListOfPeers();
        List<Peer> peers = peerRepository.getPeers();
        for (Peer p : peers) {
            List<File> filesOfThePeer = p.getFileList();
            for (File f : filesOfThePeer) {
                if (f.equals(fileToGet)) {
                    listToReturnWithPeers.getPeers().add(p);
                    break;
                }
            }

        }
        return listToReturnWithPeers;
    }

    @PostMapping("/getAllPeersWithAFileFromAllServers")
    public ListOfPeers getAllPeersWithAFileFromAllServers(@RequestBody File fileToGet) throws Exception {
        List<ForeignPC> foreignPCS = Utils.readFromFile(foreignPcRepository.getFileName());
        System.out.println(foreignPCS);
        ListOfPeers peersList = new ListOfPeers();
        for (ForeignPC foreignPC : foreignPCS) {
            String foreignPCAddress = foreignPC.getInetSocketAddress().getHostName();
            foreignPCAddress = foreignPCAddress.substring(1);

            if (!(foreignPCAddress.equals(Constants.localAddress) &&
                    foreignPC.getSpringPort().equals(Constants.currentSpringPort))) {
                ListOfPeers listOfPeersFromServer = restTemplate.postForObject("http://" + foreignPCAddress + ":" + foreignPC.getSpringPort() + "/peers/getAllPeersWithFile", fileToGet, ListOfPeers.class);
                if (listOfPeersFromServer != null)
                    peersList.getPeers().addAll(listOfPeersFromServer.getPeers());
            }
        }
        peersList.getPeers().addAll(this.getAllPeersWithFile(fileToGet).getPeers());
        return peersList;
    }

    @GetMapping("/files")
    public ResponseEntity<List<File>> getAllFileMetadatas(@RequestParam long user_id) {
        List<File> allFiles = new ArrayList<>();
        List<File> availableFiles = new ArrayList<>();

        User user = userRepository.findByUserId(user_id);
        if (user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        List<Group> groupList = user.getGroups();

        peerRepository.getPeers().forEach(el -> allFiles.addAll(el.getFileList()));

        for (File file : allFiles) {
            Group group = file.getGroup();

            if (group == null) availableFiles.add(file);
            else {
                Group james = groupList.stream()
                        .filter(p -> p.getName().equals(group.getName()))
                        .findAny()
                        .orElse(null);
                if (james != null) availableFiles.add(file);
            }
        }

        return new ResponseEntity<>(availableFiles, HttpStatus.OK);
    }

    @GetMapping("/file")
    public ResponseEntity<File> getFileMetadata(@RequestParam long file_id) {
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
