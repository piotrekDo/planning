package com.piotrdomagalski.planning.app_user;

import java.util.Objects;

public class UsernameDto {
    private String username;

    public UsernameDto() {
    }

    public UsernameDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UsernameDto{" +
                "username='" + username + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsernameDto)) return false;
        UsernameDto that = (UsernameDto) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
