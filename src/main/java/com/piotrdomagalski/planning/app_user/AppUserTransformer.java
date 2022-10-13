package com.piotrdomagalski.planning.app_user;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Transform class for app user. Allows transforming between entity and DTO
 */

@Component
public class AppUserTransformer {

    AppUser createNewDtoToEntity(AppUserCreateNewDto dto) {
        return new AppUser(
                null,
                dto.getUsername(),
                dto.getUserEmail(),
                null,
                new ArrayList<>()
        );
    }

    AppUserDto toUserDto(AppUser appUser) {
        return new AppUserDto(
                appUser.getId(),
                appUser.getUsername(),
                appUser.getUserEmail(),
                appUser.getUserRoles().stream()
                        .map(userRole -> userRole.getRoleName())
                        .collect(Collectors.toList())
        );
    }

}
