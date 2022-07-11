package de.hohenheim.ticketcricket.controller;

import de.hohenheim.ticketcricket.model.entity.*;
import de.hohenheim.ticketcricket.model.service.MessageService;
import de.hohenheim.ticketcricket.model.service.NotificationService;
import de.hohenheim.ticketcricket.model.service.TicketService;
import de.hohenheim.ticketcricket.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ExpandedTicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private MessageService messageService;

    @GetMapping("/ticket/expand{id}")
    public String expandTicket(@RequestParam("id") Integer id, Model model) {
        User currentUser = userService.getCurrentUser();
        Set<Role> roles = currentUser.getRoles();
        Set<String> roleNames = roles.stream().map(Role::getRolename).collect(java.util.stream.Collectors.toSet());
        model.addAttribute("ticket", ticketService.findTicketById(id));
        List<Message> allMessages = messageService.findAllMessages();
        List<Message> messages = allMessages.stream().filter(m -> m.getTicket().getTicketID() == id).collect(java.util.stream.Collectors.toList());
        model.addAttribute("messages", messages);
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("compareDate", new Date(System.currentTimeMillis() - (60000 * 60 * 12)));
        model.addAttribute("currentUser", userService.getCurrentUser());
        model.addAttribute("chatNotifications", notificationService.findAllNotificationsForTicket(id));
        model.addAttribute("currentNotifications", notificationService.findAllCurrentNotificationsForUser(currentUser));
        model.addAttribute("oldNotifications", notificationService.findAllOldNotificationsForUser(currentUser));
        model.addAttribute("newNotifications", notificationService.findAllNewNotificationsForUser(currentUser));
        model.addAttribute("admins", userService.getAdmins());
        model.addAttribute("responsibleAdminChar", Character.toUpperCase(ticketService.findTicketById(id).getAdmin().getUsername().charAt(0)));
        model.addAttribute("responsibleAdmin", ticketService.findTicketById(id).getAdmin().getUsername());
        model.addAttribute("bookmark", ticketService.findTicketById(id).getBookmark());
        model.addAttribute("notifications", notificationService.findAllNotificationsForTicket(id));
        return "expanded-ticket";
    }

    @PostMapping("/ticket/delete{id}")
    public String deleteTicket(@RequestParam("id") Integer id) {
        ticketService.deleteTicket(ticketService.findTicketById(id));
        return "redirect:/";
    }
    @PostMapping("/ticket/expand/setStatus{id}")
    public String setStatus(@RequestParam("id") Integer id, @ModelAttribute("ticket") Ticket ticket){
        ticketService.setStatus(ticket.getStatus(), id);
        if(ticket.getStatus() == Status.ERLEDIGT){
            return "redirect:/";
        } else {
            return "redirect:/ticket/expand?id="+id;
        }
    }
    @PostMapping("/ticket/expand/setCategory{id}")
    public String setCategory(@RequestParam("id") Integer id, @ModelAttribute("ticket") Ticket ticket){
        ticketService.setCategory(ticket.getCategory(), id);
        return "redirect:/ticket/expand?id="+id;
    }

    @PostMapping("/ticket/expand/setPriority{id}")
    public String setPriority(@RequestParam("id") Integer id, @ModelAttribute("ticket") Ticket ticket){
        ticketService.setPriority(ticket.getPriority(), id);
        return "redirect:/ticket/expand?id="+id;
    }

    @PostMapping("/ticket/expand/setAdmin{id}")
    public String setAdmin(@RequestParam("id") Integer id, @ModelAttribute("ticket") Ticket ticket){
        ticketService.setAdmin(ticket.getAdmin(), id);
        return "redirect:/ticket/expand?id="+id;
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

    @GetMapping("/ticket/load-messages{id}")
    @ResponseBody
    public List<Message> loadMessages(@RequestParam("id") Integer id) {
        List<Message> messages = messageService.findAllMessages();
        List<Message> filteredMessages = messages.stream().filter(m -> m.getTicket().getTicketID() == id).collect(java.util.stream.Collectors.toList());
        return filteredMessages;
    }


    @PostMapping("/ticket/expand/setBookmark{id}")
    public String setBookmark(@RequestParam("id") Integer id, @ModelAttribute("ticket") Ticket ticket){
        List <User> userBookmark = ticketService.findTicketById(id).getBookmark();
        if (userBookmark.contains(userService.getCurrentUser())){
         ticketService.removeBookmark(userService.getCurrentUser(),id);
        }else{
           ticketService.setBookmark(userService.getCurrentUser(), id);
        }
        return "redirect:/ticket/expand?id="+id;
    }

    @GetMapping("/notification-clicked-ticket{notificationId}")
    public String deleteNotification(@RequestParam("notificationId") Integer id){
        int ticketId = notificationService.findNotificiationById(id).getTicket().getTicketID();
        notificationService.deleteNotification(notificationService.findNotificiationById(id));
        return "redirect:/ticket/expand?id="+ticketId;
    }
}
