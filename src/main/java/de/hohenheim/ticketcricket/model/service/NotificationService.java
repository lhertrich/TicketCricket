package de.hohenheim.ticketcricket.model.service;

import de.hohenheim.ticketcricket.model.entity.Notification;
import de.hohenheim.ticketcricket.model.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
