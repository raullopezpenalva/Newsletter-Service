package com.raullopezpenalva.newsletter_service.repository;

import com.raullopezpenalva.newsletter_service.model.Subscriber;
import com.raullopezpenalva.newsletter_service.model.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, UUID> {
    // Custom query methods (if needed) can be defined here
    Optional<Subscriber> findByEmail(String email);
    Optional<Subscriber> findByEmailAndStatus(String email, SubscriptionStatus status);

    @Modifying
    @Transactional
    @Query ("update Subscriber s set s.status = ACTIVE where s.id = :id")
    int activateSubscriber(@Param("id") UUID id);
}