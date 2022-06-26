package de.hohenheim.ticketcricket.controller;

import de.hohenheim.ticketcricket.model.entity.Role;
import de.hohenheim.ticketcricket.model.entity.Status;
import de.hohenheim.ticketcricket.model.entity.Ticket;
import de.hohenheim.ticketcricket.model.entity.User;
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
        User currentUser = userService.getCurrentUser();
        Set<Role> roles = currentUser.getRoles();
        Set<String> roleNames = roles.stream().map(Role::getRolename).collect(java.util.stream.Collectors.toSet());
        if(roleNames.contains("ROLE_ADMIN")) {
            model.addAttribute("currentNotifications", notificationService.findAllCurrentNotifications());
            model.addAttribute("oldNotifications", notificationService.findAllOldNotifications());
        } else {
            model.addAttribute("currentNotifications", notificationService.findAllCurrentNotificatinsForUser(currentUser));
            model.addAttribute("oldNotifications", notificationService.findAllOldNotficiationsForUser(currentUser));
        }
        return "ticketerstellung";
    }
    @PostMapping("/create-ticket")
    public String createTicket(@Valid @ModelAttribute("ticket") Ticket ticket, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("ticket", ticket);
            return "ticketerstellung";
        }
        ticket.setStatus(Status.OFFEN);
        ticket.setUser(userService.getCurrentUser());
        Date currentDate = new Date(System.currentTimeMillis());
        ticket.setDate(currentDate);
        ticket.setLastRequest(currentDate);
        ticketService.saveTicket(ticket);
        return "redirect:/";
    }

}
