package com.filesharing.iot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {
    @Autowired
    SimpMessagingTemplate template;

    @Scheduled(fixedDelay = 3000)
    @SendTo("/topic/messages")
    public void sendPong() {
        template.convertAndSend("/topic/messages", "message (scheduled)");
    }
}
