package de.hohenheim.ticketcricket.model.service;

import de.hohenheim.ticketcricket.model.entity.*;
import de.hohenheim.ticketcricket.model.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

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

    public void setPriority(Priority priority, int id){
        Ticket ticketToUpdate = ticketRepository.getById(id);
        ticketToUpdate.setPriority(priority);
        ticketRepository.save(ticketToUpdate);
    }

    public void setAdmin(User admin, int id){
        Ticket ticketToUpdate = ticketRepository.getById(id);
        ticketToUpdate.setAdmin(admin);
        ticketRepository.save(ticketToUpdate);
    }
    public void setBookmark(User bookmark, int id){
        Ticket ticketToUpdate = ticketRepository.getById(id);
        ticketToUpdate.getBookmark().add(bookmark);
        ticketRepository.save(ticketToUpdate);
    }

    public void removeBookmark(User bookmark, int id){
        Ticket ticketToUpdate = ticketRepository.getById(id);
        ticketToUpdate.getBookmark().remove(bookmark);
        ticketRepository.save(ticketToUpdate);
    }
}
