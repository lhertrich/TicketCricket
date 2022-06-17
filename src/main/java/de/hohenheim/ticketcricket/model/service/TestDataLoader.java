package de.hohenheim.ticketcricket.model.service;

import de.hohenheim.ticketcricket.model.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
    private NotificationService notificationService;



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

        User normalUser1 = new User();
        normalUser1.setUsername("user1");
        normalUser1.setPassword(passwordEncoder.encode("user1"));
        normalUser1.setRoles(userRoles);
        userService.saveUser(normalUser1);

        User normalUser2 = new User();
        normalUser2.setUsername("user2");
        normalUser2.setPassword(passwordEncoder.encode("user2"));
        normalUser2.setRoles(userRoles);
        userService.saveUser(normalUser2);

        User normalUser3 = new User();
        normalUser3.setUsername("user3");
        normalUser3.setPassword(passwordEncoder.encode("user3"));
        normalUser3.setRoles(userRoles);
        userService.saveUser(normalUser3);

        User admin1 = new User();
        admin1.setUsername("admin");
        admin1.setPassword(passwordEncoder.encode("admin1"));
        admin1.setRoles(adminRoles);
        userService.saveUser(admin1);

        User admin2 = new User();
        admin2.setUsername("admin2");
        admin2.setPassword(passwordEncoder.encode("admin2"));
        admin2.setRoles(adminRoles);
        userService.saveUser(admin2);


        Ticket ticket = new Ticket();
        ticket.setCategory(Category.TECHNISCHE_PROBLEME);
        ticket.setUser(normalUser1);
        long du = System.currentTimeMillis();
        ticket.setDate(new Date(du));
        ticket.setLastRequest(new Date(du-100000000));
        ticket.setProblem("Bei mir erscheint ein Black Screen wenn ich einen Artikel inseriere!");
        ticket.setStatus(Status.OFFEN);
        ticket.setTitle("Black Screen");
        ticketService.saveTicket(ticket);

        Ticket ticket1 = new Ticket();
        ticket1.setCategory(Category.TECHNISCHE_PROBLEME);
        ticket1.setUser(normalUser1);
        long d = System.currentTimeMillis();
        ticket1.setDate(new Date(d));
        ticket1.setLastRequest(new Date(d));
        ticket1.setProblem("Ich kann keine Artikel suchen!");
        ticket1.setStatus(Status.IN_BEARBEITUNG);
        ticket1.setTitle("Artikelsuche");
        ticketService.saveTicket(ticket1);

        Ticket ticket2 = new Ticket();
        ticket2.setCategory(Category.INAKTIVITÄT);
        ticket2.setUser(normalUser1);
        long d2 = System.currentTimeMillis();
        ticket2.setDate(new Date(d2));
        ticket2.setLastRequest(new Date(d2-100000000));
        ticket2.setProblem("Der User LukasB antwortet mir wiederholt nicht auf meine Messages");
        ticket2.setStatus(Status.OFFEN);
        ticket2.setTitle("User LukasB antwortet nicht");
        ticketService.saveTicket(ticket2);

        Ticket ticket3 = new Ticket();
        ticket3.setCategory(Category.SONSTIGES);
        ticket3.setUser(normalUser1);
        long d3 = System.currentTimeMillis();
        ticket3.setDate(new Date(d3));
        ticket3.setLastRequest(new Date(d3-432000000));
        ticket3.setProblem("User SilasScheu hat mich mehrfach beleidigt im Chat");
        ticket3.setStatus(Status.OFFEN);
        ticket3.setTitle("Beleidigung in Direct Message");
        ticketService.saveTicket(ticket3);

        Ticket ticket4 = new Ticket();
        ticket4.setCategory(Category.SONSTIGES);
        ticket4.setUser(normalUser1);
        long d4 = System.currentTimeMillis();
        ticket4.setDate(new Date(d4));
        ticket4.setLastRequest(new Date(d4));
        ticket4.setProblem("User ErikSaalfeld schickt Spam Links im Chat");
        ticket4.setStatus(Status.ERLEDIGT);
        ticket4.setTitle("Spam Links in Direct Message");
        ticketService.saveTicket(ticket4);

        Ticket ticket5 = new Ticket();
        ticket5.setCategory(Category.TECHNISCHE_PROBLEME);
        ticket5.setUser(normalUser1);
        long d5 = System.currentTimeMillis();
        ticket5.setDate(new Date(d5));
        ticket5.setLastRequest(new Date(d5));
        ticket5.setProblem("Die Seite lädt sehr langsam und funktioniert manchmal nicht");
        ticket5.setStatus(Status.OFFEN);
        ticket5.setTitle("Seite lädt nicht");
        ticketService.saveTicket(ticket5);

        Ticket ticket6 = new Ticket();
        ticket6.setCategory(Category.TECHNISCHE_PROBLEME);
        ticket6.setUser(normalUser1);
        long d6 = System.currentTimeMillis();
        ticket6.setDate(new Date(d6));
        ticket6.setLastRequest(new Date(d6));
        ticket6.setProblem("Der Impressum-Button kann nicht gedrückt werden");
        ticket6.setStatus(Status.OFFEN);
        ticket6.setTitle("Button funktioniert nicht");
        ticketService.saveTicket(ticket6);

        Ticket ticket7 = new Ticket();
        ticket7.setCategory(Category.INAKTIVITÄT);
        ticket7.setUser(normalUser2);
        long d7 = System.currentTimeMillis();
        ticket7.setDate(new Date(d7));
        ticket7.setLastRequest(new Date(d7));
        ticket7.setProblem("User MoritzKöhler antwortet nicht auf Messages obwohl er einem Verkauf schon zugestimmt hat");
        ticket7.setStatus(Status.ERLEDIGT);
        ticket7.setTitle("User antwortet nicht");
        ticketService.saveTicket(ticket7);

        Ticket ticket8 = new Ticket();
        ticket8.setCategory(Category.INAKTIVITÄT);
        ticket8.setUser(normalUser2);
        long d8 = System.currentTimeMillis();
        ticket8.setDate(new Date(d8));
        ticket8.setLastRequest(new Date(d8));
        ticket8.setProblem("User DominikRau hat eingewilligt mir sein Hantelset zu verkaufen aber nach wiederholtem Anschreiben meldet" +
                "er sich nicht mehr");
        ticket8.setStatus(Status.IN_BEARBEITUNG);
        ticket8.setTitle("User antwortet nicht Verkauf");
        ticketService.saveTicket(ticket8);

        Ticket ticket9 = new Ticket();
        ticket9.setCategory(Category.TECHNISCHE_PROBLEME);
        ticket9.setUser(normalUser2);
        long d9 = System.currentTimeMillis();
        ticket9.setDate(new Date(d9));
        ticket9.setLastRequest(new Date(d9));
        ticket9.setProblem("Wenn ich einen Artikel inseriere crasht die Seite oft.");
        ticket9.setStatus(Status.ERLEDIGT);
        ticket9.setTitle("Seite crasht beim Inserieren");
        ticketService.saveTicket(ticket9);

        Ticket ticket10 = new Ticket();
        ticket10.setCategory(Category.TECHNISCHE_PROBLEME);
        ticket10.setUser(admin1);
        long d10 = System.currentTimeMillis();
        ticket10.setDate(new Date(d10));
        ticket10.setLastRequest(new Date(d10));
        ticket10.setProblem("Home Button in der Navbar funktioniert nicht");
        ticket10.setStatus(Status.OFFEN);
        ticket10.setTitle("Home Button Funktion");
        ticketService.saveTicket(ticket10);

        Ticket ticket11 = new Ticket();
        ticket11.setCategory(Category.SONSTIGES);
        ticket11.setUser(admin2);
        long d11 = System.currentTimeMillis();
        ticket11.setDate(new Date(d11));
        ticket11.setLastRequest(new Date(d11));
        ticket11.setProblem("Viele User verbreiten Fake Artikel");
        ticket11.setStatus(Status.IN_BEARBEITUNG);
        ticket11.setTitle("Fake Artikel");
        ticketService.saveTicket(ticket11);


        Notification notification1 = new Notification();
        notification1.setUser(normalUser1);
        notification1.setTicket(ticket1);
        long nd1 = System.currentTimeMillis();
        notification1.setDate(new Date(nd1));
        notification1.setRequest(true);
        notificationService.saveNotification(notification1);

        Notification notification2 = new Notification();
        notification2.setUser(normalUser1);
        notification2.setTicket(ticket2);
        long nd2 = System.currentTimeMillis();
        notification2.setDate(new Date(nd2));
        notification2.setRequest(true);
        notificationService.saveNotification(notification2);
    }
}
