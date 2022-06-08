package de.hohenheim.ticketcricket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExpandedTicketController {

    /**
     * Zeigt die Startseite Ihrer Anwendung.
     *
     * @param model enthält alle ModelAttribute.
     * @return home-Seite.
     */
    @GetMapping("/expanded-ticket")
    public String showExpandedTicketAdmin(Model model) {
        model.addAttribute("message", "This is an expanded ticket");
        return "expanded-ticket";
    }

}
