package de.hohenheim.ticketcricket.controller;

import de.hohenheim.ticketcricket.model.entity.Role;
import de.hohenheim.ticketcricket.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Controller
public class DashboardController {

    /**
     * Zeigt die Startseite Ihrer Anwendung.
     *
     * @param model enthält alle ModelAttribute.
     * @return home-Seite.
     */
    @Autowired
    private UserService user;

    @GetMapping("/")
    public String showDashboard(Model model) {
        Set<Role> roles = user.getCurrentUser().getRoles();
        Set<String> rolesString = new HashSet<>();
        for(Role r: roles){
            rolesString.add(r.getRolename());
        }
        if (rolesString.contains("ROLE_ADMIN")){
            return "admin/dashboard";
        }else{
            return "user/dashboard";
        }
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
        return "admin/dashboard";
    }



}
