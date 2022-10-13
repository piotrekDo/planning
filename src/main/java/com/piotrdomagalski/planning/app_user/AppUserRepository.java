package com.piotrdomagalski.planning.app_user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA repository for app users
 */

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    /**
     *
     * @param username allows looking for a suer by username, ignoring case
     * @return
     */
    Optional<AppUser> findByUsernameIgnoreCase(String username);

    /**
     *
     * @param userEmail allows looking fo a user by emial, ignoring case
     * @return
     */

    Optional<AppUser> findByUserEmailIgnoreCase(String userEmail);
}
