package com.piotrdomagalski.planning.carrier;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarrierRepository extends JpaRepository<CarrierEntity, Long> {

    Optional<CarrierEntity> findBySap(String sap);


}
