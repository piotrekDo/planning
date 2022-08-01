package com.piotrdomagalski.planning.truck;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository class for truck entity.
 */

@Repository
public interface TruckRepository extends JpaRepository<TruckEntity, Long> {

    Optional<TruckEntity> findByTruckPlatesIgnoreCase(String plates);

}
