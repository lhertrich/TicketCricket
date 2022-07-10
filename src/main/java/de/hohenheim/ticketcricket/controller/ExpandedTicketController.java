package de.hohenheim.ticketcricket.controller;

import de.hohenheim.ticketcricket.model.entity.Message;
import de.hohenheim.ticketcricket.model.entity.Notification;
import de.hohenheim.ticketcricket.model.entity.Role;
import de.hohenheim.ticketcricket.model.entity.User;
import de.hohenheim.ticketcricket.model.service.MessageService;
import de.hohenheim.ticketcricket.model.service.NotificationService;
import de.hohenheim.ticketcricket.model.service.TicketService;
import de.hohenheim.ticketcricket.model.service.UserService;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

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
        model.addAttribute("notifications", notificationService.findAllNotificationsForTicket(id));
        List<Message> allMessages = messageService.findAllMessages();
        List<Message> messages = allMessages.stream().filter(m -> m.getTicket().getTicketID() == id).collect(java.util.stream.Collectors.toList());
        model.addAttribute("messages", messages);
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("compareDate", new Date(System.currentTimeMillis() - (60000 * 60 * 12)));
        return "expanded-ticket";
    }

    @PostMapping("/ticket/delete{id}")
    public String deleteTicket(@RequestParam("id") Integer id) {
        ticketService.deleteTicket(ticketService.findTicketById(id));
        return "redirect:/";
    }



    @PostMapping(value="/ticket/send-message{id}", produces = "application/json")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public Message sendMessage(@RequestParam("id") String id, @RequestBody Message message) {
        messageService.saveMessage(message);
        return message;
    }

    @GetMapping("/ticket/load-messages{id}")
    @ResponseBody
    public List<Message> loadMessages(@RequestParam("id") Integer id) {
        List<Message> messages = messageService.findAllMessages();
        List<Message> filteredMessages = messages.stream().filter(m -> m.getTicket().getTicketID() == id).collect(java.util.stream.Collectors.toList());
        return filteredMessages;
    }

}
