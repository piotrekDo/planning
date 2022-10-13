package com.piotrdomagalski.planning.app_user;

import java.util.Objects;

/**
 * DTO class used in requests for changing password
 */

public class PasswordChangeDto {
    private String username;
    private String changePasswordToken;
    private String newPassword;

    public PasswordChangeDto() {
    }

    public PasswordChangeDto(String username, String changePasswordToken, String newPassword) {
        this.username = username;
        this.changePasswordToken = changePasswordToken;
        this.newPassword = newPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PasswordChangeDto)) return false;
        PasswordChangeDto that = (PasswordChangeDto) o;
        return Objects.equals(username, that.username) && Objects.equals(changePasswordToken, that.changePasswordToken) && Objects.equals(newPassword, that.newPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, changePasswordToken, newPassword);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getChangePasswordToken() {
        return changePasswordToken;
    }

    public void setChangePasswordToken(String changePasswordToken) {
        this.changePasswordToken = changePasswordToken;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
