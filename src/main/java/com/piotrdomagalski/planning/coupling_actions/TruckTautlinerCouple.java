package com.piotrdomagalski.planning.coupling_actions;

import java.util.Objects;

/**
 * DTO class used for coupling requests for coupling truck with tautliner.
 */

public class TruckTautlinerCouple {
    private String truck;
    private String tautliner;

    public TruckTautlinerCouple(String truck, String tautliner) {
        this.truck = truck;
        this.tautliner = tautliner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TruckTautlinerCouple)) return false;
        TruckTautlinerCouple that = (TruckTautlinerCouple) o;
        return Objects.equals(truck, that.truck) && Objects.equals(tautliner, that.tautliner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(truck, tautliner);
    }

    @Override
    public String toString() {
        return "TruckTautlinerCouple{" +
                "truck='" + truck + '\'' +
                ", tautliner='" + tautliner + '\'' +
                '}';
    }

    public String getTruck() {
        return truck;
    }

    public void setTruck(String truck) {
        this.truck = truck;
    }

    public String getTautliner() {
        return tautliner;
    }

    public void setTautliner(String tautliner) {
        this.tautliner = tautliner;
    }
}
