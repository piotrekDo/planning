package com.piotrdomagalski.planning.favorites;

import java.util.Objects;

public class AddNewTruckToFavoritesDTO {
    private String username;
    private String truck;

    public AddNewTruckToFavoritesDTO(String username, String truck) {
        this.username = username;
        this.truck = truck;
    }

    @Override
    public String toString() {
        return "AddNewTruckToFavoritesDTO{" +
                "username='" + username + '\'' +
                ", truck='" + truck + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddNewTruckToFavoritesDTO)) return false;
        AddNewTruckToFavoritesDTO that = (AddNewTruckToFavoritesDTO) o;
        return Objects.equals(username, that.username) && Objects.equals(truck, that.truck);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, truck);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTruck() {
        return truck;
    }

    public void setTruck(String truck) {
        this.truck = truck;
    }
}
