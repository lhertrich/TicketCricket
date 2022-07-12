package de.hohenheim.ticketcricket.controller;

import de.hohenheim.ticketcricket.model.entity.*;
import de.hohenheim.ticketcricket.model.service.MessageService;
import de.hohenheim.ticketcricket.model.service.NotificationService;
import de.hohenheim.ticketcricket.model.service.TicketService;
import de.hohenheim.ticketcricket.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
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

    @MessageMapping("/chat{id}")
    @SendTo("/topic/chat{id}")
    public Message chat(Message message) {
        messageService.saveMessage(message);
        Ticket ticket = ticketService.findTicketById(message.getTicket().getTicketID());
        Notification messageNotification = new Notification();
        messageNotification.setNotificationType(NotificationType.NACHRICHT);
        messageNotification.setTicket(ticket);
        messageNotification.setDate(new Date());
        messageNotification.setUser(ticket.getAdmin());
        Notification userMessageNotification = new Notification();
        userMessageNotification.setNotificationType(NotificationType.NACHRICHT);
        userMessageNotification.setTicket(ticket);
        userMessageNotification.setDate(new Date());
        userMessageNotification.setUser(ticket.getUser());
        notificationService.saveNotification(messageNotification);
        notificationService.saveNotification(userMessageNotification);
        return message;
    }

    @MessageMapping("/status{id}")
    @SendTo("/topic/status{id}")
    public Message requestStatus(Message message) {
        int id = message.getTicket().getTicketID();
        User currentUser = message.getUser();
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

}
