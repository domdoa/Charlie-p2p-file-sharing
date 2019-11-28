package com.filesharing.iot.controller;

import com.filesharing.iot.models.*;
import com.filesharing.iot.repository.PeerRepository;
import com.filesharing.iot.repository.UserRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WebSocketController {
    @Autowired
    PeerRepository peerRepository;
    @Autowired
    UserRepository userRepository;
    List<User> userList = new ArrayList<>();


    @MessageMapping("/getFiles")
    @SendTo("/topic/files")
    public Message getFiles(@Payload String email) {
        Message m = new Message();
        List<Peer> peers = peerRepository.getPeers();
        List<File> allFiles = new ArrayList<>();
        List<File> availableFiles = new ArrayList<>();
        User user = userList.stream().filter(el -> el.getEmail().equals(email)).findFirst().orElse(null);
        if ( user == null ) {
            user = userRepository.findByEmail(email);
            if ( user != null ) userList.add(user);
        }
        if ( peers.size() == 0 || user == null ) {
            m.setMessage("No files");
        } else {
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
        }
        Gson gson = new Gson();
        m.setMessage(gson.toJson(availableFiles));
        return m;
    }
}
