package de.hohenheim.ticketcricket.model.repository;

import de.hohenheim.ticketcricket.model.entity.MessageDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageDraftRepository extends JpaRepository<MessageDraft, Integer> {
    MessageDraft findMessageDraftByMessage(String messageDraft);
}
