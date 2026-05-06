package com.brais.gymtrack.client;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.brais.gymtrack.user.User;

@Repository
public interface ClientProfileRepository extends JpaRepository<ClientProfile, Long> {
    Optional<ClientProfile> findByUser(User user);

    boolean existsByUser(User user);
}
