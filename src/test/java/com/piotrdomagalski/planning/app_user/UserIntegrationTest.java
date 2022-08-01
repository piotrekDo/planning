package com.piotrdomagalski.planning.app_user;

import com.piotrdomagalski.planning.PlanningApplication;
import com.piotrdomagalski.planning.mailing.EmailComposer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PlanningApplication.class)
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @MockBean
    EmailComposer emailComposer;

    @Test
    @WithMockUser(username = "Test", authorities = {"ADMIN"})
    void saving_nev_user_should_return_code_200_and_response_body_and_add_user_to_database_set_their_password_and_role_as_user() throws Exception {
        //given
        UserRole user = userRoleRepository.save(new UserRole("USER"));

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/save").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "new_user",
                        "userEmail": "piotr.domagalski@yahoo.com"
                        }
                        """));

        //then
        AppUser appUser = appUserRepository.findByUsernameIgnoreCase("new_user").get();
        assertNotNull(appUser);
        assertNotNull(appUser.getUserPassword());
        assertEquals(1, appUser.getUserRoles().size());
        assertTrue(appUser.getUserRoles().contains(user));
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", equalTo("new_user")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userEmail", equalTo("piotr.domagalski@yahoo.com")));
    }

    @Test
    @WithMockUser(username = "Test", authorities = {"USER"})
    void saving_new_user_should_return_forbidden_if_posting_user_is_not_admin() throws Exception {
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/save").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "new_user",
                        "userEmail": "mail@mail.com"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Test", authorities = {"ADMIN"})
    void saving_new_user_with_existing_user_name_should_return_bad_request_and_NOT_override_existing_user() throws Exception {
        //given
        UserRole user = userRoleRepository.save(new UserRole("USER"));
        appUserRepository.save(new AppUser(1L, "user", "user@mail.com", "user123", List.of(user)));

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/save").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "user",
                        "userEmail": "another@mail.com"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("Another user already exists with username user")));

        AppUser appUser = appUserRepository.findByUsernameIgnoreCase("user").get();
        assertEquals("user@mail.com", appUser.getUserEmail());
    }

    @Test
    @WithMockUser(username = "Test", authorities = {"USER"})
    void adding_role_to_user_should_return_code_403_if_user_is_not_ADMIN() throws Exception {
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/role/add-to-user").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "new_user",
                        "roleName": "ADMIN"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Test", authorities = {"ADMIN"})
    void adding_role_to_user_should_return_user_with_new_role() throws Exception {
        //given
        UserRole user = userRoleRepository.save(new UserRole("USER"));
        UserRole admin = userRoleRepository.save(new UserRole("ADMIN"));
        appUserRepository.save(new AppUser(1L, "user", "user@mail.com", "user123", List.of(user)));

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/role/add-to-user").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "user",
                        "roleName": "ADMIN"
                        }
                        """));

        //then
        AppUser appUser = appUserRepository.findByUsernameIgnoreCase("user").get();
        assertTrue(appUser.getUserRoles().contains(user));
        assertTrue(appUser.getUserRoles().contains(admin));
        assertEquals(2, appUser.getUserRoles().size());
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", equalTo("user")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roleName", equalTo("ADMIN")));
    }

    @Test
    @WithMockUser(username = "Test", authorities = {"ADMIN"})
    void adding_role_to_user_should_return_not_found_when_passing_non_existing_user() throws Exception {
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/role/add-to-user").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "user",
                        "roleName": "ADMIN"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No user found with username user")));
    }

    @Test
    @WithMockUser(username = "Test", authorities = {"USER"})
    void deleteUser_should_return_code_403_if_user_is_not_ADMIN() throws Exception {
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/delete-user").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "User"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Test", authorities = {"ADMIN"})
    void deleting_user_should_return_code_200_and_username_when_deleted_and_username() throws Exception {
        //when
        UserRole user = userRoleRepository.save(new UserRole("USER"));
        appUserRepository.save(new AppUser(1L, "user", "user@mail.com", "user123", List.of(user)));

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/users/delete-user").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "user"
                        }
                        """));

        //then
        Optional<AppUser> result = appUserRepository.findByUserEmailIgnoreCase("user");
        assertEquals(Optional.empty(), result);
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", equalTo("user")));
    }

    @Test
    @WithMockUser(username = "Test", authorities = {"ADMIN"})
    void deleting_user_should_return_not_found_if_deleting_non_existing_user() throws Exception {

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/users/delete-user").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "User"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No user found with username User")));
    }

    @Test
    @WithMockUser(username = "Test", authorities = {"USER"})
    void removing_role_from_user_should_return_code_403_if_user_is_not_ADMIN() throws Exception {
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/role/remove-from-user").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "user",
                        "roleName": "MODERATOR"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Test", authorities = {"ADMIN"})
    void removing_role_from_user_should_return_code_200_and_username_if_role_was_removed() throws Exception {
        //given
        UserRole user = userRoleRepository.save(new UserRole("USER"));
        UserRole admin = userRoleRepository.save(new UserRole("ADMIN"));
        appUserRepository.save(new AppUser(1L, "user", "user@mail.com", "user123", List.of(user, admin)));
        RoleToUserForm roleToUserForm = new RoleToUserForm("user", "ADMIN");

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/role/remove-from-user").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "user",
                        "roleName": "ADMIN"
                        }
                        """));

        //then
        AppUser appUser = appUserRepository.findByUsernameIgnoreCase("user").get();
        assertEquals(1, appUser.getUserRoles().size());
        assertTrue(appUser.getUserRoles().contains(user));
        assertFalse(appUser.getUserRoles().contains(admin));
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", equalTo("user")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roleName", equalTo("ADMIN")));
    }

    @Test
    @WithMockUser(username = "Test", authorities = {"ADMIN"})
    void removing_role_from_user_should_return_not_found_if_user_doesnt_exist() throws Exception {
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/role/remove-from-user").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "user",
                        "roleName": "ADMIN"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No user found with username user")));
    }

    @Test
    void sending_password_reset_request_should_return_code_200_and_message_if_token_was_sent() throws Exception {
        //given
        UserRole userRole = userRoleRepository.save(new UserRole("USER"));
        AppUser user = appUserRepository.save(new AppUser(1L, "user", "user@mail.com", "user123", List.of(userRole)));

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/password-reset-request").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "user",
                        "email": "user@mail.com"
                        }
                        """));

        //then
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUser(user).get();
        assertNotNull(passwordResetToken);
        assertEquals(user.getUsername(), passwordResetToken.getUser().getUsername());
        assertNotNull(passwordResetToken.getToken());
        assertNotNull(passwordResetToken.getExpiryDate());
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Token was sent to your e-mail")));
    }

    @Test
    void sending_password_reset_request_should_override_existing_token_if_such_was_created_before() throws Exception {
        //given
        UserRole userRole = userRoleRepository.save(new UserRole("USER"));
        AppUser user = appUserRepository.save(new AppUser(1L, "user", "user@mail.com", "user123", List.of(userRole)));
        PasswordResetToken first_token = new PasswordResetToken("TOKEN1234", user);
        passwordResetTokenRepository.save(first_token);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/password-reset-request").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "user",
                        "email": "user@mail.com"
                        }
                        """));

        //then
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUser(user).get();
        assertNotNull(passwordResetToken);
        assertEquals(user.getUsername(), passwordResetToken.getUser().getUsername());
        assertNotEquals(first_token.getToken(), passwordResetToken.getToken());
        assertNotNull(passwordResetToken.getExpiryDate());
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Token was sent to your e-mail")));
    }

    @Test
    void change_user_password_should_return_code_200_and_username_and_change_the_password() throws Exception {
        //given
        UserRole userRole = userRoleRepository.save(new UserRole("USER"));
        AppUser user = appUserRepository.save(new AppUser(1L, "user", "user@mail.com", "user123", List.of(userRole)));
        passwordResetTokenRepository.save(new PasswordResetToken("TOKEN1234", user));

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/password-change").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "user",
                        "changePasswordToken": "TOKEN1234",
                        "newPassword": "new_password"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", equalTo("user")));
        AppUser updatedUser = appUserRepository.findByUsernameIgnoreCase("user").get();
        Optional<PasswordResetToken> token = passwordResetTokenRepository.findByUser(user);
        assertNotEquals(user.getUserPassword(), updatedUser.getUserPassword());
        assertEquals(Optional.empty(), token);
    }

    @Test
    void changing_user_password_should_return_404_and_error_message_when_no_such_user_was_found() throws Exception {
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/password-change").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "user",
                        "changePasswordToken": "TOKEN1234",
                        "newPassword": "new_password"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No user found with username user")));
    }

    @Test
    void changing_user_password_should_return_404_and_error_message_when_no_reset_token_is_found_and_password_should_not_change() throws Exception {
        //given
        UserRole userRole = userRoleRepository.save(new UserRole("USER"));
        AppUser user = appUserRepository.save(new AppUser(1L, "user", "user@mail.com", "user123", List.of(userRole)));

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/password-change").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "user",
                        "changePasswordToken": "TOKEN1234",
                        "newPassword": "new_password"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No token found for user user")));

        AppUser appUser = appUserRepository.findByUsernameIgnoreCase("user").get();
        assertEquals(user.getUserPassword(), appUser.getUserPassword());
    }

    @Test
    void changing_user_pssword_should_return_400_and_error_message_when_change_password_token_has_expired_and_password_should_not_change() throws Exception {
        //given
        UserRole userRole = userRoleRepository.save(new UserRole("USER"));
        AppUser user = appUserRepository.save(new AppUser(1L, "user", "user@mail.com", "user123", List.of(userRole)));
        PasswordResetToken token = new PasswordResetToken("TOKEN1234", user);
        token.setExpiryDate(token.getExpiryDate().minusMinutes(240));
        passwordResetTokenRepository.save(token);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/password-change").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "user",
                        "changePasswordToken": "TOKEN1234",
                        "newPassword": "new_password"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("Token has expired")));

        AppUser appUser = appUserRepository.findByUsernameIgnoreCase("user").get();
        assertEquals(user.getUserPassword(), appUser.getUserPassword());
    }

    @Test
    void changing_user_password_should_return_code_400_and_error_message_if_provided_token_is_incorrect_and_password_should_not_change() throws Exception {
        //given
        UserRole userRole = userRoleRepository.save(new UserRole("USER"));
        AppUser user = appUserRepository.save(new AppUser(1L, "user", "user@mail.com", "user123", List.of(userRole)));
        PasswordResetToken token = new PasswordResetToken("TOKEN1234", user);
        passwordResetTokenRepository.save(token);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/password-change").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "user",
                        "changePasswordToken": "WRONG_TOKEN",
                        "newPassword": "new_password"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("Incorrect token provided")));

        AppUser appUser = appUserRepository.findByUsernameIgnoreCase("user").get();
        assertEquals(user.getUserPassword(), appUser.getUserPassword());
    }


}
