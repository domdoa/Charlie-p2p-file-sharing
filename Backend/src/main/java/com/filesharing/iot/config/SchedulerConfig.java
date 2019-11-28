package com.filesharing.iot.config;

import com.filesharing.iot.models.File;
import com.filesharing.iot.models.Peer;
import com.filesharing.iot.repository.FileRepository;
import com.filesharing.iot.repository.PeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableScheduling
public class SchedulerConfig {
    @Autowired
    SimpMessagingTemplate template;
    @Autowired
    PeerRepository peerRepository;


//    @SendTo("/topic/files")
//    public void sendMessageFiles(Payload payload) {
//        System.out.println(payload);
//        template.convertAndSend("/topic/files", payload);
//    }

    @Scheduled(fixedDelay = 3000)
    @SendTo("/topic/peers")
    public void sendMessagePeers() {
        List<Peer> peers = peerRepository.getPeers();
        if (peers.size() > 0){
            template.convertAndSend("/topic/peers", peers);
        } else {
            template.convertAndSend("/topic/peers", "No peers");
        }
    }
}
