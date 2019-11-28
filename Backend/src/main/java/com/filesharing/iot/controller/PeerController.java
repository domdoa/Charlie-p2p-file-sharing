package com.filesharing.iot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filesharing.iot.utils.Constants;
import com.filesharing.iot.models.*;
import com.filesharing.iot.repository.ForeignPcRepository;
import com.filesharing.iot.repository.PeerRepository;
import com.filesharing.iot.repository.UserRepository;
import com.filesharing.iot.utils.Utils;
import com.google.gson.Gson;
import okhttp3.ResponseBody;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/peers")
public class PeerController {
    private static final Logger LOGGER = Logger.getLogger(PeerController.class.getName());

    private Instant getCurrentUTC() {
        Instant instant = Instant.now();
        return instant;
    }

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
        LOGGER.log(Level.INFO, getCurrentUTC() + " Creating new peer", peer);
        if ( peerRepository.findByEmail(peer.getEmail()) == null ) {
            peerRepository.save(peer);
            LOGGER.log(Level.INFO, getCurrentUTC() + " New peer successfully added", peer);
            return ResponseEntity.ok().build();
        }
        LOGGER.log(Level.WARNING, getCurrentUTC() + " Failed to add peer. Peer with this email: " + peer.getEmail() + " already exist", peer);
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping
    public ResponseEntity deletePeer(@RequestBody Peer peer) {
        LOGGER.log(Level.INFO, getCurrentUTC() + " Deleting peer", peer);
        peerRepository.remove(peer);
        LOGGER.log(Level.INFO, getCurrentUTC() + " Peer deleted successfully", peer);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/getAllPeersWithFile")
    public ListOfPeers getAllPeersWithFile(@RequestBody File fileToGet) {
        LOGGER.log(Level.INFO, getCurrentUTC() + " Getting all peers with file");
        ListOfPeers listToReturnWithPeers = new ListOfPeers();
        List<Peer> peers = peerRepository.getPeers();
        for (Peer p : peers) {
            List<File> filesOfThePeer = p.getFileList();
            for (File f : filesOfThePeer) {
                if ( f.equals(fileToGet) ) {
                    listToReturnWithPeers.getPeers().add(p);
                    break;
                }
            }

        }
        return listToReturnWithPeers;
    }

    @PostMapping("/getAllPeersWithAFileFromAllServers")
    public ResponseEntity getAllPeersWithAFileFromAllServers(HttpServletRequest httpRequest, @RequestBody File fileToGet, @RequestParam String email) throws Exception {
        LOGGER.log(Level.INFO, getCurrentUTC() + " Getting all peers with file from all servers");
        String authorization = httpRequest.getHeader("Authorization");
        String contentType = httpRequest.getHeader("Content-Type");

        List<ForeignPC> foreignPCS = Utils.readFromFile(foreignPcRepository.getFileName());
        System.out.println(foreignPCS);
        ListOfPeers peersList = new ListOfPeers();
        LOGGER.log(Level.INFO, getCurrentUTC() + " Looping through all servers...");
        for (ForeignPC foreignPC : foreignPCS) {
            String foreignPCAddress = foreignPC.getInetSocketAddress().getHostName();
            foreignPCAddress = foreignPCAddress.substring(1);
            if ( !(foreignPCAddress.equals(Constants.localAddress) &&
                    foreignPC.getSpringPort().equals(Constants.currentSpringPort)) ) {
                ObjectMapper mapper = new ObjectMapper();


                String URL = "http://" + foreignPCAddress + ":" + foreignPC.getSpringPort() + "/peers/getAllPeersWithFile";
                try {
                    Request request = new Request.Builder()
                            .url(URL)
                            .addHeader("Authorization", authorization)
                            .addHeader("Content-Type", contentType)
                            .post(okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mapper.writeValueAsString(fileToGet)))
                            .build();
                    OkHttpClient client = new OkHttpClient();
                    Call call = client.newCall(request);
                    Response response = call.execute();
                    Gson gson = new Gson();
                    ResponseBody responseBody = response.body();
                    ListOfPeers listOfPeersFromServer = gson.fromJson(responseBody.string(), ListOfPeers.class);

                    if ( listOfPeersFromServer != null ) {
                        LOGGER.log(Level.INFO, getCurrentUTC() + " Added list of peers from server " + foreignPC.getSpringPort());
                        peersList.getPeers().addAll(listOfPeersFromServer.getPeers());
                    }
                } catch ( Exception e ) {
                    e.printStackTrace();
                }

            }
        }
        peersList.getPeers().addAll(this.getAllPeersWithFile(fileToGet).getPeers());

        // we have all the information about the peers
        Peer peer = peerRepository.findByEmail(email);
        if ( peer == null ) {
            LOGGER.log(Level.WARNING, getCurrentUTC() + " Failed to find peer with this email: ");
            return ResponseEntity.notFound().build(); // the requested peer does not exist
        }
        FilePeers filePeers = new FilePeers(fileToGet, peersList.getPeers());

        try {
            String URL = "http://" + peer.getIpAddress() + ":" + peer.getSpringPort();
            //restTemplate.postForObject(URL, filePeers, ResponseEntity.class);
            ObjectMapper mapper = new ObjectMapper();
            Request request = new Request.Builder()
                    .url(URL)
                    .addHeader("Authorization", authorization)
                    .addHeader("Content-Type", contentType)
                    .post(okhttp3.RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mapper.writeValueAsString(filePeers)))
                    .build();
            OkHttpClient client = new OkHttpClient();
            Call call = client.newCall(request);
            Response response = call.execute();
            if(response.code()!=200){
                System.err.println("Peer didn't download the file");
            }
        } catch ( Exception e ) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

