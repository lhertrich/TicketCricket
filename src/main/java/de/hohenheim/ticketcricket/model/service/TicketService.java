package de.hohenheim.ticketcricket.model.service;

import de.hohenheim.ticketcricket.model.entity.Ticket;
import de.hohenheim.ticketcricket.model.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public List<Ticket> findAllTickets(){
        return ticketRepository.findAll();
    }

}
