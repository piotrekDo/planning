package com.piotrdomagalski.planning.app_user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AppUserRepositoryTest {

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    void findByUsernameIgnoreCase_should_return_optional_empty_if_no_user_found(){
        //when
        Optional<AppUser> result = appUserRepository.findByUsernameIgnoreCase("user");

        //then
        assertEquals(Optional.empty(), result);
    }

    @Test
    void findByUserEmailIgnoreCase_should_return_optional_empty_if_no_user_found(){
        //when
        Optional<AppUser> result = appUserRepository.findByUserEmailIgnoreCase("user@user.com");

        //then
        assertEquals(Optional.empty(), result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "Admin", "ADMIN", "aDmIN"})
    void findByUsernameIgnoreCase_should_return_optional_of_user_if_found(String username){
        //given
        AppUser appUser = new AppUser("admin", "admin@mail.com", "admin1");
        testEntityManager.persist(appUser);
        //when
        Optional<AppUser> result = appUserRepository.findByUsernameIgnoreCase(username);

        //then
        assertEquals(Optional.of(new AppUser(1L, "admin", "admin@mail.com", "admin1", new ArrayList<>())), result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin@mail.com", "Admin@mail.com", "ADMIN@MAIL.COM", "Admin@Mail.Com"})
    void findByUserEmailIgnoreCase_should_return_optional_of_user_if_found(String mail){
        //given
        AppUser appUser = new AppUser("admin", "admin@mail.com", "admin1");
        testEntityManager.persist(appUser);
        //when
        Optional<AppUser> result = appUserRepository.findByUserEmailIgnoreCase(mail);

        //then
        assertEquals(Optional.of(new AppUser(1L, "admin", "admin@mail.com", "admin1", new ArrayList<>())), result);
    }


}