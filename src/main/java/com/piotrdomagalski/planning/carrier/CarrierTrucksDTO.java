package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.truck.TruckInfoDTO;

import java.util.List;
import java.util.Objects;

/**
 * DTO class returning all trucks available for specific carrier along with basic carrier information.
 */

public class CarrierTrucksDTO {
    private String sap;
    private String name;
    private String origin;
    private double rate;
    private List<TruckInfoDTO> trucks;

    public CarrierTrucksDTO(String sap, String name, String origin, double rate, List<TruckInfoDTO> trucks) {
        this.sap = sap;
        this.name = name;
        this.origin = origin;
        this.rate = rate;
        this.trucks = trucks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarrierTrucksDTO)) return false;
        CarrierTrucksDTO that = (CarrierTrucksDTO) o;
        return Double.compare(that.rate, rate) == 0 && Objects.equals(sap, that.sap) && Objects.equals(name, that.name) && Objects.equals(origin, that.origin) && Objects.equals(trucks, that.trucks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sap, name, origin, rate, trucks);
    }

    @Override
    public String toString() {
        return "CarrierTrucksDTO{" +
                "sap='" + sap + '\'' +
                ", name='" + name + '\'' +
                ", origin='" + origin + '\'' +
                ", rate=" + rate +
                ", trucks=" + trucks +
                '}';
    }

    public String getSap() {
        return sap;
    }

    public void setSap(String sap) {
        this.sap = sap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public List<TruckInfoDTO> getTrucks() {
        return trucks;
    }

    public void setTrucks(List<TruckInfoDTO> trucks) {
        this.trucks = trucks;
    }
}
