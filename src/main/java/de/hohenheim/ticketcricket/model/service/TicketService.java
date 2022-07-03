package de.hohenheim.ticketcricket.model.service;

import de.hohenheim.ticketcricket.model.entity.Role;
import de.hohenheim.ticketcricket.model.entity.Ticket;
import de.hohenheim.ticketcricket.model.entity.User;
import de.hohenheim.ticketcricket.model.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public List<Ticket> findAllTicketsByUserSearch(String searchString, User user){
        List<Ticket> allTickets;
        Set<String> userRolenames = user.getRoles().stream().map(Role::getRolename).collect(Collectors.toSet());
        if(userRolenames.contains("ROLE_ADMIN")){
            allTickets = findAllTickets();
        } else {
            allTickets = findAllTicketsByUser(user);
        }
        List<Ticket> allTicketsSearch;
        if(searchString != null){
            allTicketsSearch = allTickets.stream().filter(x -> x.getTitle().toLowerCase().contains(searchString.toLowerCase())).collect(Collectors.toList());
        } else {
            allTicketsSearch = allTickets;
        }
        return allTicketsSearch;
    }

    public void setRequest(int id){
        Ticket ticketToUpdate = ticketRepository.getById(id);
        ticketToUpdate.setLastRequest(new Date(System.currentTimeMillis()));
        ticketRepository.save(ticketToUpdate);
    }
}
