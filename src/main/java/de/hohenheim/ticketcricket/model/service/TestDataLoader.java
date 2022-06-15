package de.hohenheim.ticketcricket.model.service;

import de.hohenheim.ticketcricket.model.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class TestDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(TestDataLoader.class);

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private MessageService messageService;

    /**
     * Diese Methode wird zum Aufsetzen von Testdaten für die Datenbank verwendet werden. Die Methode wird immer dann
     * ausgeführt, wenn der Spring Kontext initialisiert wurde, d.h. wenn Sie Ihren Server (neu-)starten.
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("Initialisiere Datenbank mit Testdaten...");

        // Initialisieren Sie Beispielobjekte und speichern Sie diese über Ihre Services
        Role userRole = new Role("ROLE_USER");
        Role adminRole = new Role("ROLE_ADMIN");
        roleService.saveRole(userRole);
        roleService.saveRole(adminRole);

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);

        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);

        User normalUser = new User();
        normalUser.setUsername("user");
        normalUser.setPassword(passwordEncoder.encode("1234"));
        normalUser.setRoles(userRoles);
        userService.saveUser(normalUser);

        User normalUser2 = new User();
        normalUser2.setUsername("user2");
        normalUser2.setPassword(passwordEncoder.encode("1234"));
        normalUser2.setRoles(userRoles);
        userService.saveUser(normalUser2);

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRoles(adminRoles);
        userService.saveUser(admin);

        Ticket ticket = new Ticket();
        ticket.setCategory(Category.INAKTIVITÄT);
        ticket.setUser(normalUser);
        long d = System.currentTimeMillis();
        ticket.setDate(new Date(d));
        ticket.setLastRequest(new Date(d));
        ticket.setProblem("Darko antwortet nicht");
        ticket.setStatus(Status.OFFEN);
        ticket.setTitle("Darko antwortet nicht");
        ticketService.saveTicket(ticket);

        Ticket ticket2 = new Ticket();
        ticket2.setCategory(Category.SONSTIGES);
        ticket2.setUser(normalUser);
        long d2 = System.currentTimeMillis();
        ticket2.setDate(new Date(d2));
        ticket2.setLastRequest(new Date(d2));
        ticket2.setProblem("Ihr seid nicht so schön");
        ticket2.setStatus(Status.IN_BEARBEITUNG);
        ticket2.setTitle("Ihr seid nicht so schön");
        ticketService.saveTicket(ticket2);

        Ticket ticket3 = new Ticket();
        ticket3.setCategory(Category.TECHNISCHE_PROBLEME);
        ticket3.setUser(normalUser);
        long d3 = System.currentTimeMillis();
        ticket3.setDate(new Date(d3));
        ticket3.setLastRequest(new Date(d3));
        ticket3.setProblem("Technisches Problem gefunden");
        ticket3.setStatus(Status.ERLEDIGT);
        ticket3.setTitle("Technisches Problem gefunden");
        ticketService.saveTicket(ticket3);

        Ticket ticket4 = new Ticket();
        ticket4.setCategory(Category.INAKTIVITÄT);
        ticket4.setUser(normalUser2);
        long d4 = System.currentTimeMillis();
        ticket4.setDate(new Date(d4));
        ticket4.setLastRequest(new Date(d4));
        ticket4.setProblem("Darko antwortet nicht schnell genug. Ich finde, dass das nicht in Ordnung ist.");
        ticket4.setStatus(Status.OFFEN);
        ticket4.setTitle("Darko antwortet nicht schnell genug");
        ticketService.saveTicket(ticket4);

        Ticket ticket5 = new Ticket();
        ticket5.setCategory(Category.INAKTIVITÄT);
        ticket5.setUser(admin);
        long d5 = System.currentTimeMillis();
        ticket5.setDate(new Date(d5));
        ticket5.setLastRequest(new Date(d5));
        ticket5.setProblem("Darko antwortet nicht schnell genug. Ich finde, dass das nicht in Ordnung ist.");
        ticket5.setStatus(Status.OFFEN);
        ticket5.setTitle("Admin test");
        ticketService.saveTicket(ticket5);

        Message message = new Message();
        message.setMessage("Darko wollte mir sein Fahrrad für 100€ verkaufen, ich habe 3€ geboten und jetzt antwortet er nicht mehr");
        message.setUser(normalUser);
        message.setTicket(ticket);
        messageService.saveMessage(message);
    }
}
