package com.piotrdomagalski.planning.app_user;

import java.util.List;
import java.util.Objects;

/**
 * DTO class acting as a format data returned for user requests, hides password
 */

public class AppUserDto {

    private Long id;
    private String username;
    private String userEmail;
    private List<String> userRoles;

    public AppUserDto(Long id, String username, String userEmail, List<String> userRoles) {
        this.id = id;
        this.username = username;
        this.userEmail = userEmail;
        this.userRoles = userRoles;
    }

    @Override
    public String toString() {
        return "AppUserDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userRoles=" + userRoles +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppUserDto)) return false;
        AppUserDto that = (AppUserDto) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(userEmail, that.userEmail) && Objects.equals(userRoles, that.userRoles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, userEmail, userRoles);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<String> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<String> userRoles) {
        this.userRoles = userRoles;
    }
}
