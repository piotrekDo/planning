package com.piotrdomagalski.planning.tautliner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class TautlinerRepositoryTest {

    @Autowired
    TautlinerRepository tautlinerRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    void find_by_name_should_return_tautliner_if_exists() {
        //given
        TautlinerEntity tautlinerEntity = new TautlinerEntity(true, "IOP963", LocalDateTime.now(), null, null);
        testEntityManager.persist(tautlinerEntity);

        //when
        Optional<TautlinerEntity> result = tautlinerRepository.findByTautlinerPlatesIgnoreCase("IOP963");

        //then
        tautlinerEntity.setId(1L);
        assertEquals(Optional.of(tautlinerEntity), result);
    }

    @Test
    void should_return_optional_empty_if_no_tautliner_with_provided_plates_exists() {
        //when
        Optional<TautlinerEntity> result = tautlinerRepository.findByTautlinerPlatesIgnoreCase("IOP963");

        //then
        assertEquals(Optional.empty(), result);
    }

}