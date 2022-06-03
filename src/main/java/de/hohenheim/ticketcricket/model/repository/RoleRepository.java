package de.hohenheim.ticketcricket.model.repository;

import de.hohenheim.ticketcricket.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}