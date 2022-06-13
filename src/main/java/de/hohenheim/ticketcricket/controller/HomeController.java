package de.hohenheim.ticketcricket.controller;

import de.hohenheim.ticketcricket.model.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {



    /**
     * Zeigt die Startseite Ihrer Anwendung.
     *
     * @param model enthält alle ModelAttribute.
     * @return home-Seite.
     */
    @GetMapping("/")
    public String showHome(Model model) {
        model.addAttribute("message", "Und hier sehen Sie ein ModelAttribut");
        return "home";
    }


    @GetMapping("/user")
    public String user(Model model){
        model.addAttribute("message", "Und hier sehen Sie ein ModelAttribut");
        return "userTicketerstellung";
    }
    @PostMapping("/user")
    public String storeTicket(Model model){
        model.addAttribute("message", "Und hier sehen Sie ein ModelAttribut");
        // hier müssen noch die Daten gespeichert werden, die eingegeben wurden.
        return "home";
    }



}
