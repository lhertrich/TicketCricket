package de.hohenheim.ticketcricket.controller;

import de.hohenheim.ticketcricket.model.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class JSController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/ajax/setNotificationsRead")
    @ResponseStatus(value = HttpStatus.OK)
    public void setNotificationsRead(){
        System.out.println("Request erhalten");
    }
}
