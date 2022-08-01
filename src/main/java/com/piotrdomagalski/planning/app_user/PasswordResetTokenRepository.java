package com.piotrdomagalski.planning.app_user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA repository for password reset tokens.
 */

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    /**
     *
     * @param appUser method allowing to look for tokens by user
     * @return
     */
    Optional<PasswordResetToken> findByUser(AppUser appUser);

}
