package com.filesharing.iot.controller;

import com.filesharing.iot.models.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public Message sendMessage(@Payload Message message) {
        return message;
    }
}
