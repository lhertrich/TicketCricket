package de.hohenheim.ticketcricket.model.service;

import de.hohenheim.ticketcricket.model.entity.Message;
import de.hohenheim.ticketcricket.model.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message saveMessage(Message message){
        return messageRepository.save(message);
    }

    public List<Message> findAllMessages() {
        return messageRepository.findAll();
    }
}
