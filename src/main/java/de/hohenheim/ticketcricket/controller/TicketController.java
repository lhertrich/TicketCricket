package de.hohenheim.ticketcricket.controller;

import de.hohenheim.ticketcricket.model.entity.*;
import de.hohenheim.ticketcricket.model.service.NotificationService;
import de.hohenheim.ticketcricket.model.service.TicketService;
import de.hohenheim.ticketcricket.model.service.UserService;
import de.hohenheim.ticketcricket.model.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Controller
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private TicketValidator ticketValidator;

    @Autowired
    private NotificationService notificationService;

    @InitBinder("ticket")
    public void initBinder(WebDataBinder binder){
        binder.setValidator(ticketValidator);
    }

    @GetMapping("/ticket-form")
    public String showTicketForm(Model model){
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("admins", userService.getAdmins());
        User currentUser = userService.getCurrentUser();
        model.addAttribute("currentNotifications", notificationService.findAllCurrentNotificationsForUser(currentUser));
        model.addAttribute("oldNotifications", notificationService.findAllOldNotificationsForUser(currentUser));
        model.addAttribute("newNotifications", notificationService.findAllNewNotificationsForUser(currentUser));
        return "ticketerstellung";
    }
    @PostMapping("/create-ticket")
    public String createTicket(@Valid @ModelAttribute("ticket") Ticket ticket, BindingResult result, Model model){
        User currentUser = userService.getCurrentUser();
        if(result.hasErrors()){
            model.addAttribute("ticket", ticket);
            model.addAttribute("currentNotifications", notificationService.findAllCurrentNotificationsForUser(currentUser));
            model.addAttribute("oldNotifications", notificationService.findAllOldNotificationsForUser(currentUser));
            model.addAttribute("newNotifications", notificationService.findAllNewNotificationsForUser(currentUser));
            return "ticketerstellung";
        }
        ticket.setStatus(Status.OFFEN);
        ticket.setUser(userService.getCurrentUser());
        ticket.setDate(new Date());
        ticket.setLastRequest(new Date());
        ticket.setPriority(prioritiseTicket(ticket));
        if(ticket.getAdmin() == null){
            ticket.setAdmin(assignAdmin(ticket));
        }
        ticketService.saveTicket(ticket);
        return "redirect:/";
    }

    private Priority prioritiseTicket(Ticket ticket){
        if(ticket.getCategory() == Category.TECHNISCHE_PROBLEME){
            return Priority.SEHR_WICHTIG;
        } else if(ticket.getCategory() == Category.INAKTIVITÄT){
            return Priority.WICHTIG;
        } else {
            return Priority.UNWICHTIG;
        }
    }

    private User assignAdmin(Ticket ticket){
        List<User> capableAdmins = userService.getAdminsByCategory(ticket.getCategory());
        User assignedAdmin = capableAdmins.get(0);
        int minNumberOfTickets = ticketService.findAllTicketsForAdmin(assignedAdmin).size();
        for(User admin : capableAdmins){
            if(ticketService.findAllTicketsForAdmin(admin).size() < minNumberOfTickets){
                assignedAdmin = admin;
                minNumberOfTickets = ticketService.findAllTicketsForAdmin(admin).size();
            }
        }
        return assignedAdmin;
    }

}
