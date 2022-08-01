package com.piotrdomagalski.planning.truck_driver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class for driver entity.
 */

@Repository
public interface TruckDriverRepository extends JpaRepository<TruckDriverEntity, Long> {
}
