package com.piotrdomagalski.planning.truck;

import com.piotrdomagalski.planning.app_user.AppUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
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
    void findAllByUser_username_should_return_favorites_truck_for_user() {
        //given
        TruckEntity abc123 = TruckEntity.newTruck("ABC123", true);
        TruckEntity xyz456 = TruckEntity.newTruck("XYZ456", true);
        TruckEntity ooo999 = TruckEntity.newTruck("OOO999", true);
        AppUser appUser1 = new AppUser("user1", "user@example.com", "password");
        AppUser appUser2 = new AppUser("user2", "user2@example.com", "password");
        testEntityManager.persist(abc123);
        testEntityManager.persist(xyz456);
        testEntityManager.persist(ooo999);
        testEntityManager.persist(appUser1);
        testEntityManager.persist(appUser2);
        appUser1.getFavoritesTrucks().add(abc123);
        appUser1.getFavoritesTrucks().add(xyz456);
        appUser2.getFavoritesTrucks().add(ooo999);

        //when
        List<TruckEntity> result = truckRepository.findByAppUser_username("user1");

        //then
        assertEquals(List.of(abc123, xyz456), result);
    }

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