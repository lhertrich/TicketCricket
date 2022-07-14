package de.hohenheim.ticketcricket.controller;

import de.hohenheim.ticketcricket.model.entity.*;
import de.hohenheim.ticketcricket.model.service.MessageService;
import de.hohenheim.ticketcricket.model.service.NotificationService;
import de.hohenheim.ticketcricket.model.service.TicketService;
import de.hohenheim.ticketcricket.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@Transactional
public class ChatController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @MessageMapping("/chat{id}")
    @SendTo("/topic/chat{id}")
    public Message chat(Message message, Principal principal) {
        messageService.saveMessage(message);
        Ticket ticket = ticketService.findTicketById(message.getTicket().getTicketID());
        String username = ((UserDetails) ((Authentication) principal).getPrincipal()).getUsername();
        User currentUser = userService.getUserByUsername(username);
        Set<String> rolenames = currentUser.getRoles().stream().map(Role::getRolename).collect(Collectors.toSet());
        if(rolenames.contains("ROLE_ADMIN") && currentUser.equals(ticket.getAdmin())) {
            Notification userMessageNotification = new Notification();
            userMessageNotification.setNotificationType(NotificationType.NACHRICHT);
            userMessageNotification.setTicket(ticket);
            userMessageNotification.setDate(new Date());
            userMessageNotification.setUser(ticket.getUser());
            notificationService.saveNotification(userMessageNotification);
        } else if (rolenames.contains("ROLE_ADMIN")) {
            Notification messageNotification = new Notification();
            messageNotification.setNotificationType(NotificationType.NACHRICHT);
            messageNotification.setTicket(ticket);
            messageNotification.setDate(new Date());
            messageNotification.setUser(ticket.getAdmin());
            notificationService.saveNotification(messageNotification);
            Notification userMessageNotification = new Notification();
            userMessageNotification.setNotificationType(NotificationType.NACHRICHT);
            userMessageNotification.setTicket(ticket);
            userMessageNotification.setDate(new Date());
            userMessageNotification.setUser(ticket.getUser());
            notificationService.saveNotification(userMessageNotification);
        } else {
            Notification messageNotification = new Notification();
            messageNotification.setNotificationType(NotificationType.NACHRICHT);
            messageNotification.setTicket(ticket);
            messageNotification.setDate(new Date());
            messageNotification.setUser(ticket.getAdmin());
            notificationService.saveNotification(messageNotification);
        }
        return message;
    }

    @MessageMapping("/status{id}")
    @SendTo("/topic/status{id}")
    public Message requestStatus(Message message) {
        int id = message.getTicket().getTicketID();
        Ticket ticket = message.getTicket();
        ticketService.setRequest(id);
        ticketService.setViewed(false, id);
        Notification notification = new Notification();
        notification.setNotificationType(NotificationType.STATUS_ANFRAGE);
        notification.setTicket(ticket);
        notification.setUser(ticket.getAdmin());
        notification.setDate(new Date());
        notificationService.saveNotification(notification);
        messageService.saveMessage(message);
        return message;
    }

    @MessageMapping("/disabled{id}")
    @SendTo("/topic/disabled{id}")
    public Message disabled(Message message) {
        messageService.saveMessage(message);
        return message;
    }

    @MessageMapping("/enabled{id}")
    @SendTo("/topic/enabled{id}")
    public Message enabled(Message message) {
        messageService.saveMessage(message);
        return message;
    }

    @MessageMapping("/status-change{id}")
    @SendTo("/topic/status-change{id}")
    public Message statusChange(Message message, Principal principal) {
        String username = ((UserDetails) ((Authentication) principal).getPrincipal()).getUsername();
        User currentUser = userService.getUserByUsername(username);

        messageService.saveMessage(message);
        Ticket newTicket = ticketService.findTicketById(message.getTicket().getTicketID());

        Notification statusNotification = new Notification();
        statusNotification.setNotificationType(NotificationType.STATUS_ÄNDERUNG);
        statusNotification.setTicket(newTicket);
        statusNotification.setDate(new Date());
        statusNotification.setUser(newTicket.getUser());

        if(!currentUser.equals(newTicket.getAdmin())) {
            Notification adminNotification = new Notification();
            adminNotification.setNotificationType(NotificationType.STATUS_ÄNDERUNG);
            adminNotification.setTicket(newTicket);
            adminNotification.setDate(new Date());
            adminNotification.setUser(newTicket.getAdmin());
            notificationService.saveNotification(adminNotification);
        }

        notificationService.saveNotification(statusNotification);

        return message;
    }

}
