package com.piotrdomagalski.planning.app_user;

import com.piotrdomagalski.planning.error.IllegalOperationException;
import com.piotrdomagalski.planning.mailing.EmailComposer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @Autowired
    AppUserService userService;
    @MockBean
    AppUserRepository userRepository;
    @MockBean
    UserRoleRepository roleRepository;
    @MockBean
    PasswordResetTokenRepository passwordResetTokenRepository;
    @MockBean
    AppUserTransformer appUserTransformer;
    @MockBean
    PasswordEncoder passwordEncoder;
    @MockBean
    EmailComposer emailComposer;

    @Test
    void saveUser_should_return_new_user_details_if_saved() {
        //given
        AppUserCreateNewDto newUserDto = new AppUserCreateNewDto("user1", "User@mail.com");
        AppUser appUserfromDto = new AppUser("user1", "User@mail.com", null);
        UserRole role_user = new UserRole(99L, "USER");
        Mockito.when(userRepository.findByUserEmailIgnoreCase(newUserDto.getUserEmail())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByUsernameIgnoreCase(newUserDto.getUsername())).thenReturn(Optional.empty());
        Mockito.when(appUserTransformer.createNewDtoToEntity(newUserDto)).thenReturn(appUserfromDto);
        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn("#GHJ^&");
        Mockito.when(roleRepository.findByRoleNameIgnoreCase("user")).thenReturn(Optional.of(role_user));

        //when
        AppUserCreateNewDto result = userService.saveUser(newUserDto);

        //then
        assertEquals(newUserDto, result);
        assertTrue(appUserfromDto.getUserRoles().contains(role_user));
        assertNotNull(appUserfromDto.getUserPassword());
        Mockito.verify(userRepository).findByUserEmailIgnoreCase(newUserDto.getUserEmail());
        Mockito.verify(userRepository).findByUsernameIgnoreCase(newUserDto.getUsername());
        Mockito.verify(passwordEncoder).encode(Mockito.any());
        Mockito.verify(roleRepository).findByRoleNameIgnoreCase("user");
        Mockito.verify(userRepository).save(appUserfromDto);
        Mockito.verify(emailComposer).sendRegistrationMessage(Mockito.any(), Mockito.any());
    }

    @Test
    void saveUser_should_throw_an_exception_if_user_with_provided_mail_already_exists() {
        //given
        AppUserCreateNewDto newUserDto = new AppUserCreateNewDto("user1", "User@mail.com");
        Mockito.when(userRepository.findByUserEmailIgnoreCase(newUserDto.getUserEmail())).thenReturn(Optional.of(new AppUser()));

        //when + then
        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> userService.saveUser(newUserDto));
        assertEquals("Another user already exists with email address User@mail.com", exception.getMessage());
        Mockito.verify(userRepository).findByUserEmailIgnoreCase(newUserDto.getUserEmail());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(emailComposer, Mockito.never()).sendRegistrationMessage(Mockito.any(), Mockito.any());
    }

    @Test
    void saveUser_should_throw_an_exception_if_user_with_provided_username_already_exists() {
        //given
        AppUserCreateNewDto newUserDto = new AppUserCreateNewDto("user1", "User@mail.com");
        Mockito.when(userRepository.findByUserEmailIgnoreCase(newUserDto.getUserEmail())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByUsernameIgnoreCase(newUserDto.getUsername())).thenReturn(Optional.of(new AppUser()));

        //when + then
        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> userService.saveUser(newUserDto));
        assertEquals("Another user already exists with username user1", exception.getMessage());
        Mockito.verify(userRepository).findByUsernameIgnoreCase(newUserDto.getUsername());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(emailComposer, Mockito.never()).sendRegistrationMessage(Mockito.any(), Mockito.any());
    }

    @Test
    void requestResetUserPassword_should_return_message_if_token_was_sent() {
        //given
        PasswordChangeRequestDto request = new PasswordChangeRequestDto("user", "user@mail.com");
        AppUser appUser = new AppUser("user", "user@mail.com", "abc1234");
        Mockito.when(userRepository.findByUsernameIgnoreCase(request.getUsername())).thenReturn(Optional.of(appUser));

        //when
        MessageDto result = userService.requestResetUserPassword(request);

        //then
        assertEquals(new MessageDto("Token was sent to your e-mail"), result);
        Mockito.verify(userRepository).findByUsernameIgnoreCase(request.getUsername());
        Mockito.verify(passwordResetTokenRepository).save(Mockito.any());
        Mockito.verify(emailComposer).sendResetPasswordToken(Mockito.any(), Mockito.any());
    }

    @Test
    void requestResetUserPassword_should_throw_an_exception_when_provided_user_doesnt_exist() {
        //given
        PasswordChangeRequestDto request = new PasswordChangeRequestDto("user", "user@mail.com");
        Mockito.when(userRepository.findByUsernameIgnoreCase(request.getUsername())).thenReturn(Optional.empty());

        //when + then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> userService.requestResetUserPassword(request));
        assertEquals("No user found with username user", exception.getMessage());
        Mockito.verify(userRepository).findByUsernameIgnoreCase(request.getUsername());
        Mockito.verify(passwordResetTokenRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(emailComposer, Mockito.never()).sendResetPasswordToken(Mockito.any(), Mockito.any());
    }

    @Test
    void requestResetUserPassword_should_throw_an_exception_when_provided_email_address_doesnt_match() {
        // given
        PasswordChangeRequestDto request = new PasswordChangeRequestDto("user", "another_user@mail.com");
        AppUser appUser = new AppUser("user", "user@mail.com", "abc1234");
        Mockito.when(userRepository.findByUsernameIgnoreCase(request.getUsername())).thenReturn(Optional.of(appUser));

        //when + then
        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> userService.requestResetUserPassword(request));
        assertEquals("Incorrect mail provided", exception.getMessage());
        Mockito.verify(userRepository).findByUsernameIgnoreCase(request.getUsername());
        Mockito.verify(passwordResetTokenRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(emailComposer, Mockito.never()).sendResetPasswordToken(Mockito.any(), Mockito.any());
    }

    @Test
    void changeUserPassword_should_return_username_if_password_was_changed() {
        //given
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto("user", "token123", "user1ABC");
        AppUser appUser = new AppUser("user", "user@mail.com", "user1");
        PasswordResetToken tokenObject = new PasswordResetToken("token123", appUser);
        Mockito.when(userRepository.findByUsernameIgnoreCase(passwordChangeDto.getUsername())).thenReturn(Optional.of(appUser));
        Mockito.when(passwordResetTokenRepository.findByUser(appUser)).thenReturn(Optional.of(tokenObject));
        Mockito.when(passwordEncoder.encode(passwordChangeDto.getNewPassword())).thenReturn("ENCODED_NEW_PASSWORD");

        //when
        UsernameDto result = userService.changeUserPassword(passwordChangeDto);

        //then
        assertEquals(new UsernameDto("user"), result);
        assertEquals("ENCODED_NEW_PASSWORD", appUser.getUserPassword());
        Mockito.verify(userRepository).findByUsernameIgnoreCase(passwordChangeDto.getUsername());
        Mockito.verify(passwordResetTokenRepository).findByUser(appUser);
        Mockito.verify(passwordEncoder).encode(passwordChangeDto.getNewPassword());
        Mockito.verify(userRepository).save(appUser);
        Mockito.verify(emailComposer).sendPasswordChangeConformation(appUser);
        Mockito.verify(passwordResetTokenRepository).delete(tokenObject);
    }

    @Test
    void changeUserPassword_should_throw_an_exception_when_no_provided_user_found() {
        //given
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto("user", "token123", "user1ABC");
        Mockito.when(userRepository.findByUsernameIgnoreCase(passwordChangeDto.getUsername())).thenReturn(Optional.empty());

        //when + then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> userService.changeUserPassword(passwordChangeDto));
        assertEquals("No user found with username user", exception.getMessage());
        Mockito.verify(userRepository).findByUsernameIgnoreCase(passwordChangeDto.getUsername());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(emailComposer, Mockito.never()).sendPasswordChangeConformation(Mockito.any());
        Mockito.verify(passwordResetTokenRepository, Mockito.never()).delete(Mockito.any());
    }

    @Test
    void changeUserPassword_should_throw_an_exception_when_no_token_was_found() {
        //given
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto("user", "token123", "user1ABC");
        AppUser appUser = new AppUser("user", "user@mail.com", "user1");
        Mockito.when(userRepository.findByUsernameIgnoreCase(passwordChangeDto.getUsername())).thenReturn(Optional.of(appUser));
        Mockito.when(passwordResetTokenRepository.findByUser(appUser)).thenReturn(Optional.empty());

        //when+then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> userService.changeUserPassword(passwordChangeDto));
        assertEquals("No token found for user user", exception.getMessage());
        Mockito.verify(userRepository).findByUsernameIgnoreCase(passwordChangeDto.getUsername());
        Mockito.verify(passwordResetTokenRepository).findByUser(appUser);
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(emailComposer, Mockito.never()).sendPasswordChangeConformation(Mockito.any());
        Mockito.verify(passwordResetTokenRepository, Mockito.never()).delete(Mockito.any());
    }

    @Test
    void changeUserPassword_should_throw_an_exception_when_provided_token_doesnt_match() {
        //given
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto("user", "wrong_token", "user1ABC");
        AppUser appUser = new AppUser("user", "user@mail.com", "user1");
        PasswordResetToken tokenObject = new PasswordResetToken("token123", appUser);
        Mockito.when(userRepository.findByUsernameIgnoreCase(passwordChangeDto.getUsername())).thenReturn(Optional.of(appUser));
        Mockito.when(passwordResetTokenRepository.findByUser(appUser)).thenReturn(Optional.of(tokenObject));

        //when + then
        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> userService.changeUserPassword(passwordChangeDto));
        assertEquals("Incorrect token provided", exception.getMessage());
        Mockito.verify(userRepository).findByUsernameIgnoreCase(passwordChangeDto.getUsername());
        Mockito.verify(passwordResetTokenRepository).findByUser(appUser);
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(emailComposer, Mockito.never()).sendPasswordChangeConformation(Mockito.any());
        Mockito.verify(passwordResetTokenRepository, Mockito.never()).delete(Mockito.any());
    }

    @Test
    void changeUserPassword_should_throw_an_exception_if_token_has_expired() {
        //given
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto("user", "token123", "user1ABC");
        AppUser appUser = new AppUser("user", "user@mail.com", "user1");
        PasswordResetToken tokenObject = new PasswordResetToken("token123", appUser);
        tokenObject.setExpiryDate(tokenObject.getExpiryDate().minusMinutes(45));
        Mockito.when(userRepository.findByUsernameIgnoreCase(passwordChangeDto.getUsername())).thenReturn(Optional.of(appUser));
        Mockito.when(passwordResetTokenRepository.findByUser(appUser)).thenReturn(Optional.of(tokenObject));

        //when + then
        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> userService.changeUserPassword(passwordChangeDto));
        assertEquals("Token has expired", exception.getMessage());
        Mockito.verify(userRepository).findByUsernameIgnoreCase(passwordChangeDto.getUsername());
        Mockito.verify(passwordResetTokenRepository).findByUser(appUser);
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
        Mockito.verify(emailComposer, Mockito.never()).sendPasswordChangeConformation(Mockito.any());
        Mockito.verify(passwordResetTokenRepository, Mockito.never()).delete(Mockito.any());
    }

    @Test
    void deleteUser_should_return_username_if_user_was_deleted() {
        //given
        UsernameDto user = new UsernameDto("user");
        AppUser appUser = new AppUser("user", "user@mail.com", "user1");
        Mockito.when(userRepository.findByUsernameIgnoreCase(user.getUsername())).thenReturn(Optional.of(appUser));

        //when
        UsernameDto result = userService.deleteUser(user);

        //then
        assertEquals(user, result);
        Mockito.verify(userRepository).findByUsernameIgnoreCase(user.getUsername());
        Mockito.verify(userRepository).delete(appUser);
    }

    @Test
    void deleteUser_should_throw_an_exception_when_deleting_non_existing_user() {
        //given
        UsernameDto user = new UsernameDto("user");

        //when + then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> userService.deleteUser(user));
        assertEquals("No user found with username user", exception.getMessage());
        Mockito.verify(userRepository).findByUsernameIgnoreCase(user.getUsername());
        Mockito.verify(userRepository, Mockito.never()).delete(Mockito.any());
    }

    @Test
    void addRoleToUser_should_return_user_if_role_was_added() {
        //given
        RoleToUserForm roleToUserForm = new RoleToUserForm("user", "moderator");
        AppUser appUser = new AppUser(1L, "user", "user@mail.com", "user1", new ArrayList<>());
        UserRole userRole = new UserRole("moderator");
        Mockito.when(userRepository.findByUsernameIgnoreCase(roleToUserForm.getUsername())).thenReturn(Optional.of(appUser));
        Mockito.when(roleRepository.findByRoleNameIgnoreCase(roleToUserForm.getRoleName())).thenReturn(Optional.of(userRole));

        //when
        RoleToUserForm result = userService.addRoleToUser(roleToUserForm);

        //then
        assertEquals(roleToUserForm, result);
        assertTrue(appUser.getUserRoles().contains(userRole));
        Mockito.verify(userRepository).findByUsernameIgnoreCase(roleToUserForm.getUsername());
        Mockito.verify(roleRepository).findByRoleNameIgnoreCase(result.getRoleName());
        Mockito.verify(userRepository).save(appUser);
    }

    @Test
    void addRoleToUser_should_throw_an_exception_when_user_doesnt_exist() {
        //given
        RoleToUserForm roleToUserForm = new RoleToUserForm("user", "moderator");
        Mockito.when(userRepository.findByUsernameIgnoreCase(roleToUserForm.getUsername())).thenReturn(Optional.empty());

        //when + then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> userService.addRoleToUser(roleToUserForm));
        assertEquals("No user found with username user", exception.getMessage());
        Mockito.verify(userRepository).findByUsernameIgnoreCase(roleToUserForm.getUsername());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void addRoleToUser_should_throw_an_exception_when_role_doesnt_exist() {
        //given
        RoleToUserForm roleToUserForm = new RoleToUserForm("user", "moderator");
        AppUser appUser = new AppUser(1L, "user", "user@mail.com", "user1", new ArrayList<>());
        Mockito.when(userRepository.findByUsernameIgnoreCase(roleToUserForm.getUsername())).thenReturn(Optional.of(appUser));
        Mockito.when(roleRepository.findByRoleNameIgnoreCase(roleToUserForm.getRoleName())).thenReturn(Optional.empty());

        //when + then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> userService.addRoleToUser(roleToUserForm));
        assertEquals("No user role found with name moderator", exception.getMessage());
        Mockito.verify(userRepository).findByUsernameIgnoreCase(roleToUserForm.getUsername());
        Mockito.verify(roleRepository).findByRoleNameIgnoreCase(roleToUserForm.getRoleName());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void removeRoleFromUser_should_return_user_if_role_was_removed() {
        //given
        RoleToUserForm roleToUserForm = new RoleToUserForm("user", "moderator");
        UserRole userRole = new UserRole("moderator");
        AppUser appUser = new AppUser(1L, "user", "user@mail.com", "user1", new ArrayList<>());
        appUser.getUserRoles().add(new UserRole("USER"));
        appUser.getUserRoles().add(new UserRole("MODERATOR"));
        Mockito.when(userRepository.findByUsernameIgnoreCase(roleToUserForm.getUsername())).thenReturn(Optional.of(appUser));
        Mockito.when(roleRepository.findByRoleNameIgnoreCase(roleToUserForm.getRoleName())).thenReturn(Optional.of(userRole));

        //when
        RoleToUserForm result = userService.removeRoleFromUser(roleToUserForm);

        //then
        assertEquals(roleToUserForm, result);
        assertFalse(appUser.getUserRoles().contains(userRole));
        Mockito.verify(userRepository).findByUsernameIgnoreCase(roleToUserForm.getUsername());
        Mockito.verify(roleRepository).findByRoleNameIgnoreCase(roleToUserForm.getRoleName());
        Mockito.verify(userRepository).save(appUser);
    }

    @Test
    void removeRoleFromUser_should_throw_an_exception_when_user_doesnt_exist() {
        //given
        RoleToUserForm roleToUserForm = new RoleToUserForm("user", "moderator");
        Mockito.when(userRepository.findByUsernameIgnoreCase(roleToUserForm.getUsername())).thenReturn(Optional.empty());

        //when + then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> userService.removeRoleFromUser(roleToUserForm));
        assertEquals("No user found with username user", exception.getMessage());
        Mockito.verify(userRepository).findByUsernameIgnoreCase(roleToUserForm.getUsername());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void removeRoleFromUser_should_throw_an_exception_if_role_doesnt_exist() {
        //given
        RoleToUserForm roleToUserForm = new RoleToUserForm("user", "moderator");
        AppUser appUser = new AppUser(1L, "user", "user@mail.com", "user1", new ArrayList<>());
        appUser.getUserRoles().add(new UserRole("USER"));
        appUser.getUserRoles().add(new UserRole("MODERATOR"));
        Mockito.when(userRepository.findByUsernameIgnoreCase(roleToUserForm.getUsername())).thenReturn(Optional.of(appUser));
        Mockito.when(roleRepository.findByRoleNameIgnoreCase(roleToUserForm.getRoleName())).thenReturn(Optional.empty());

        //when + then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> userService.removeRoleFromUser(roleToUserForm));
        assertEquals("No user role found with name moderator", exception.getMessage());
        Mockito.verify(userRepository).findByUsernameIgnoreCase(roleToUserForm.getUsername());
        Mockito.verify(roleRepository).findByRoleNameIgnoreCase(roleToUserForm.getRoleName());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void removeRoleFromUser_should_throw_an_exception_if_role_is_user() {
        //given
        RoleToUserForm roleToUserForm = new RoleToUserForm("user", "user");

        //when + then
        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> userService.removeRoleFromUser(roleToUserForm));
        assertEquals("The default role of USER cannot be removed!", exception.getMessage());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any());
    }

    @TestConfiguration
    static class UserServiceTestConfiguration {

        @Bean
        AppUserService userService(AppUserRepository userRepository, UserRoleRepository roleRepository, PasswordResetTokenRepository passwordResetTokenRepository, AppUserTransformer appUserTransformer, PasswordEncoder passwordEncoder, EmailComposer emailComposer) {
            return new AppUserService(userRepository, roleRepository, passwordResetTokenRepository, appUserTransformer, passwordEncoder, emailComposer);
        }
    }

}