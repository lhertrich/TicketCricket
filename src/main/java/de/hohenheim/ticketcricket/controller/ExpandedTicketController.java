package de.hohenheim.ticketcricket.controller;

import de.hohenheim.ticketcricket.model.entity.Notification;
import de.hohenheim.ticketcricket.model.entity.NotificationType;
import de.hohenheim.ticketcricket.model.entity.Role;
import de.hohenheim.ticketcricket.model.entity.User;
import de.hohenheim.ticketcricket.model.service.NotificationService;
import de.hohenheim.ticketcricket.model.service.TicketService;
import de.hohenheim.ticketcricket.model.service.UserService;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.Set;

@Controller
public class ExpandedTicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/ticket/expand{id}")
    public String expandTicket(@RequestParam("id") Integer id, Model model){
        User currentUser = userService.getCurrentUser();
        Set<Role> roles = currentUser.getRoles();
        Set<String> roleNames = roles.stream().map(Role::getRolename).collect(java.util.stream.Collectors.toSet());
        model.addAttribute("ticket", ticketService.findTicketById(id));
        model.addAttribute("chatNotifications", notificationService.findAllNotificationsForTicket(id));
        if(roleNames.contains("ROLE_ADMIN")) {
            model.addAttribute("user", userService.getCurrentUser());
            model.addAttribute("compareDate", new Date(System.currentTimeMillis() - (60000*60*12)));
            model.addAttribute("currentNotifications", notificationService.findAllCurrentNotifications());
            model.addAttribute("oldNotifications", notificationService.findAllOldNotifications());
        } else {
            model.addAttribute("compareDate", new Date(System.currentTimeMillis() - (60000*60*12)));
            model.addAttribute("currentNotifications", notificationService.findAllCurrentNotificatinsForUser(currentUser));
            model.addAttribute("oldNotifications", notificationService.findAllOldNotficiationsForUser(currentUser));
        }
        return "expanded-ticket";
    }

    @PostMapping("/ticket/delete{id}")
    public String deleteTicket(@RequestParam("id") Integer id) {
        ticketService.deleteTicket(ticketService.findTicketById(id));
        return "redirect:/";
    }

    @PostMapping("/ticket/status{id}")
    public String requestStatus(@RequestParam("id") Integer id){
        ticketService.setRequest(id);
        Notification notification = new Notification();
        notification.setNotificationType(NotificationType.STATUS_ANFRAGE);
        notification.setTicket(ticketService.findTicketById(id));
        notification.setUser(userService.getCurrentUser());
        notification.setDate(new Date());
        notificationService.saveNotification(notification);
        return "redirect:/ticket/expand?id="+id;
    }
}
