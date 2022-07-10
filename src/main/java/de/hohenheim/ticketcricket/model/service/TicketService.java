package de.hohenheim.ticketcricket.model.service;

import de.hohenheim.ticketcricket.config.SelectionObject;
import de.hohenheim.ticketcricket.model.entity.*;
import de.hohenheim.ticketcricket.model.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
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
            allTicketsSearch = allTickets.stream().filter(x -> x.getTitle().replaceAll("\s", "").toLowerCase().contains(searchString.toLowerCase())).collect(Collectors.toList());
        } else {
            allTicketsSearch = allTickets;
        }
        return allTicketsSearch;
    }

    private List<Ticket> filterTicketsByCategory(List<Ticket> tickets, String filterString){
        List<Ticket> categoryTickets = new ArrayList<>();
        if(filterString.equals("")){
            return tickets;
        }
        if (filterString.contains("OFFEN")){
            categoryTickets.addAll(tickets.stream().filter(x -> x.getStatus() == Status.OFFEN).collect(Collectors.toList()));
        }
        if(filterString.contains("IN_BEARBEITUNG")){
            categoryTickets.addAll(tickets.stream().filter(x -> x.getStatus() == Status.IN_BEARBEITUNG).collect(Collectors.toList()));
        }
        if(filterString.contains("ERLEDIGT")){
            categoryTickets.addAll(tickets.stream().filter(x -> x.getStatus() == Status.ERLEDIGT).collect(Collectors.toList()));
        }

        if(categoryTickets.isEmpty()){
            return tickets;
        }
        return categoryTickets;
    }

    private List<Ticket> filterTicketsByStatus(List<Ticket> tickets, String filterString){
        List<Ticket> statusTickets = new ArrayList<>();
        if(filterString.equals("")){
            return tickets;
        }
        if (filterString.contains("INAKTIVITÄT")){
            statusTickets.addAll(tickets.stream().filter(x -> x.getCategory() == Category.INAKTIVITÄT).collect(Collectors.toList()));
        }
        if(filterString.contains("TECHNISCHE_PROBLEME")){
            statusTickets.addAll(tickets.stream().filter(x -> x.getCategory() == Category.TECHNISCHE_PROBLEME).collect(Collectors.toList()));
        }
        if(filterString.contains("SONSTIGES")){
            statusTickets.addAll(tickets.stream().filter(x -> x.getCategory() == Category.SONSTIGES).collect(Collectors.toList()));
        }
        if(statusTickets.isEmpty()){
            return tickets;
        }
        return statusTickets;
    }

    public List<Ticket> findAllTicketsByUserFilter(String filterString, User user){
        List<Ticket> allTickets;
        Set<String> userRolenames = user.getRoles().stream().map(Role::getRolename).collect(Collectors.toSet());
        if(userRolenames.contains("ROLE_ADMIN")){
            allTickets = findAllTickets();
        } else {
            allTickets = findAllTicketsByUser(user);
        }
        List<Ticket> categoryTickets = filterTicketsByCategory(allTickets, filterString);
        List<Ticket> statusTickets = filterTicketsByStatus(allTickets, filterString);
        List<Ticket> filterTickets = categoryTickets.stream().filter(statusTickets::contains).collect(Collectors.toList());
        
        return filterTickets;
    }

    private static void sortTickets(List<Ticket> tickets, String sortString){
        if(sortString == null){
            return;
        } else if(sortString.equals("Kategorie")){
            Collections.sort(tickets, Comparator.comparing(Ticket::getCategory));
        } else if(sortString.equals("Status")){
            Collections.sort(tickets, Comparator.comparing(Ticket::getStatus));
        }
    }

    public List<Ticket> findAllTicketsForUserSelection(User user, SelectionObject selectionObject){
        List<Ticket> selectedTickets = findAllTicketsByUserFilter(selectionObject.getFilterString(), user);
        List<Ticket> searchTickets = findAllTicketsByUserSearch(selectionObject.getSearchString(), user);
        selectedTickets = selectedTickets.stream().filter(searchTickets::contains).collect(Collectors.toList());
        sortTickets(selectedTickets, selectionObject.getSortString());
        return selectedTickets;
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
