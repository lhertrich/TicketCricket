package de.hohenheim.ticketcricket.model.repository;

import de.hohenheim.ticketcricket.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}
