package com.piotrdomagalski.planning.app_user;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class AppUserController {

    private final AppUserService userService;

    public AppUserController(AppUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/save")
    AppUserCreateNewDto saveNewUser(@RequestBody @Valid AppUserCreateNewDto newUser) {
        return userService.saveUser(newUser);
    }

    @PostMapping("/role/add-to-user")
    RoleToUserForm addRoleToUser(@RequestBody RoleToUserForm roleToUserForm) {
        return userService.addRoleToUser(roleToUserForm);
    }

    @DeleteMapping("/delete-user")
    UsernameDto deleteUser(@RequestBody UsernameDto username) {
        return userService.deleteUser(username);
    }

    @PostMapping("/role/remove-from-user")
    RoleToUserForm removeRoleFromUser(@RequestBody RoleToUserForm roleToUserForm) {
        return userService.removeRoleFromUser(roleToUserForm);
    }

    @PostMapping("/password-reset-request")
    MessageDto resetUserPasswordRequest(@RequestBody PasswordChangeRequestDto request) {
        return userService.requestResetUserPassword(request);
    }

    @PostMapping("/password-change")
    UsernameDto changeUserPassword(@RequestBody PasswordChangeDto data) {
        return userService.changeUserPassword(data);
    }
}




