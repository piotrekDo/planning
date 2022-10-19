package com.piotrdomagalski.planning.carrier;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.piotrdomagalski.planning.app.DatabaseEntity;
import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.truck.TruckEntity;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entity class for carriers.
 * @JsonManagedReference allows ending endless inception of calling another carriers within embedded entities
 */

@Entity(name = "carriers")
public class CarrierEntity extends DatabaseEntity implements Comparable<CarrierEntity> {
    private String sap;
    private String name;
    private String origin;
    private Double rate;
    @OneToMany(mappedBy = "carrier")
    @JsonManagedReference(value = "carrier-truck")
    private List<TruckEntity> trucks = new ArrayList<>();
    @OneToMany(mappedBy = "carrier")
    @JsonManagedReference
    private List<TruckDriverEntity> drivers = new ArrayList<>();
    @OneToMany(mappedBy = "carrier")
    @JsonManagedReference
    private List<TautlinerEntity> tautliners = new ArrayList<>();

    public CarrierEntity() {
    }

    public CarrierEntity(String sap, String name, String origin, Double rate, List<TruckEntity> trucks,
                         List<TruckDriverEntity> drivers, List<TautlinerEntity> tautliners) {
        this.sap = sap;
        this.name = name;
        this.origin = origin;
        this.rate = rate;
        this.trucks = trucks;
        this.drivers = drivers;
        this.tautliners = tautliners;
    }

    public CarrierEntity(Long id, String sap, String name, String origin, Double rate,
                         List<TruckEntity> trucks, List<TruckDriverEntity> drivers, List<TautlinerEntity> tautliners) {
        super(id);
        this.sap = sap;
        this.name = name;
        this.origin = origin;
        this.rate = rate;
        this.trucks = trucks;
        this.drivers = drivers;
        this.tautliners = tautliners;
    }

    public static CarrierEntity newCarrier(String sap, String name, String origin, Double rate) {
        return new CarrierEntity(sap, name, origin, rate,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    @Override
    public String toString() {
        return "CarrierEntity{" +
                super.toString() +
                "sap='" + sap + '\'' +
                ", name='" + name + '\'' +
                ", origin='" + origin + '\'' +
                ", rate=" + rate +
                ", trucks=" + trucks +
                ", drivers=" + drivers +
                ", tautliners=" + tautliners +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarrierEntity)) return false;
        if (!super.equals(o)) return false;
        CarrierEntity that = (CarrierEntity) o;
        return Objects.equals(sap, that.sap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sap);
    }

    @Override
    public int compareTo(CarrierEntity carrier2) {
        return this.name.compareTo(carrier2.getName());
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

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public List<TruckEntity> getTrucks() {
        return trucks;
    }

    public void setTrucks(List<TruckEntity> trucks) {
        this.trucks = trucks;
    }

    public List<TruckDriverEntity> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<TruckDriverEntity> drivers) {
        this.drivers = drivers;
    }

    public List<TautlinerEntity> getTautliners() {
        return tautliners;
    }

    public void setTautliners(List<TautlinerEntity> tautliners) {
        this.tautliners = tautliners;
    }
}
