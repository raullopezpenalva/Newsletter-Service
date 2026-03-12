package com.raullopezpenalva.newsletter_service.modules.platform.tokens.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.raullopezpenalva.newsletter_service.modules.platform.tokens.domain.TokenType;
import com.raullopezpenalva.newsletter_service.modules.platform.tokens.domain.VerificationToken;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {
    Optional<VerificationToken> findByToken(String token);
    List<VerificationToken> findBySubscriberIdAndType(UUID subscriberId, TokenType type);

    @Modifying
    @Transactional
    @Query("update VerificationToken t set t.used = true where t.id = :id")
    int markUsed(@Param("id") UUID id);
}