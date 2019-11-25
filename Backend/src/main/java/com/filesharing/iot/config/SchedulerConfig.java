package com.filesharing.iot.config;

import com.filesharing.iot.models.File;
import com.filesharing.iot.repository.FileRepository;
import com.filesharing.iot.repository.PeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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
    FileRepository fileRepository;
    List<File> files = new ArrayList<>();
    Integer filesSize = 0;

    @Scheduled(fixedDelay = 10000)
    @SendTo("/topic/files")
    public void sendMessage() {
        files = fileRepository.getFiles();
        if (files.size() != filesSize){
            filesSize = files.size();
            template.convertAndSend("/topic/files", files);
        } else if (files.size() == 0){
            template.convertAndSend("/topic/files", "No files");
        }
    }
}
