package com.piotrdomagalski.planning.truck;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
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
        Optional<TruckEntity> result = truckRepository.findByTruckPlates(plates);

        //then
        assertEquals(Optional.empty(), result);
    }

    @Test
    void findByTruckPlates_should_return_optional_of_truck_entity_if_found() {
        //given
        String plates = "ASD432";
        testEntityManager.persist(new TruckEntity(plates, false, null, null, null));

        //when
        Optional<TruckEntity> result = truckRepository.findByTruckPlates(plates);

        //then
        assertEquals(Optional.of(new TruckEntity(1L, plates, false, null, null, null)), result);
    }

}