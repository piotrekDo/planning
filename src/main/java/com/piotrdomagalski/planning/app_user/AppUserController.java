package com.piotrdomagalski.planning.app_user;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Rest controller for users. @RestController ensures response coding to JSON format
 */

@RestController
@RequestMapping("/users")
public class AppUserController {
    private final AppUserService userService;

    public AppUserController(AppUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    AppUserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     *
     * @param page if not passed will be default 0
     * @param size if not passed will be set to 50
     * @return
     */

    @GetMapping
    Page<AppUserDto> getAllUsers(@RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer size) {
        return userService.getAllUsers(page, size);
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




