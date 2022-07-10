package de.hohenheim.ticketcricket.controller;

import de.hohenheim.ticketcricket.model.entity.Message;
import de.hohenheim.ticketcricket.model.entity.Notification;
import de.hohenheim.ticketcricket.model.entity.Ticket;
import de.hohenheim.ticketcricket.model.entity.User;
import de.hohenheim.ticketcricket.model.service.MessageService;
import de.hohenheim.ticketcricket.model.service.NotificationService;
import de.hohenheim.ticketcricket.model.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.Date;

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
        return message;
    }

    @MessageMapping("/status{id}")
    @SendTo("/topic/status{id}")
    public Message requestStatus(Message message) {
        int id = message.getTicket().getTicketID();
        User currentUser = message.getUser();
        Ticket ticket = message.getTicket();
        ticketService.setRequest(id);
        Notification notification = new Notification();
        notification.setRequest(true);
        notification.setTicket(ticket);
        notification.setUser(currentUser);
        notification.setDate(new Date());
        notificationService.saveNotification(notification);
        messageService.saveMessage(message);
        return message;
    }

}
