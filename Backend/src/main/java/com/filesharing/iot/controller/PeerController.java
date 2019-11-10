package com.filesharing.iot.controller;

import com.filesharing.iot.models.File;
import com.filesharing.iot.models.Peer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/peers")
public class PeerController {
    List<Peer> peersList = new ArrayList<>();

    @PostMapping
    public ResponseEntity addPeer(@RequestBody Peer peer){
        peersList.add(peer);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/files")
    public ResponseEntity<List<File>> getAllFileMetadatas() {

        List<File> files = new ArrayList<>();
        peersList.stream().forEach(el -> files.addAll(el.getFileList()));
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @GetMapping("/file")
    public ResponseEntity<File> getAllFileMetadata(@RequestParam long file_id) {
        File file = new File();
        List<File> files = new ArrayList<>();

        peersList.stream().forEach(el -> files.addAll(el.getFileList()));
        for (File f : files) {
            if (f.getId() == file_id)
                file = f;
        }
        return new ResponseEntity<>(file, HttpStatus.OK);
    }


    // C U D files
    @PostMapping("/files")
    public ResponseEntity addFilesToPeer(@RequestBody List<File> files, @RequestParam long peer_id) {
        Peer peer = peersList.stream()
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
        Peer peer = peersList.stream()
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
        Peer peer = peersList.stream()
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
