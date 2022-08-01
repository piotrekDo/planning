package com.piotrdomagalski.planning.truck;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TruckRepositoryTest {

    @Autowired
    TruckRepository truckRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    void findByTruckPlates_should_return_an_empty_optional_when_no_such_plates_found() {
        //given
        String plates = "TEST123";

        //when
        Optional<TruckEntity> result = truckRepository.findByTruckPlatesIgnoreCase(plates);

        //then
        assertEquals(Optional.empty(), result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ASD432", "asd432", "aSd432"})
    void findByTruckPlates_should_return_optional_of_truck_entity_if_found(String plates) {
        //given
        testEntityManager.persist(new TruckEntity("ASD432", false, null, null, null));

        //when
        Optional<TruckEntity> result = truckRepository.findByTruckPlatesIgnoreCase(plates);

        //then
        assertEquals(Optional.of(new TruckEntity(1L, "ASD432", false, null, null, null)), result);
    }

}