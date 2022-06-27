package de.hohenheim.ticketcricket.model.service;

import de.hohenheim.ticketcricket.model.entity.Notification;
import de.hohenheim.ticketcricket.model.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public List<Notification> findAllNotifications() {
        return notificationRepository.findAll();
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
}
