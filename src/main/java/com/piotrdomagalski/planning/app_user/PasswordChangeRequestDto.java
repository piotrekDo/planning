package com.piotrdomagalski.planning.app_user;

import java.util.Objects;

/**
 * DTO class used as request body for requesting for token in order to change user password.
 */

public class PasswordChangeRequestDto {
    private String username;
    private String email;

    public PasswordChangeRequestDto() {
    }

    public PasswordChangeRequestDto(String username, String email) {
        this.username = username;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PasswordChangeRequestDto)) return false;
        PasswordChangeRequestDto that = (PasswordChangeRequestDto) o;
        return Objects.equals(username, that.username) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
