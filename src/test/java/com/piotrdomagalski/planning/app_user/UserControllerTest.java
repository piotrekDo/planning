package com.piotrdomagalski.planning.app_user;

import com.piotrdomagalski.planning.error.IllegalOperationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.equalTo;


@ExtendWith(SpringExtension.class)
@WebMvcTest(AppUserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    AppUserService userService;

    @Test
    @WithMockUser(username = "Test", authorities = {"USER"})
    void saveNewUser_should_return_code_403_if_user_is_not_ADMIN() throws Exception {
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

    @ParameterizedTest
    @ValueSource(strings = {"User", "user12", "user_new", "user_99", "new_user_user_9090"})
    @WithMockUser(username = "Test", authorities = {"ADMIN"})
    void saveNewUser_should_return_code_200_and_new_user_if_saved(String username) throws Exception {
        //given
        AppUserCreateNewDto appUserCreateNewDto = new AppUserCreateNewDto(username, "mail@mail.com");
        Mockito.when(userService.saveUser(appUserCreateNewDto)).thenReturn(appUserCreateNewDto);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/save").contentType(MediaType.APPLICATION_JSON)
                .content(String.format("""
                        {
                        "username": "%s",
                        "userEmail": "mail@mail.com"
                        }
                        """, username)));

        //then
        Mockito.verify(userService).saveUser(appUserCreateNewDto);
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", equalTo(username)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"_User", "user12_", "user__new", "user_ 99", "0new_user_user_9090", "user++", "!123", "123ab", "423.,.,.21"})
    @WithMockUser(username = "Test", authorities = {"ADMIN"})
    void saveNewUser_should_return_bad_request_when_adding_not_valid_username(String username) throws Exception {
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/save").contentType(MediaType.APPLICATION_JSON)
                .content(String.format("""
                        {
                        "username": "%s",
                        "userEmail": "mail@mail.com"
                        }
                        """, username)));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Bad Request")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"username.@domain.com", ".user.name@domain.com", "user-name@domain.com.", "username@.com"})
    @WithMockUser(username = "Test", authorities = {"ADMIN"})
    void saveNewUser_should_return_bad_request_when_adding_not_valid_email(String email) throws Exception {
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/save").contentType(MediaType.APPLICATION_JSON)
                .content(String.format("""
                        {
                        "username": "user",
                        "userEmail": "%s"
                        }
                        """, email)));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Bad Request")));
    }

    @Test
    @WithMockUser(username = "Test", authorities = {"ADMIN"})
    void saveNewUser_should_return_bad_request_when_existing_user() throws Exception {
        //given
        AppUserCreateNewDto appUserCreateNewDto = new AppUserCreateNewDto("user", "user@email.com");
        Mockito.when(userService.saveUser(appUserCreateNewDto)).thenThrow(
                new IllegalOperationException("Another user already exists with username user"));
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/save").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "user",
                        "userEmail": "user@email.com"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("Another user already exists with username user")));
    }

    @Test
    @WithMockUser(username = "Test", authorities = {"ADMIN"})
    void saveNewUser_should_return_bad_request_when_existing_email() throws Exception {
        //given
        AppUserCreateNewDto appUserCreateNewDto = new AppUserCreateNewDto("user", "user@email.com");
        Mockito.when(userService.saveUser(appUserCreateNewDto)).thenThrow(
                new IllegalOperationException("Another user already exists with email address user@email.com"));
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/save").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "user",
                        "userEmail": "user@email.com"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Bad Request")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("Another user already exists with email address user@email.com")));
    }

    @Test
    @WithMockUser(username = "Test", authorities = {"USER"})
    void addRoleToUser_should_return_code_403_if_user_is_not_ADMIN() throws Exception {
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
    void addRoleToUser_should_return_code_200_if_role_was_added() throws Exception {
        //given
        RoleToUserForm roleToUserForm = new RoleToUserForm("new_user", "ADMIN");
        Mockito.when(userService.addRoleToUser(roleToUserForm)).thenReturn(roleToUserForm);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/role/add-to-user").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "new_user",
                        "roleName": "ADMIN"
                        }
                        """));

        //then
        Mockito.verify(userService).addRoleToUser(roleToUserForm);
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", equalTo("new_user")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roleName", equalTo("ADMIN")));
    }

    @Test
    @WithMockUser(username = "Test", authorities = {"ADMIN"})
    void addRoleToUser_should_return_not_found_when_passing_non_existing_user() throws Exception {
        //given
        RoleToUserForm roleToUserForm = new RoleToUserForm("new_user", "ADMIN");
        Mockito.when(userService.addRoleToUser(roleToUserForm)).thenThrow(
                new NoSuchElementException("No user found with username user"));
        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/role/add-to-user").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "new_user",
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
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/users/delete-user").contentType(MediaType.APPLICATION_JSON)
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
    void deleteUser_should_return_code_200_and_username_when_deleted() throws Exception {
        //when
        UsernameDto user = new UsernameDto("User");
        Mockito.when(userService.deleteUser(user)).thenReturn(user);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.delete("/users/delete-user").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "User"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", equalTo("User")));
    }

    @Test
    @WithMockUser(username = "Test", authorities = {"ADMIN"})
    void deleteUser_should_return_not_found_if_deleting_non_existing_user() throws Exception {
        //given
        UsernameDto user = new UsernameDto("User");
        Mockito.when(userService.deleteUser(user)).thenThrow(
                new NoSuchElementException("No user found with username User"));

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
    void removeRoleFromUser_should_return_code_403_if_user_is_not_ADMIN() throws Exception {
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
    void removeRoleFromUser_should_return_code_200_and_username_if_role_was_removed() throws Exception {
        //given
        RoleToUserForm roleToUserForm = new RoleToUserForm("user", "ADMIN");
        Mockito.when(userService.removeRoleFromUser(roleToUserForm)).thenReturn(roleToUserForm);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/role/remove-from-user").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "user",
                        "roleName": "ADMIN"
                        }
                        """));

        //then
        Mockito.verify(userService).removeRoleFromUser(roleToUserForm);
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", equalTo("user")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roleName", equalTo("ADMIN")));
    }

    @Test
    @WithMockUser(username = "Test", authorities = {"ADMIN"})
    void removeRoleFromUser_should_return_not_found_if_user_doesnt_exist() throws Exception {
        //given
        RoleToUserForm roleToUserForm = new RoleToUserForm("user", "ADMIN");
        Mockito.when(userService.removeRoleFromUser(roleToUserForm)).thenThrow(
                new NoSuchElementException("No user found with username user"));

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
    void resetUserPasswordRequest_should_return_code_200_and_message_if_token_was_sent() throws Exception {
        //given
        PasswordChangeRequestDto request = new PasswordChangeRequestDto("user", "user@mail.com");
        MessageDto message = new MessageDto("Token was sent to your e-mail");
        Mockito.when(userService.requestResetUserPassword(request)).thenReturn(message);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/password-reset-request").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "user",
                        "email": "user@mail.com"
                        }
                        """));

        //then
        Mockito.verify(userService).requestResetUserPassword(request);
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Token was sent to your e-mail")));
    }

    @Test
    void resetUserPasswordRequest_should_return_not_found_if_no_such_user_was_found() throws Exception {
        //given
        PasswordChangeRequestDto request = new PasswordChangeRequestDto("user", "user@mail.com");
        Mockito.when(userService.requestResetUserPassword(request)).thenThrow(
                new NoSuchElementException("No user found with username user"));

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/password-reset-request").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "user",
                        "email": "user@mail.com"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No user found with username user")));
    }

    @Test
    void changeUserPassword_should_return_code_200_and_username_when_password_was_changed() throws Exception {
        //given
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto("user", "token123", "newpassword");
        UsernameDto user = new UsernameDto("user");
        Mockito.when(userService.changeUserPassword(passwordChangeDto)).thenReturn(user);

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/password-change").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "user",
                        "changePasswordToken": "token123",
                        "newPassword": "newpassword"
                        }
                        """));

        //then
        Mockito.verify(userService).changeUserPassword(passwordChangeDto);
        perform.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", equalTo("user")));
    }

    @Test
    void changeUserPassword_should_return_not_found_if_no_such_user_was_found() throws Exception {
        //given
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto("user", "token123", "newpassword");
        Mockito.when(userService.changeUserPassword(passwordChangeDto)).thenThrow(
                new NoSuchElementException("No user found with username user"));

        //when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/users/password-change").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "username": "user",
                        "changePasswordToken": "token123",
                        "newPassword": "newpassword"
                        }
                        """));

        //then
        perform.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", equalTo(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", equalTo("Not Found")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details", equalTo("No user found with username user")));
    }


}