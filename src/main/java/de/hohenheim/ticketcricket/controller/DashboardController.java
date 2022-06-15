package de.hohenheim.ticketcricket.controller;

import de.hohenheim.ticketcricket.model.entity.User;
import de.hohenheim.ticketcricket.model.service.TicketService;
import de.hohenheim.ticketcricket.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    /**
     * Zeigt die Startseite Ihrer Anwendung.
     *
     * @param model enthält alle ModelAttribute.
     * @return home-Seite.
     */
    @GetMapping("/")
    public String showHome(Model model) {
        User currentUser = userService.getCurrentUser();
        if(currentUser.getRoles().contains("ROLE_ADMIN")) {
            model.addAttribute("tickets", ticketService.findAllTickets());
            return "admin/dashboard";
        } else {
            model.addAttribute("tickets", ticketService.findAllTicketsByUser(currentUser));
            return "user/dashboard";
        }
    }

    @GetMapping("/user")
    public String user(Model model){
        model.addAttribute("message", "Und hier sehen Sie ein ModelAttribut");
        return "userTicketerstellung";
    }
    @PostMapping("/user")
    public String storeTicket(Model model){
        model.addAttribute("message", "Und hier sehen Sie ein ModelAttribut");
        // hier müssen noch die Daten gespeichert werden, die eingegeben wurden.
        return "admin/dashboard";
    }

    @GetMapping("/ticket/expand{id}")
    public String expandTicket(@RequestParam("id") Integer id, Model model){
        model.addAttribute("ticket", ticketService.findTicketById(id));
        return "admin/expanded-ticket";
    }




}
