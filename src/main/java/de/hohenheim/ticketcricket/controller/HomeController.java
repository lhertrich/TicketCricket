package de.hohenheim.ticketcricket.controller;

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

   @PostMapping("/user")
    public String user(Model model, @RequestParam String uname, @RequestParam String password){
        if(uname.equals("user") && password.equals("user")){
            return "userTicketerstellung";
        }
        model.addAttribute("errorMsg", "Please provide the correct Username and Password");
        return "login";
    }

    @PostMapping("/admin")
    public String admin(){
        return "userTicketerstellung";
    }

}
