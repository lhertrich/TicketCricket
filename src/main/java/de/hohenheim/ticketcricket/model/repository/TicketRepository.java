package de.hohenheim.ticketcricket.model.repository;

import de.hohenheim.ticketcricket.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
}
