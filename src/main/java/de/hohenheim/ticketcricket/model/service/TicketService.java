package de.hohenheim.ticketcricket.model.service;

import de.hohenheim.ticketcricket.model.entity.Category;
import de.hohenheim.ticketcricket.model.entity.Status;
import de.hohenheim.ticketcricket.model.entity.Ticket;
import de.hohenheim.ticketcricket.model.entity.User;
import de.hohenheim.ticketcricket.model.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public void deleteTicket(Ticket ticket) {
        ticketRepository.delete(ticket);
    }

    public Ticket findTicketById(int id) {
        return ticketRepository.getById(id);
    }

    public List<Ticket> findAllTickets(){
        return ticketRepository.findAll();
    }

    public List<Ticket> findAllTicketsByUser(User user) {
        List<Ticket> allTickets = ticketRepository.findAll();
        List<Ticket> userTickets = new ArrayList<>();
        for (Ticket ticket : allTickets) {
            if (ticket.getUser().equals(user)) {
                userTickets.add(ticket);
            }
        }
        return userTickets;
    }

    public void setRequest(int id){
        Ticket ticketToUpdate = ticketRepository.getById(id);
        ticketToUpdate.setLastRequest(new Date(System.currentTimeMillis()));
        ticketRepository.save(ticketToUpdate);
    }

    public void setStatus(Status status, int id){
        Ticket ticketToUpdate = ticketRepository.getById(id);
        ticketToUpdate.setStatus(status);
        ticketRepository.save(ticketToUpdate);
    }

    public void setCategory(Category category, int id){
        Ticket ticketToUpdate = ticketRepository.getById(id);
        ticketToUpdate.setCategory(category);
        ticketRepository.save(ticketToUpdate);
    }

    public void setPriority(Category category, int id){
        Ticket ticketToUpdate = ticketRepository.getById(id);
        ticketToUpdate.setCategory(category);
        ticketRepository.save(ticketToUpdate);
    }
}
