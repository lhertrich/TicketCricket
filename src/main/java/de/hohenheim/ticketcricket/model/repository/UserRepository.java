package de.hohenheim.ticketcricket.model.repository;

import de.hohenheim.ticketcricket.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
