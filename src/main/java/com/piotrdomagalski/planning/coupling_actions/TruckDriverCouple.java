package com.piotrdomagalski.planning.coupling_actions;

import java.util.Objects;

public class TruckDriverCouple {
    private String truck;
    private Long driver;

    public TruckDriverCouple(String truck, Long driver) {
        this.truck = truck;
        this.driver = driver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TruckDriverCouple)) return false;
        TruckDriverCouple that = (TruckDriverCouple) o;
        return Objects.equals(truck, that.truck) && Objects.equals(driver, that.driver);
    }

    @Override
    public int hashCode() {
        return Objects.hash(truck, driver);
    }

    @Override
    public String toString() {
        return "TruckDriverCouple{" +
                "truck='" + truck + '\'' +
                ", driver=" + driver +
                '}';
    }

    public String getTruck() {
        return truck;
    }

    public void setTruck(String truck) {
        this.truck = truck;
    }

    public Long getDriver() {
        return driver;
    }

    public void setDriver(Long driver) {
        this.driver = driver;
    }
}
