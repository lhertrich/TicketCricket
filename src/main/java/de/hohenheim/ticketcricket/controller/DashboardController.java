package de.hohenheim.ticketcricket.controller;

import de.hohenheim.ticketcricket.config.SelectionObject;
import de.hohenheim.ticketcricket.model.entity.Role;

import de.hohenheim.ticketcricket.model.entity.Ticket;
import de.hohenheim.ticketcricket.model.entity.User;
import de.hohenheim.ticketcricket.model.service.NotificationService;
import de.hohenheim.ticketcricket.model.service.TicketService;
import de.hohenheim.ticketcricket.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        model.addAttribute("currentUser", userService.getCurrentUser());
        model.addAttribute("currentNotifications", notificationService.findAllCurrentNotificationsForUser(currentUser));
        model.addAttribute("oldNotifications", notificationService.findAllOldNotificationsForUser(currentUser));
        model.addAttribute("newNotifications", notificationService.findAllNewNotificationsForUser(currentUser));
        if(roleNames.contains("ROLE_ADMIN")) {
            model.addAttribute("tickets", ticketService.findAllTickets());
        } else {
            model.addAttribute("tickets", ticketService.findAllTicketsByUser(currentUser));
        }
        return "dashboard";
    }

    @PostMapping("/setBookmark{id}")
    public String setBookmark(@RequestParam("id") Integer id, @ModelAttribute("ticket") Ticket ticket){
        List<User> userBookmark = ticketService.findTicketById(id).getBookmark();
        if (userBookmark.contains(userService.getCurrentUser())){
            ticketService.removeBookmark(userService.getCurrentUser(),id);
        }else{
            ticketService.setBookmark(userService.getCurrentUser(), id);
        }
        return "redirect:/";
    }

    @GetMapping("/ajax/updateHome{searchString}")
    @ResponseStatus(value = HttpStatus.OK)
    public String updateHome(Model model, @RequestParam("searchString") String searchString){
        User currentUser = userService.getCurrentUser();
        Set<Role> roles = currentUser.getRoles();
        Set<String> roleNames = roles.stream().map(Role::getRolename).collect(java.util.stream.Collectors.toSet());
        model.addAttribute("tickets", ticketService.findAllTicketsByUserSearch(searchString, currentUser));
        return "dashboard :: #innerWindowTickets";
    }

    @GetMapping("/ajax/filter{filterString}")
    @ResponseStatus(value = HttpStatus.OK)
    public String filterHome(Model model, @RequestParam("filterString") String filterString){
        User currentUser = userService.getCurrentUser();
        Set<Role> roles = currentUser.getRoles();
        Set<String> roleNames = roles.stream().map(Role::getRolename).collect(java.util.stream.Collectors.toSet());
        model.addAttribute("tickets", ticketService.findAllTicketsByUserFilter(filterString, currentUser));
        return "dashboard :: #innerWindowTickets";
    }

    @PostMapping(value = "/ajax/updateDashboard", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public String updateHome(@RequestBody SelectionObject selectionObject, Model model){
        User currentUser = userService.getCurrentUser();
        System.out.println("filterstring: "+selectionObject.getFilterString()+"; searchstring: "+selectionObject.getSearchString()+"; sortString: "+selectionObject.getSortString());
        model.addAttribute("tickets", ticketService.findAllTicketsForUserSelection(currentUser, selectionObject));
        return "dashboard :: #ticketsWithHeader";
    }

}
