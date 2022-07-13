package de.hohenheim.ticketcricket.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Notification {

    @Id
    @GeneratedValue
    private int notificationId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creatorID")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ticketID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Ticket ticket;

    private Date date;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private boolean isNew = true;

    public Notification(){}

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", user=" + user.getUsername() +
                ", ticket=" + ticket.getTicketID() +
                ", date=" + date +
                ", notificationType=" + notificationType +
                ", isNew=" + isNew +
                '}';
    }

}
