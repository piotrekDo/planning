package com.piotrdomagalski.planning.carrier;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA repository for carrier entity.
 */

@Repository
public interface CarrierRepository extends JpaRepository<CarrierEntity, Long> {

    /**
     * Additional method to search for carriers by their SAP number
     * @param sap
     * @return
     */

    Optional<CarrierEntity> findBySap(String sap);


}
