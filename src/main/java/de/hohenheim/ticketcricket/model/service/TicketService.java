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
            allTicketsSearch = allTickets.stream().filter(x -> x.getTitle().replaceAll("\s", "").toLowerCase().contains(searchString.toLowerCase())).collect(Collectors.toList());
        } else {
            allTicketsSearch = allTickets;
        }
        return allTicketsSearch;
    }

    public List<Ticket> findAllTicketsByUserFilter(String filterString, User user){
        List<Ticket> allTickets;
        List<Ticket> allTicketsFilter = new ArrayList<>();
        Set<String> userRolenames = user.getRoles().stream().map(Role::getRolename).collect(Collectors.toSet());
        if(userRolenames.contains("ROLE_ADMIN")){
            allTickets = findAllTickets();
        } else {
            allTickets = findAllTicketsByUser(user);
        }
        if(filterString.equals("")){
            return allTickets;
        }
        if (filterString.contains("OFFEN")){
            allTicketsFilter.addAll(allTickets.stream().filter(x -> x.getStatus() == Status.OFFEN).collect(Collectors.toList()));
        }
        if(filterString.contains("IN_BEARBEITUNG")){
            allTicketsFilter.addAll(allTickets.stream().filter(x -> x.getStatus() == Status.IN_BEARBEITUNG).collect(Collectors.toList()));
        }
        if(filterString.contains("ERLEDIGT")){
            allTicketsFilter.addAll(allTickets.stream().filter(x -> x.getStatus() == Status.ERLEDIGT).collect(Collectors.toList()));
        }

        if(filterString.contains("INAKTIVITÄT")){
            allTicketsFilter.addAll(allTickets.stream().filter(x -> x.getCategory() == Category.INAKTIVITÄT).collect(Collectors.toList()));
        }
        if(filterString.contains("TECHNISCHE_PROBLEME")){
            allTicketsFilter.addAll(allTickets.stream().filter(x -> x.getCategory() == Category.TECHNISCHE_PROBLEME).collect(Collectors.toList()));
        }
        if(filterString.contains("SONSTIGES")){
            allTicketsFilter.addAll(allTickets.stream().filter(x -> x.getCategory() == Category.SONSTIGES).collect(Collectors.toList()));
        }
        return allTicketsFilter;
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
