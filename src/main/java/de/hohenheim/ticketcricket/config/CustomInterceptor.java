package de.hohenheim.ticketcricket.config;

import de.hohenheim.ticketcricket.controller.SseController;
import de.hohenheim.ticketcricket.model.entity.Notification;
import de.hohenheim.ticketcricket.model.entity.Ticket;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.Serializable;

public class CustomInterceptor extends EmptyInterceptor {

    @Override
    public boolean onSave(Object entity, Serializable id,
                          Object[] state, String[] propertyNames, Type[] types) {
        if (entity instanceof Notification) {
            Notification notification = (Notification) entity;
            SseEmitters sseEmitters = SseController.getNotificationEmitters();
            sseEmitters.send(notification);
        }
        if (entity instanceof Ticket) {
            Ticket ticket = (Ticket) entity;
            SseEmitters sseEmitters = SseController.getTicketEmitters();
            sseEmitters.send(ticket);
        }

        return super.onSave(entity, id, state, propertyNames, types);
    }

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (entity instanceof Ticket) {
            Ticket ticket = (Ticket) entity;
            SseEmitters sseEmitters = SseController.getTicketEmitters();
            sseEmitters.send(ticket);
        }
    }
}
