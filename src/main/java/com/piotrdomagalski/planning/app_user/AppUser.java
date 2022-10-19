package com.piotrdomagalski.planning.app_user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.piotrdomagalski.planning.truck.TruckEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Entity class for application user
 */

@Entity(name = "users")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String userEmail;
    private String userPassword;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<UserRole> userRoles = new ArrayList<>();
    @ManyToMany
    @JoinTable(name = "favorites")
    private List<TruckEntity> favoritesTrucks = new ArrayList<>();

    public AppUser() {
    }

    public AppUser(String username, String userEmail, String userPassword) {
        this.username = username;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public AppUser(Long id, String username, String userEmail, String userPassword, Collection<UserRole> userRoles) {
        this.id = id;
        this.username = username;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userRoles = userRoles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppUser)) return false;
        AppUser appUser = (AppUser) o;
        return Objects.equals(id, appUser.id) && Objects.equals(username, appUser.username) && Objects.equals(userEmail, appUser.userEmail) && Objects.equals(userPassword, appUser.userPassword) && Objects.equals(userRoles, appUser.userRoles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, userEmail, userPassword, userRoles, favoritesTrucks);
    }

    public void setFavoritesTrucks(List<TruckEntity> favoritesTrucks) {
        this.favoritesTrucks = favoritesTrucks;
    }

    public List<TruckEntity> getFavoritesTrucks() {
        return favoritesTrucks;
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

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Collection<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Collection<UserRole> userRoles) {
        this.userRoles = userRoles;
    }
}