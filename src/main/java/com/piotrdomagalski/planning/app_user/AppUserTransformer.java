package com.piotrdomagalski.planning.app_user;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

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

}
