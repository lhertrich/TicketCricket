package de.hohenheim.ticketcricket.controller;

import de.hohenheim.ticketcricket.model.entity.*;
import de.hohenheim.ticketcricket.model.service.NotificationService;
import de.hohenheim.ticketcricket.model.service.TicketService;
import de.hohenheim.ticketcricket.model.service.UserService;
import de.hohenheim.ticketcricket.model.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private NotificationService notificationService;

    @InitBinder("ticket")
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(ticketValidator);
    }

    @GetMapping("/ticket-form")
    public String showTicketForm(Model model) {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("admins", userService.getAdmins());
        model.addAttribute("currentNotifications", notificationService.findAllCurrentNotificationsForUser(currentUser));
        model.addAttribute("oldNotifications", notificationService.findAllOldNotificationsForUser(currentUser));
        model.addAttribute("newNotifications", notificationService.findAllNewNotificationsForUser(currentUser));
        return "ticketerstellung";
    }

    @PostMapping("/create-ticket")
    public String createTicket(@Valid @ModelAttribute("ticket") Ticket ticket, BindingResult result, Model model){
        User currentUser = userService.getCurrentUser();
        if(result.hasErrors()){
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("admins", userService.getAdmins());
            model.addAttribute("ticket", ticket);
            model.addAttribute("currentNotifications", notificationService.findAllCurrentNotificationsForUser(currentUser));
            model.addAttribute("oldNotifications", notificationService.findAllOldNotificationsForUser(currentUser));
            model.addAttribute("newNotifications", notificationService.findAllNewNotificationsForUser(currentUser));
            return "ticketerstellung";
        }
        ticket.setStatus(Status.OFFEN);
        ticket.setUser(userService.getCurrentUser());
        ticket.setDate(new Date());
        ticket.setLastRequest(new Date());
        ticket.setPriority(prioritiseTicket(ticket));
        if(ticket.getAdmin() == null){
            ticket.setAdmin(assignAdmin(ticket));
        }
        model.addAttribute("currentUser", currentUser);
        if (currentUser.isAllowed()) {
            ticketService.saveTicket(ticket);
        }
        return "redirect:/";
    }

    private Priority prioritiseTicket(Ticket ticket){
        if(ticket.getCategory() == Category.TECHNISCHE_PROBLEME){
            return Priority.SEHR_WICHTIG;
        } else if(ticket.getCategory() == Category.INAKTIVITÄT){
            return Priority.WICHTIG;
        } else {
            return Priority.UNWICHTIG;
        }
    }

    private User assignAdmin(Ticket ticket){
        List<User> capableAdmins = userService.getAdminsByCategory(ticket.getCategory());
        User assignedAdmin = capableAdmins.get(0);
        int minNumberOfTickets = ticketService.findAllTicketsForAdmin(assignedAdmin).size();
        for(User admin : capableAdmins){
            if(ticketService.findAllTicketsForAdmin(admin).size() < minNumberOfTickets){
                assignedAdmin = admin;
                minNumberOfTickets = ticketService.findAllTicketsForAdmin(admin).size();
            }
        }
        return assignedAdmin;
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
        model.addAttribute("currentNotifications", notificationService.findAllCurrentNotificationsForUser(userService.getCurrentUser()));
        model.addAttribute("oldNotifications", notificationService.findAllOldNotificationsForUser(userService.getCurrentUser()));
        model.addAttribute("newNotifications", notificationService.findAllNewNotificationsForUser(userService.getCurrentUser()));
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
        model.addAttribute("currentNotifications", notificationService.findAllCurrentNotificationsForUser(userService.getCurrentUser()));
        model.addAttribute("oldNotifications", notificationService.findAllOldNotificationsForUser(userService.getCurrentUser()));
        model.addAttribute("newNotifications", notificationService.findAllNewNotificationsForUser(userService.getCurrentUser()));
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
        model.addAttribute("currentNotifications", notificationService.findAllCurrentNotificationsForUser(userService.getCurrentUser()));
        model.addAttribute("oldNotifications", notificationService.findAllOldNotificationsForUser(userService.getCurrentUser()));
        model.addAttribute("newNotifications", notificationService.findAllNewNotificationsForUser(userService.getCurrentUser()));
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
        model.addAttribute("currentNotifications", notificationService.findAllCurrentNotificationsForUser(userService.getCurrentUser()));
        model.addAttribute("oldNotifications", notificationService.findAllOldNotificationsForUser(userService.getCurrentUser()));
        model.addAttribute("newNotifications", notificationService.findAllNewNotificationsForUser(userService.getCurrentUser()));
        model.addAttribute("currentUser", userService.getCurrentUser());
        model.addAttribute("users", roleUserList);
        return "ticketerstellung-disabled";
    }

    @GetMapping("/ajax/updateTicketerstellung{searchString}")
    @ResponseStatus(value = HttpStatus.OK)
    public String updateUser(Model model, @RequestParam("searchString") String searchString){

        model.addAttribute("users", userService.findAllUserByUsername(searchString));
        return "ticketerstellung-verwalten :: #innerWindowUsers";
    }

    @GetMapping("/ajax/updateTicketerstellungDisabled{searchString}")
    @ResponseStatus(value = HttpStatus.OK)
    public String updateUserDisabled(Model model, @RequestParam("searchString") String searchString){

        model.addAttribute("users", userService.findAllUserByUsername(searchString));
        return "ticketerstellung-disabled :: #innerWindowUsers";
    }

    @GetMapping("/ticket/load-tickets")
    @ResponseBody
    public List<Ticket> loadTickets(Model model) {
        model.addAttribute("tickets", ticketService.findAllTickets());
        List<Ticket> ticketList = ticketService.findAllTickets();
        return ticketList;
    }
}