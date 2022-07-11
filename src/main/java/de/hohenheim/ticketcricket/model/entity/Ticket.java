package de.hohenheim.ticketcricket.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.aspectj.weaver.ast.Not;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Ticket {

    @Id
    @GeneratedValue
    private int ticketID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creatorID")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "adminID")
    private User admin;

    private String title;

    private String problem;

    @Enumerated(EnumType.STRING)
    private Category category;

    private Date date;

    private Date lastRequest;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> bookmark;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    private boolean viewed;



    public Ticket(){
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(Date lastRequest) {
        this.lastRequest = lastRequest;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public User getAdmin() {return admin;}

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public Set<User> getBookmark() {
        return bookmark;
    }
    public void setBookmark(Set<User> bookmark) {
        this.bookmark = bookmark;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }
}
