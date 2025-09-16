package com.raullopezpenalva.newsletter_service.repository;

import com.raullopezpenalva.newsletter_service.model.VerificationToken;
import com.raullopezpenalva.newsletter_service.model.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {
    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findBySubscriberIdAndType(UUID subscriberId, TokenType type);

    @Modifying
    @Transactional
    @Query("update VerificationToken t set t.used = true where t.id = :id")
    int markUsed(@Param("id") UUID id);
}