package de.hohenheim.ticketcricket.controller;

import de.hohenheim.ticketcricket.model.entity.Message;
import de.hohenheim.ticketcricket.model.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat{id}")
    @SendTo("/topic/chat{id}")
    public Message chat(Message message) {
        messageService.saveMessage(message);
        return message;
    }

}
