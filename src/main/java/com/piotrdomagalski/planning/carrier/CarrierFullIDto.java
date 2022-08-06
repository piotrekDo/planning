package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.tautliner.TautlinerInfoDTO;
import com.piotrdomagalski.planning.truck.TruckInfoDTO;
import com.piotrdomagalski.planning.truck_driver.TruckDriverInfoDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CarrierFullIDto {
    private String sap;
    private String name;
    private String origin;
    private double rate;
    private List<TruckInfoDTO> trucks = new ArrayList<>();
    private List<TruckDriverInfoDTO> drivers = new ArrayList<>();
    private List<TautlinerInfoDTO> tautliners = new ArrayList<>();

    public CarrierFullIDto(String sap, String name, String origin, double rate, List<TruckInfoDTO> trucks,
                           List<TruckDriverInfoDTO> drivers, List<TautlinerInfoDTO> tautliners) {
        this.sap = sap;
        this.name = name;
        this.origin = origin;
        this.rate = rate;
        this.trucks = trucks;
        this.drivers = drivers;
        this.tautliners = tautliners;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarrierFullIDto)) return false;
        CarrierFullIDto that = (CarrierFullIDto) o;
        return Double.compare(that.rate, rate) == 0 && Objects.equals(sap, that.sap) && Objects.equals(name, that.name) && Objects.equals(origin, that.origin) && Objects.equals(trucks, that.trucks) && Objects.equals(drivers, that.drivers) && Objects.equals(tautliners, that.tautliners);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sap, name, origin, rate, trucks, drivers, tautliners);
    }

    @Override
    public String toString() {
        return "CarrierFullIDto{" +
                "sap='" + sap + '\'' +
                ", name='" + name + '\'' +
                ", origin='" + origin + '\'' +
                ", rate=" + rate +
                ", trucks=" + trucks +
                ", drivers=" + drivers +
                ", tautliners=" + tautliners +
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

    public List<TruckDriverInfoDTO> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<TruckDriverInfoDTO> drivers) {
        this.drivers = drivers;
    }

    public List<TautlinerInfoDTO> getTautliners() {
        return tautliners;
    }

    public void setTautliners(List<TautlinerInfoDTO> tautliners) {
        this.tautliners = tautliners;
    }
}
