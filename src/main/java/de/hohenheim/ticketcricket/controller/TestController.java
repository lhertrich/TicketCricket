package de.hohenheim.ticketcricket.controller;

import de.hohenheim.ticketcricket.model.entity.TestUser;
import de.hohenheim.ticketcricket.model.service.TestUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TestController {

    @Autowired
    private TestUserService service;

    @PostMapping ("/input")
    public String getNewUser(@ModelAttribute("user") TestUser user){
        service.saveUser(user);
        return "pageTwo";
    }

    @GetMapping("/input")
    public String showUsers(Model model){
        System.out.println("getMapping wird aufgerufen");
        model.addAttribute("testUser", service.findAllUsers());
        return "pageTwo";
    }
}
