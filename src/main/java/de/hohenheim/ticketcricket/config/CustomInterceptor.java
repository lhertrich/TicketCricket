package de.hohenheim.ticketcricket.config;

import de.hohenheim.ticketcricket.controller.SseController;
import de.hohenheim.ticketcricket.model.entity.Notification;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;

public class CustomInterceptor extends EmptyInterceptor {

    @Override
    public boolean onSave(Object entity, Serializable id,
                          Object[] state, String[] propertyNames, Type[] types) {

        if (entity instanceof Notification) {
            Notification notification = (Notification) entity;
            SseEmitters sseEmitters = SseController.getSseEmitters();
            sseEmitters.send(notification);
        }

        return super.onSave(entity, id, state, propertyNames, types);
    }
}
