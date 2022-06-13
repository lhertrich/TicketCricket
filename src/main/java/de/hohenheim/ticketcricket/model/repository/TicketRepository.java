package de.hohenheim.ticketcricket.model.repository;

import de.hohenheim.ticketcricket.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
}
