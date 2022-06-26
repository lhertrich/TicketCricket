package de.hohenheim.ticketcricket.model.service;

import de.hohenheim.ticketcricket.model.entity.Notification;
import de.hohenheim.ticketcricket.model.entity.User;
import de.hohenheim.ticketcricket.model.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    private Date today = new Date(System.currentTimeMillis());

    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public List<Notification> findAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        Collections.sort(notifications, new Comparator<Notification>() {
            @Override
            public int compare(Notification not1, Notification not2) {
                return not2.getDate().compareTo(not1.getDate());
            }
        });
        return notifications;
    }

    public List<Notification> findAllCurrentNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        List<Notification> currentNotifications = new ArrayList<>();
        for (Notification notification : notifications){
            if(today.getTime()-notification.getDate().getTime() <= 1000*60*60*24*7){
                currentNotifications.add(notification);
            }
        }
        Collections.sort(currentNotifications, new Comparator<Notification>() {
            @Override
            public int compare(Notification not1, Notification not2) {
                return not2.getDate().compareTo(not1.getDate());
            }
        });
        return currentNotifications;
    }

    public List<Notification> findAllOldNotifications(){
        List<Notification> allOldNotifications = findAllNotifications();
        List<Notification> allCurrentNotifications = findAllCurrentNotifications();
        allOldNotifications.removeAll(allCurrentNotifications);
        return allOldNotifications;
    }


    public List<Notification> findAllNotificationsForTicket(int ticketId){
        List<Notification> allNotifications = findAllNotifications();
        List<Notification> ticketNotifications = new ArrayList<>();
        for (Notification notification : allNotifications){
            if (notification.getTicket().getTicketID()==ticketId){
                ticketNotifications.add(notification);
            }
        }
        return ticketNotifications;
    }

    public List<Notification> findAllNotificationsForUser(User user){
        List<Notification> allNotifications = findAllNotifications();
        List<Notification> userNotifications = new ArrayList<>();
        for (Notification notification : allNotifications){
            if(notification.getUser().equals(user)){
                userNotifications.add(notification);
            }
        }
        Collections.sort(userNotifications, new Comparator<Notification>() {
            @Override
            public int compare(Notification not1, Notification not2) {
                return not2.getDate().compareTo(not1.getDate());
            }
        });
        return userNotifications;
    }

    public List<Notification> findAllCurrentNotificatinsForUser(User user){
        List<Notification> allNotifications = findAllNotifications();
        List<Notification> currentUserNotifications = new ArrayList<>();
        for (Notification notification : allNotifications){
            if(notification.getUser().equals(user) && (today.getTime()-notification.getDate().getTime() <= 1000*60*60*24*7)){
                currentUserNotifications.add(notification);
            }
        }
        Collections.sort(currentUserNotifications, new Comparator<Notification>() {
            @Override
            public int compare(Notification not1, Notification not2) {
                return not2.getDate().compareTo(not1.getDate());
            }
        });
        return currentUserNotifications;
    }

    public List<Notification> findAllOldNotficiationsForUser(User user){
        List<Notification> allNotifications = findAllNotificationsForUser(user);
        List<Notification> currentNotifications = findAllCurrentNotificatinsForUser(user);
        allNotifications.removeAll(currentNotifications);
        return allNotifications;
    }
}
