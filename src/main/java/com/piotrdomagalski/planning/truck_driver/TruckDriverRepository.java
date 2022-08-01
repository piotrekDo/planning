package com.piotrdomagalski.planning.truck_driver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TruckDriverRepository extends JpaRepository<TruckDriverEntity, Long> {
}
