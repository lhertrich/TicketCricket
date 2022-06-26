package de.hohenheim.ticketcricket.controller;

import de.hohenheim.ticketcricket.model.entity.Role;

import de.hohenheim.ticketcricket.model.entity.User;
import de.hohenheim.ticketcricket.model.service.NotificationService;
import de.hohenheim.ticketcricket.model.service.TicketService;
import de.hohenheim.ticketcricket.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Set;

@Controller
public class DashboardController {


    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/")
    public String showHome(Model model) {
        User currentUser = userService.getCurrentUser();
        Set<Role> roles = currentUser.getRoles();
        Set<String> roleNames = roles.stream().map(Role::getRolename).collect(java.util.stream.Collectors.toSet());
        if(roleNames.contains("ROLE_ADMIN")) {
            model.addAttribute("tickets", ticketService.findAllTickets());
            model.addAttribute("currentNotifications", notificationService.findAllCurrentNotifications());
            model.addAttribute("oldNotifications", notificationService.findAllOldNotifications());
        } else {
            model.addAttribute("tickets", ticketService.findAllTicketsByUser(currentUser));
            model.addAttribute("currentNotifications", notificationService.findAllCurrentNotificatinsForUser(currentUser));
            model.addAttribute("oldNotifications", notificationService.findAllOldNotficiationsForUser(currentUser));
        }
        return "dashboard";
    }

}
