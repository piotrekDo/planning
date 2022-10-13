package com.piotrdomagalski.planning.app_user;

import java.util.Objects;

/**
 * DTO class used as request body in order to assign or remove role from user.
 */

public class RoleToUserForm {
    private String username;
    private String roleName;

    public RoleToUserForm(String username, String roleName) {
        this.username = username;
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "RoleToUserForm{" +
                "username='" + username + '\'' +
                ", roleName='" + roleName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleToUserForm)) return false;
        RoleToUserForm that = (RoleToUserForm) o;
        return Objects.equals(username, that.username) && Objects.equals(roleName, that.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, roleName);
    }

    public RoleToUserForm() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
