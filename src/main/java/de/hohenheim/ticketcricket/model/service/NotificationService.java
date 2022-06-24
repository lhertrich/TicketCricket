package de.hohenheim.ticketcricket.model.service;

import de.hohenheim.ticketcricket.model.entity.Notification;
import de.hohenheim.ticketcricket.model.entity.User;
import de.hohenheim.ticketcricket.model.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

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
}
