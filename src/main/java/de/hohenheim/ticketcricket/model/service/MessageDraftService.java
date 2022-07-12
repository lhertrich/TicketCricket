package de.hohenheim.ticketcricket.model.service;

import de.hohenheim.ticketcricket.model.entity.MessageDraft;
import de.hohenheim.ticketcricket.model.repository.MessageDraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MessageDraftService {

    @Autowired
    MessageDraftRepository messageDraftRepository;

    public MessageDraft saveMessage(MessageDraft messageDraft){
        return messageDraftRepository.save(messageDraft);
    }
    public void deleteMessageDraft(MessageDraft messageDraft){
         messageDraftRepository.delete(messageDraft);
    }
    public Set<MessageDraft> findAllMessageDrafts(){
        return messageDraftRepository.findAll().stream().collect(Collectors.toSet());
    }

    public void deleteMessageDraftByText(String messageDraft){
        MessageDraft messageDraftToDelete = messageDraftRepository.findMessageDraftByMessage(messageDraft);
        messageDraftRepository.delete(messageDraftToDelete);
    }
}
