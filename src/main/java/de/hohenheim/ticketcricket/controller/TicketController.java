package de.hohenheim.ticketcricket.controller;

import de.hohenheim.ticketcricket.model.entity.Status;
import de.hohenheim.ticketcricket.model.entity.Ticket;
import de.hohenheim.ticketcricket.model.service.TicketService;
import de.hohenheim.ticketcricket.model.service.UserService;
import de.hohenheim.ticketcricket.model.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Date;

@Controller
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private TicketValidator ticketValidator;

    @InitBinder("ticket")
    public void initBinder(WebDataBinder binder){
        binder.setValidator(ticketValidator);
    }

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
    public String createUserTicket(@Valid @ModelAttribute("ticket") Ticket ticket, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("ticket", ticket);
            return "user/userTicketerstellung";
        }
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
