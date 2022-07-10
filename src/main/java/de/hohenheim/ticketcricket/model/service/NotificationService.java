package de.hohenheim.ticketcricket.model.service;

import de.hohenheim.ticketcricket.model.entity.Notification;
import de.hohenheim.ticketcricket.model.entity.User;
import de.hohenheim.ticketcricket.model.repository.NotificationRepository;
import org.aspectj.weaver.ast.Not;
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

    public Notification findNotificiationById(int id){
        return notificationRepository.getById(id);
    }

    public void deleteNotification(Notification notification){
        notificationRepository.delete(notification);
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

    public List<Notification> findAllNewNotificationsForUser(User user){
        List<Notification> allNotifications = findAllNotifications();
        List<Notification> allNewNotificationsForUser = new ArrayList<>();
        for (Notification notification : allNotifications){
            if (notification.getUser().equals(user) && notification.isNew()){
                allNewNotificationsForUser.add(notification);
            }
        }
        return allNewNotificationsForUser;
    }

    public List<Notification> findAllCurrentNotificationsForUser(User user){
        List<Notification> allNotifications = findAllNotifications();
        List<Notification> currentUserNotifications = new ArrayList<>();
        for (Notification notification : allNotifications){
            if(notification.getUser().equals(user) &&
                    (today.getTime() - notification.getDate().getTime() <= 1000*60*60*24*7) &&
                    !notification.isNew()){
                currentUserNotifications.add(notification);
            }
        }
        return currentUserNotifications;
    }

    public List<Notification> findAllOldNotificationsForUser(User user){
        List<Notification> allNotifications = findAllNotifications();
        List<Notification> allOldNotificationsForUser = new ArrayList<>();
        List<Notification> allNewNotificationsForUser = findAllNewNotificationsForUser(user);
        List<Notification> allCurrentNotificationsForUser = findAllCurrentNotificationsForUser(user);
        for(Notification notification : allNotifications){
            if(notification.getUser().equals(user) &&
                    (!allNewNotificationsForUser.contains(notification)) &&
                    (!allCurrentNotificationsForUser.contains(notification))){
                allOldNotificationsForUser.add(notification);
            }
        }
        return allOldNotificationsForUser;
    }

    public void setNotificationsReadForUser(User user){
        List<Notification> allNotifications = findAllNotifications();
        for (Notification notification : allNotifications){
            if(notification.getUser().equals(user)){
                notification.setNew(false);
                notificationRepository.save(notification);
            }
        }
    }
}
