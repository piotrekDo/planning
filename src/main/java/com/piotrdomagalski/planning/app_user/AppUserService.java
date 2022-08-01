package com.piotrdomagalski.planning.app_user;

import com.piotrdomagalski.planning.error.IllegalOperationException;
import com.piotrdomagalski.planning.mailing.EmailComposer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.piotrdomagalski.planning.app.ConfigurationLibrary.USERS_RESULT_PER_PAGE;
import static com.piotrdomagalski.planning.utlis.PasswordGenerator.generatePassword;

/**
 * Service class fo application user
 */

@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepository userRepository;
    private final UserRoleRepository roleRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final AppUserTransformer appUserTransformer;
    private final PasswordEncoder passwordEncoder;
    private final EmailComposer emailComposer;

    public AppUserService(AppUserRepository userRepository, UserRoleRepository roleRepository, PasswordResetTokenRepository passwordResetTokenRepository, AppUserTransformer appUserTransformer, PasswordEncoder passwordEncoder, EmailComposer emailComposer) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.appUserTransformer = appUserTransformer;
        this.passwordEncoder = passwordEncoder;
        this.emailComposer = emailComposer;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByUsernameIgnoreCase(username).orElseThrow(() ->
                new NoSuchElementException("No user found with username " + username));
        return new UserDetailsAdapter(appUser);
    }

    public AppUserCreateNewDto saveUser(AppUserCreateNewDto user) {
        userRepository.findByUserEmailIgnoreCase(user.getUserEmail()).ifPresent(u -> {
            throw new IllegalOperationException("Another user already exists with email address " + user.getUserEmail());
        });
        userRepository.findByUsernameIgnoreCase(user.getUsername()).ifPresent(u -> {
            throw new IllegalOperationException("Another user already exists with username " + user.getUsername());
        });

        AppUser appUserEntity = appUserTransformer.createNewDtoToEntity(user);
        String password = generatePassword();
        appUserEntity.setUserPassword(passwordEncoder.encode(password));
        UserRole userRole = roleRepository.findByRoleNameIgnoreCase("user").get();
        appUserEntity.getUserRoles().add(userRole);
        userRepository.save(appUserEntity);
        emailComposer.sendRegistrationMessage(user, password);
        return user;
    }

    MessageDto requestResetUserPassword(PasswordChangeRequestDto request) {
        AppUser appUser = userRepository.findByUsernameIgnoreCase(request.getUsername()).orElseThrow(() ->
                new NoSuchElementException("No user found with username " + request.getUsername()));

        if (!request.getEmail().equals(appUser.getUserEmail())) {
            throw new IllegalOperationException("Incorrect mail provided");
        }

        String token = UUID.randomUUID().toString();
        passwordResetTokenRepository.findByUser(appUser).ifPresent(passwordResetTokenRepository::delete);
        passwordResetTokenRepository.save(new PasswordResetToken(token, appUser));

        emailComposer.sendResetPasswordToken(request, token);
        return new MessageDto("Token was sent to your e-mail");
    }

    UsernameDto changeUserPassword(PasswordChangeDto data) {
        AppUser appUser = userRepository.findByUsernameIgnoreCase(data.getUsername()).orElseThrow(() ->
                new NoSuchElementException("No user found with username " + data.getUsername()));
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUser(appUser).orElseThrow(() ->
                new NoSuchElementException("No token found for user " + data.getUsername()));
        if (!passwordResetToken.getToken().equals(data.getChangePasswordToken())) {
            throw new IllegalOperationException("Incorrect token provided");
        }
        if (LocalDateTime.now().isAfter(passwordResetToken.getExpiryDate())) {
            throw new IllegalOperationException("Token has expired");
        }
        appUser.setUserPassword(passwordEncoder.encode(data.getNewPassword()));
        userRepository.save(appUser);
        emailComposer.sendPasswordChangeConformation(appUser);
        passwordResetTokenRepository.delete(passwordResetToken);
        return new UsernameDto(data.getUsername());
    }

    UsernameDto deleteUser(UsernameDto username) {
        AppUser appUser = userRepository.findByUsernameIgnoreCase(username.getUsername()).orElseThrow(() ->
                new NoSuchElementException("No user found with username " + username.getUsername()));
        userRepository.delete(appUser);
        return username;
    }

    public RoleToUserForm addRoleToUser(RoleToUserForm roleToUserForm) {
        AppUser appUser = userRepository.findByUsernameIgnoreCase(roleToUserForm.getUsername()).orElseThrow(() ->
                new NoSuchElementException("No user found with username " + roleToUserForm.getUsername()));
        UserRole role = roleRepository.findByRoleNameIgnoreCase(roleToUserForm.getRoleName()).orElseThrow(() ->
                new NoSuchElementException("No user role found with name " + roleToUserForm.getRoleName()));

        appUser.getUserRoles().add(role);
        userRepository.save(appUser);
        return roleToUserForm;
    }

    RoleToUserForm removeRoleFromUser(RoleToUserForm roleToUserForm) {
        if (roleToUserForm.getRoleName().equalsIgnoreCase("user")) {
            throw new IllegalOperationException("The default role of USER cannot be removed!");
        }
        AppUser appUser = userRepository.findByUsernameIgnoreCase(roleToUserForm.getUsername()).orElseThrow(() ->
                new NoSuchElementException("No user found with username " + roleToUserForm.getUsername()));
        UserRole role = roleRepository.findByRoleNameIgnoreCase(roleToUserForm.getRoleName()).orElseThrow(() ->
                new NoSuchElementException("No user role found with name " + roleToUserForm.getRoleName()));

        appUser.getUserRoles().remove(role);
        userRepository.save(appUser);
        return roleToUserForm;
    }

    public Page<AppUserDto> getAllUsers(Integer page, Integer size) {
        page = page == null || page < 0 ? 0 : page;
        size = size == null || size < 1 ? USERS_RESULT_PER_PAGE : size;
        Page<AppUser> results = userRepository
                .findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "username")));
        return results == null ? Page.empty() : results.map(appUserTransformer::toUserDto);
    }

    public AppUserDto getUserById(Long id) {
        AppUser appUser = userRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("No user found with id " + id));
        return appUserTransformer.toUserDto(appUser);
    }

}
