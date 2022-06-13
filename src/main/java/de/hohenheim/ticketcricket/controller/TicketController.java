package de.hohenheim.ticketcricket.controller;

import de.hohenheim.ticketcricket.model.entity.Status;
import de.hohenheim.ticketcricket.model.entity.Ticket;
import de.hohenheim.ticketcricket.model.service.TicketService;
import de.hohenheim.ticketcricket.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@Controller
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @GetMapping("/user/ticketForm")
    public String showUserTicketForm(Model model){
        model.addAttribute("ticket", new Ticket());
        return "user/userTicketerstellung";
    }

    @GetMapping("/admin/ticketForm")
    public String showAdminTicketForm(Model model){
        model.addAttribute("ticket", new Ticket());
        return "user/userTicketerstellung";
    }

    @PostMapping("/user/createTicket")
    public String createUserTicket(@ModelAttribute("ticket") Ticket ticket){
        ticket.setStatus(Status.OFFEN);
        ticket.setUser(userService.getCurrentUser());
        Date currentDate = new Date(System.currentTimeMillis());
        ticket.setDate(currentDate);
        ticket.setLastRequest(currentDate);
        ticketService.saveTicket(ticket);
        return "redirect:/";
    }

    @PostMapping("/admin/createTicket")
    public String createAdminTicket(@ModelAttribute("ticket") Ticket ticket){
        ticket.setStatus(Status.OFFEN);
        ticket.setUser(userService.getCurrentUser());
        Date currentDate = new Date(System.currentTimeMillis());
        ticket.setDate(currentDate);
        ticket.setLastRequest(currentDate);
        ticketService.saveTicket(ticket);
        return "redirect:/";
    }
}
