package de.hohenheim.ticketcricket.controller;

import de.hohenheim.ticketcricket.model.entity.*;
import de.hohenheim.ticketcricket.model.service.TicketService;
import de.hohenheim.ticketcricket.model.service.UserService;
import de.hohenheim.ticketcricket.model.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private TicketValidator ticketValidator;

    @InitBinder("ticket")
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(ticketValidator);
    }

    @GetMapping("/ticket-form")
    public String showTicketForm(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("currentUser", user);
        model.addAttribute("ticket", new Ticket());
        return "ticketerstellung";
    }

    @PostMapping("/create-ticket")
    public String createTicket(@Valid @ModelAttribute("ticket") Ticket ticket, BindingResult result, Model model) {
        User user = userService.getCurrentUser();
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("ticket", ticket);
            return "ticketerstellung";
        }
        ticket.setStatus(Status.OFFEN);
        ticket.setUser(userService.getCurrentUser());
        Date currentDate = new Date(System.currentTimeMillis());
        ticket.setDate(currentDate);
        ticket.setLastRequest(currentDate);

        if (ticket.getCategory() == Category.INAKTIVITÄT) {
            ticket.setPriority(Priority.WICHTIG);
        } else if (ticket.getCategory() == Category.TECHNISCHE_PROBLEME) {
            ticket.setPriority(Priority.SEHR_WICHTIG);
        } else {
            ticket.setPriority(Priority.UNWICHTIG);
        }
        if (user.isAllowed()) {
            ticketService.saveTicket(ticket);
        }

        return "redirect:/";
    }

    @GetMapping("/ticket-creation-managing")
    public String showTicketcreationManagement(Model model) {
        List<User> roleUserList = new ArrayList<>();
        List<User> userList = userService.findAllUsers();
        for (User user : userList) {
            Set<Role> roles = user.getRoles();
            for (Role role : roles) {
                if (role.getRolename().equals("ROLE_USER")) {
                    roleUserList.add(user);
                }
            }
        }

        model.addAttribute("currentUser", userService.getCurrentUser());
        model.addAttribute("users", roleUserList);
        return "ticketerstellung-verwalten";
    }

    @PostMapping("/ticket-creation-toggle{id}")
    public String ticketCreationManaging(@RequestParam("id") int id) {
        Optional<User> user = userService.getUserByID(id);
        if (!user.isEmpty()) {
            if (user.get().isAllowed()) {
                user.get().setAllowed(false);
            } else {
                user.get().setAllowed(true);
            }
            userService.saveUser(user.get());


        }
        return "redirect:/ticket-creation-managing";

    }

    @PostMapping("/ticket-creation-everybody")
    public String ticketCreationEverybody(Model model) {
        List<User> userList = userService.findAllUsers();
        for (User user : userList) {
            user.setAllowedGeneral(false);
            userService.saveUser(user);
        }

        List<User> roleUserList = new ArrayList<>();
        for (User user : userList) {
            Set<Role> roles = user.getRoles();
            for (Role role : roles) {
                if (role.getRolename().equals("ROLE_USER")) {
                    roleUserList.add(user);
                }
            }
        }
        model.addAttribute("currentUser", userService.getCurrentUser());
        model.addAttribute("users", roleUserList);
        return "ticketerstellung-disabled";
    }

    @PostMapping("/ticket-creation-disabled")
    public String ticketCreationDisabled(Model model){
        List<User> userList = userService.findAllUsers();
        for (User user: userList) {
            user.setAllowedGeneral(true);
            userService.saveUser(user);
        }
        List<User> roleUserList = new ArrayList<>();
        for (User user : userList) {
            Set<Role> roles = user.getRoles();
            for (Role role : roles) {
                if (role.getRolename().equals("ROLE_USER")) {
                    roleUserList.add(user);
                }
            }
        }
        model.addAttribute("currentUser", userService.getCurrentUser());
        model.addAttribute("users", roleUserList);
        return "ticketerstellung-verwalten";
    }

    @GetMapping("/ticket-creation-managing-disabled")
    public String showTicketcreationManagementDisabled(Model model) {
        List<User> roleUserList = new ArrayList<>();
        List<User> userList = userService.findAllUsers();
        for (User user : userList) {
            Set<Role> roles = user.getRoles();
            for (Role role : roles) {
                if (role.getRolename().equals("ROLE_USER")) {
                    roleUserList.add(user);
                }
            }
        }

        model.addAttribute("currentUser", userService.getCurrentUser());
        model.addAttribute("users", roleUserList);
        return "ticketerstellung-disabled";
    }

}