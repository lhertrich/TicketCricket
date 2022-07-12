package de.hohenheim.ticketcricket.model.service;

import de.hohenheim.ticketcricket.model.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        Role adminRoleInaktivität = new Role("ROLE_"+Category.INAKTIVITÄT);
        Role adminRoleTechnisch = new Role("ROLE_"+Category.TECHNISCHE_PROBLEME);
        Role adminRoleSonstiges = new Role("ROLE_"+Category.SONSTIGES);
        roleService.saveRole(userRole);
        roleService.saveRole(adminRole);
        roleService.saveRole(adminRoleInaktivität);
        roleService.saveRole(adminRoleTechnisch);
        roleService.saveRole(adminRoleSonstiges);

        Set<User> allBookmarked = new HashSet<>(userService.findAllUsers());
        Set<User> bookmarkedTicket8 = new HashSet<>();
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);

        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);

        User normalUser1 = new User();
        normalUser1.setUsername("user1");
        normalUser1.setPassword(passwordEncoder.encode("user1"));
        normalUser1.setRoles(userRoles);
        normalUser1.setAllowed(true);
        normalUser1.setAllowedGeneral(true);
        userService.saveUser(normalUser1);

        User normalUser2 = new User();
        normalUser2.setUsername("user2");
        normalUser2.setPassword(passwordEncoder.encode("user2"));
        normalUser2.setRoles(userRoles);
        normalUser2.setAllowed(true);
        normalUser2.setAllowedGeneral(true);
        userService.saveUser(normalUser2);

        User normalUser3 = new User();
        normalUser3.setUsername("user3");
        normalUser3.setPassword(passwordEncoder.encode("user3"));
        normalUser3.setRoles(userRoles);
        normalUser3.setAllowed(true);
        normalUser3.setAllowedGeneral(true);
        userService.saveUser(normalUser3);

        User admin1 = new User();
        admin1.setUsername("admin1");
        admin1.setPassword(passwordEncoder.encode("admin1"));
        admin1.setRoles(new HashSet<>(Arrays.asList(adminRole, adminRoleInaktivität)));
        admin1.setAllowed(true);
        admin1.setAllowedGeneral(true);
        userService.saveUser(admin1);

        User admin2 = new User();
        admin2.setUsername("admin2");
        admin2.setPassword(passwordEncoder.encode("admin2"));
        admin2.setRoles(new HashSet<>(Arrays.asList(adminRole, adminRoleTechnisch)));
        admin2.setAllowed(true);
        admin2.setAllowedGeneral(true);
        userService.saveUser(admin2);

        User admin3 = new User();
        admin3.setUsername("Peter Admin");
        admin3.setPassword(passwordEncoder.encode("admin3"));
        admin3.setRoles(new HashSet<>(Arrays.asList(adminRole, adminRoleSonstiges)));
        admin3.setAllowed(true);
        admin3.setAllowedGeneral(true);
        userService.saveUser(admin3);

        bookmarkedTicket8.add(admin1);
        bookmarkedTicket8.add(admin2);

        Ticket ticket = new Ticket();
        ticket.setCategory(Category.TECHNISCHE_PROBLEME);
        ticket.setUser(normalUser1);
        long du = System.currentTimeMillis() ;
        ticket.setDate(new Date(du-50400000));
        ticket.setLastRequest(new Date(du-46800000));
        ticket.setProblem("Bei mir erscheint ein Black Screen wenn ich einen Artikel inseriere!");
        ticket.setStatus(Status.OFFEN);
        ticket.setTitle("Black Screen");
        ticket.setPriority(Priority.MITTEL);
        ticket.setAdmin(admin1);
        ticket.setBookmark(allBookmarked);
        ticketService.saveTicket(ticket);

        Ticket ticket1 = new Ticket();
        ticket1.setCategory(Category.TECHNISCHE_PROBLEME);
        ticket1.setUser(normalUser1);
        long d = System.currentTimeMillis();
        ticket1.setDate(new Date(d-50400000));
        ticket1.setLastRequest(new Date(d-46800000));
        ticket1.setProblem("Ich kann keine Artikel suchen!");
        ticket1.setStatus(Status.IN_BEARBEITUNG);
        ticket1.setTitle("Artikelsuche");
        ticket1.setPriority(Priority.MITTEL);
        ticket1.setAdmin(admin1);
        ticket1.setBookmark(allBookmarked);
        ticketService.saveTicket(ticket1);

        Ticket ticket2 = new Ticket();
        ticket2.setCategory(Category.INAKTIVITÄT);
        ticket2.setUser(normalUser1);
        long d2 = System.currentTimeMillis();
        ticket2.setDate(new Date(d2-50400000));
        ticket2.setLastRequest(new Date(d2-46800000));
        ticket2.setProblem("Der User LukasB antwortet mir wiederholt nicht auf meine Messages");
        ticket2.setStatus(Status.OFFEN);
        ticket2.setTitle("User LukasB antwortet nicht");
        ticket2.setPriority(Priority.MITTEL);
        ticket2.setAdmin(admin1);
        ticket2.setBookmark(new HashSet<>());
        ticketService.saveTicket(ticket2);

        Ticket ticket3 = new Ticket();
        ticket3.setCategory(Category.SONSTIGES);
        ticket3.setUser(normalUser1);
        long d3 = System.currentTimeMillis();
        ticket3.setDate(new Date(d3));
        ticket3.setLastRequest(new Date());
        ticket3.setProblem("User SilasScheu hat mich mehrfach beleidigt im Chat");
        ticket3.setStatus(Status.OFFEN);
        ticket3.setTitle("Beleidigung in Direct Message");
        ticket3.setPriority(Priority.MITTEL);
        ticket3.setAdmin(admin1);
        ticket3.setBookmark(new HashSet<>());
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
        ticket4.setPriority(Priority.MITTEL);
        ticket4.setAdmin(admin1);
        ticket4.setBookmark(new HashSet<>());
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
        ticket5.setPriority(Priority.MITTEL);
        ticket5.setAdmin(admin1);
        ticket5.setBookmark(allBookmarked);
        ticketService.saveTicket(ticket5);

        Ticket ticket6 = new Ticket();
        ticket6.setCategory(Category.TECHNISCHE_PROBLEME);
        ticket6.setUser(normalUser1);
        long d6 = System.currentTimeMillis();
        ticket6.setDate(new Date(d6 - 200000000));
        ticket6.setLastRequest(new Date(d6));
        ticket6.setProblem("Der Impressum-Button kann nicht gedrückt werden");
        ticket6.setStatus(Status.OFFEN);
        ticket6.setTitle("Button funktioniert nicht");
        ticket6.setPriority(Priority.MITTEL);
        ticket6.setAdmin(admin1);
        ticket6.setBookmark(new HashSet<>());
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
        ticket7.setPriority(Priority.MITTEL);
        ticket7.setAdmin(admin1);
        ticket7.setBookmark(allBookmarked);
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
        ticket8.setPriority(Priority.MITTEL);
        ticket8.setAdmin(admin1);
        ticket8.setBookmark(bookmarkedTicket8);
        ticketService.saveTicket(ticket8);

        Ticket ticket9 = new Ticket();
        ticket9.setCategory(Category.TECHNISCHE_PROBLEME);
        ticket9.setUser(normalUser2);
        long d9 = System.currentTimeMillis();
        ticket9.setDate(new Date(d9-1000000000));
        ticket9.setLastRequest(new Date(d9));
        ticket9.setProblem("Wenn ich einen Artikel inseriere crasht die Seite oft.");
        ticket9.setStatus(Status.ERLEDIGT);
        ticket9.setTitle("Seite crasht beim Inserieren");
        ticket9.setPriority(Priority.MITTEL);
        ticket9.setAdmin(admin1);
        ticket9.setBookmark(allBookmarked);
        ticketService.saveTicket(ticket9);

        Ticket ticket10 = new Ticket();
        ticket10.setCategory(Category.TECHNISCHE_PROBLEME);
        ticket10.setUser(admin1);
        long d10 = System.currentTimeMillis();
        ticket10.setDate(new Date(d10-500000000));
        ticket10.setLastRequest(new Date(d10));
        ticket10.setProblem("Home Button in der Navbar funktioniert nicht");
        ticket10.setStatus(Status.OFFEN);
        ticket10.setTitle("Home Button Funktion");
        ticket10.setPriority(Priority.MITTEL);
        ticket10.setAdmin(admin1);
        ticket10.setBookmark(new HashSet<>());
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
        ticket11.setPriority(Priority.MITTEL);
        ticket11.setAdmin(admin1);
        ticket11.setBookmark(new HashSet<>());
        ticketService.saveTicket(ticket11);

        Notification notification1 = new Notification();
        notification1.setUser(normalUser1);
        notification1.setTicket(ticket3);
        long nd1 = System.currentTimeMillis();
        notification1.setDate(new Date(nd1));
        notification1.setNotificationType(NotificationType.STATUS_ANFRAGE);
        notificationService.saveNotification(notification1);

        Notification notification2 = new Notification();
        notification2.setUser(normalUser1);
        notification2.setTicket(ticket4);
        long nd2 = System.currentTimeMillis();
        notification2.setDate(new Date(nd2));
        notification2.setNotificationType(NotificationType.STATUS_ANFRAGE);
        notificationService.saveNotification(notification2);

        Message message1 = new Message();
        message1.setTicket(ticket);
        message1.setUser(normalUser1);
        message1.setDate(new Date(System.currentTimeMillis()));
        message1.setMessage("Test von user1");
        messageService.saveMessage(message1);

        Message message2 = new Message();
        message2.setTicket(ticket);
        message2.setUser(admin1);
        message2.setDate(new Date(System.currentTimeMillis()));
        message2.setMessage("Test von admin1");
        messageService.saveMessage(message2);

       Notification notification3 = new Notification();
       notification3.setUser(admin1);
       notification3.setTicket(ticket4);
       notification3.setDate(new Date());
       notification3.setNew(false);
       notification3.setNotificationType(NotificationType.NACHRICHT);
       notificationService.saveNotification(notification3);

        Notification notification4 = new Notification();
        notification4.setUser(admin1);
        notification4.setTicket(ticket4);
        notification4.setDate(new Date());
        notification4.setNotificationType(NotificationType.NACHRICHT);
        notificationService.saveNotification(notification4);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        String dateInString = "7-Jun-2022";
        String dateInString2 = "4-Jul-2022";
        try {
            Date date1 = formatter.parse(dateInString);
            Date date2 = formatter.parse(dateInString2);

            Notification notification5 = new Notification();
            notification5.setUser(admin1);
            notification5.setTicket(ticket4);
            notification5.setDate(date1);
            notification5.setNotificationType(NotificationType.NACHRICHT);
            notificationService.saveNotification(notification5);

            Notification notification6 = new Notification();
            notification6.setUser(admin1);
            notification6.setTicket(ticket4);
            notification6.setDate(date2);
            notification6.setNotificationType(NotificationType.NACHRICHT);
            notificationService.saveNotification(notification6);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
