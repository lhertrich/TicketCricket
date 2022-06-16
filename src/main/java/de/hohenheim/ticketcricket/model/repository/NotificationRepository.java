package de.hohenheim.ticketcricket.model.repository;

import de.hohenheim.ticketcricket.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
