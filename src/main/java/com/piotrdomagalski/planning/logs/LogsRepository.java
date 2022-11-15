package com.piotrdomagalski.planning.logs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Logs are designed in the way that they persist even if related object is removed.
 * Therefore, they can't store ID data, but unique property.
 */

@Repository
public interface LogsRepository extends JpaRepository<LogEntity, Long> {

    /**
     * Method used to find logs related to specific truck, tautliner etc.
     *
     * @param identifier differs from database ID e.g. truck plates or carrier SAP
     * @return
     */

    List<LogEntity> findAllByUniqueIdentifierIgnoreCase(String identifier);

}
