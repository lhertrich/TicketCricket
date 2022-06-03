package de.hohenheim.ticketcricket.model.entity;

import javax.persistence.*;

@Entity
public class Message {

    @Id
    @GeneratedValue
    private int messageId;

    private String message;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_ID")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "originTicket_ID")
    private Ticket ticket;

    public Message(){
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
}
