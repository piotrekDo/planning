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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserRoleRepositoryTest {

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    void findByRoleNameIgnoreCase_should_return_optional_empty_if_no_user_found() {
        //when
        Optional<UserRole> result = userRoleRepository.findByRoleNameIgnoreCase("user");

        //then
        assertEquals(Optional.empty(), result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "Admin", "ADMIN", "aDmIN"})
    void findByRoleNameIgnoreCase_should_return_optional_of_user_if_found(String role) {
        //given
        UserRole userRole = new UserRole("ADMIN");
        testEntityManager.persist(userRole);

        //when
        Optional<UserRole> result = userRoleRepository.findByRoleNameIgnoreCase(role);

        //then
        assertEquals(Optional.of(new UserRole(1L, "ADMIN")), result);
    }
}