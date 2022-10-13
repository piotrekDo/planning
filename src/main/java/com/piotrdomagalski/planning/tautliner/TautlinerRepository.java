package com.piotrdomagalski.planning.tautliner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository class for tautliners.
 */

@Repository
public interface TautlinerRepository extends JpaRepository<TautlinerEntity, Long> {

    Optional<TautlinerEntity> findByTautlinerPlatesIgnoreCase(String plates);

}
