package de.hohenheim.ticketcricket.controller;

import de.hohenheim.ticketcricket.model.entity.Role;

import de.hohenheim.ticketcricket.model.entity.User;
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


    @GetMapping("/")
    public String showHome(Model model) {
        User currentUser = userService.getCurrentUser();
        Set<Role> roles = currentUser.getRoles();
        Set<String> roleNames = roles.stream().map(Role::getRolename).collect(java.util.stream.Collectors.toSet());
        model.addAttribute("currentUser", userService.getCurrentUser());
        if(roleNames.contains("ROLE_ADMIN")) {
            model.addAttribute("tickets", ticketService.findAllTickets());
        } else {
            model.addAttribute("tickets", ticketService.findAllTicketsByUser(currentUser));
        }
        return "dashboard";
    }

}