        LOGGER.log(Level.INFO, getCurrentUTC() + " Success");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/findPeerByEmail")
    public ResponseEntity<Peer> findPeerByEmail(@RequestParam String email) {
        LOGGER.log(Level.INFO, getCurrentUTC() + " Finding peer by email");
        List<Peer> peers = peerRepository.getPeers();

        for (Peer peer : peers) {
            if ( peer.getEmail().equals(email) ) {
                LOGGER.log(Level.INFO, getCurrentUTC() + " Peer found: " + email);
                return new ResponseEntity<>(peer, HttpStatus.OK);
            }
        }
        LOGGER.log(Level.WARNING, getCurrentUTC() + " Peer is not online");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/files")
    public ResponseEntity<List<File>> getAllFileMetadatas(@RequestParam String email) {
        LOGGER.log(Level.INFO, getCurrentUTC() + " Getting all files");
        List<File> allFiles = new ArrayList<>();
        List<File> availableFiles = new ArrayList<>();

        User user = userRepository.findByEmail(email);
        if ( user == null ) {
            LOGGER.log(Level.WARNING, getCurrentUTC() + " User not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Group> groupList = user.getGroups();

        peerRepository.getPeers().forEach(el -> allFiles.addAll(el.getFileList()));

        for (File file : allFiles) {
            Group group = file.getGroup();

            if ( group == null ) availableFiles.add(file);
            else {
                Group james = groupList.stream()
                        .filter(p -> p.getName().equals(group.getName()))
                        .findAny()
                        .orElse(null);
                if ( james != null ) availableFiles.add(file);
            }
        }
        LOGGER.log(Level.INFO, getCurrentUTC() + " All available files returned");
        return new ResponseEntity<>(availableFiles, HttpStatus.OK);
    }

    @GetMapping("/file")
    public ResponseEntity<File> getFileMetadata(@RequestParam String md5) {
        LOGGER.log(Level.INFO, getCurrentUTC() + " Getting file");
        File file = new File();
        List<File> files = new ArrayList<>();

        peerRepository.getPeers().forEach(el -> files.addAll(el.getFileList()));
        for (File f : files) {
            if ( f.getMd5Sign().equals(md5) )
                file = f;
        }
        LOGGER.log(Level.INFO, getCurrentUTC() + " File retrieved");
        return new ResponseEntity<>(file, HttpStatus.OK);
    }


    // C U D files
    @PostMapping("/files")
    public ResponseEntity addFilesToPeer(@RequestBody File file, @RequestParam String email) {
        LOGGER.log(Level.INFO, getCurrentUTC() + " Adding files to peer");
        Peer peer = peerRepository.getPeers().stream()
                .filter(el -> el.getEmail().equals(email))
                .findFirst()
                .orElse(null);
        if ( peer != null ) {
            File f = peer.getFileList().stream()
                    .filter(el -> el.equals(file))
                    .findFirst()
                    .orElse(null);
            if ( f != null ) {
                LOGGER.log(Level.WARNING, getCurrentUTC() + " File already added");
                return ResponseEntity.badRequest().build();
            }
            peer.addFile(file);
            LOGGER.log(Level.INFO, getCurrentUTC() + " File added to the peer: " + email);
            return ResponseEntity.ok().build();
        } else {
            LOGGER.log(Level.WARNING, getCurrentUTC() + " Peer not found");
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/files")
    public ResponseEntity removeFileFromPeer(@RequestBody File file, @RequestParam String email) {
        LOGGER.log(Level.INFO, getCurrentUTC() + " Deleting file from peer");
        Peer peer = peerRepository.getPeers().stream()
                .filter(el -> el.getEmail().equals(email))
                .findFirst()
                .orElse(null);
        if ( peer != null ) {
            peer.removeFile(file);
            LOGGER.log(Level.INFO, getCurrentUTC() + " File deleted: " + file.getName());
            return ResponseEntity.ok().build();
        } else {
            LOGGER.log(Level.WARNING, getCurrentUTC() + " Peer not found");
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/file")
    public ResponseEntity updateFileOfPeer(@RequestBody File file, @RequestBody String fileName, @RequestParam String email) {
        LOGGER.log(Level.INFO, getCurrentUTC() + " Updating peer file");
        Peer peer = peerRepository.getPeers().stream()
                .filter(el -> el.getEmail().equals(email))
                .findFirst()
                .orElse(null);
        if ( peer != null ) {
            peer.updateFile(fileName, file);
            LOGGER.log(Level.INFO, getCurrentUTC() + " File updated: " + fileName);
            return ResponseEntity.ok().build();
        } else {
            LOGGER.log(Level.WARNING, getCurrentUTC() + " Peer not found");
            return ResponseEntity.notFound().build();
        }
    }

}
