package com.piotrdomagalski.planning.carrier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CarrierRepositoryTest {

    @Autowired
    CarrierRepository carrierRepository;

    @Autowired
    TestEntityManager testEntityManager;


    @Test
    void find_by_name_should_return_carrier_if_exists(){
        //given
        CarrierEntity carrierEntity = CarrierEntity.newCarrier("123456", "Test carrier", "Testland", 1.0);
        testEntityManager.persist(carrierEntity);

        //when
        Optional<CarrierEntity> test_carrier = carrierRepository.findByName("Test carrier");

        //then
        carrierEntity.setId(1L);
        assertEquals(Optional.of(carrierEntity),
                test_carrier);
    }

    @Test
    void should_return_optional_empty_if_no_value_with_provided_name_exists() {
        //when
        Optional<CarrierEntity> test_carrier = carrierRepository.findByName("Test carrier");

        //then
        assertEquals(Optional.empty(),
                test_carrier);
    }

}