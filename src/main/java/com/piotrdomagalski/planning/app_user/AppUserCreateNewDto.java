package com.piotrdomagalski.planning.app_user;

import com.piotrdomagalski.planning.constraint.UserNameConstraint;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class AppUserCreateNewDto {
    @NotBlank
    @UserNameConstraint(message = "Username mist start with 3 letters. Can only contain letter, digits and underscore")
    private String username;
    @Email
    private String userEmail;

    public AppUserCreateNewDto(String username, String userEmail) {
        this.username = username;
        this.userEmail = userEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppUserCreateNewDto)) return false;
        AppUserCreateNewDto that = (AppUserCreateNewDto) o;
        return Objects.equals(username, that.username) && Objects.equals(userEmail, that.userEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, userEmail);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
