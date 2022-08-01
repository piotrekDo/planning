package com.piotrdomagalski.planning.truck;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.piotrdomagalski.planning.DatabaseEntity;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
public class TruckEntity extends DatabaseEntity implements Comparable<TruckEntity>{
    @Size(min = 3, max = 15, message = "Truck's plates must be between 3 and 15 characters")
    private String truckPlates;
    @ManyToOne
    @JsonBackReference
    private CarrierEntity carrier;
    @OneToOne
    @JsonBackReference
    private TruckDriverEntity truckDriver;
    @OneToOne
    @JsonBackReference
    private TautlinerEntity tautliner;

    public TruckEntity() {
    }

    public TruckEntity(String truckPlates, CarrierEntity carrier, TruckDriverEntity truckDriver, TautlinerEntity tautliner) {
        this.truckPlates = truckPlates;
        this.carrier = carrier;
        this.truckDriver = truckDriver;
        this.tautliner = tautliner;
    }

    public TruckEntity(Long id, String truckPlates, CarrierEntity carrier, TruckDriverEntity truckDriver, TautlinerEntity tautliner) {
        super(id);
        this.truckPlates = truckPlates;
        this.carrier = carrier;
        this.truckDriver = truckDriver;
        this.tautliner = tautliner;
    }

    public static TruckEntity newTruck(String truckPlates) {
        return new TruckEntity(truckPlates, null, null, null);
    }

    @Override
    public String toString() {
        return "TruckEntity{" +
                super.toString() +
                "truckPlates='" + truckPlates + '\'' +
                ", carrier=" + carrier +
                ", truckDriver=" + truckDriver +
                ", tautliner=" + tautliner +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TruckEntity)) return false;
        if (!super.equals(o)) return false;
        TruckEntity that = (TruckEntity) o;
        return Objects.equals(truckPlates, that.truckPlates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), truckPlates);
    }


    @Override
    public int compareTo(TruckEntity truck2) {
        return this.truckPlates.compareTo(truck2.getTruckPlates());
    }

    public String getTruckPlates() {
        return truckPlates;
    }

    public void setTruckPlates(String truckPlates) {
        this.truckPlates = truckPlates;
    }

    public CarrierEntity getCarrier() {
        return carrier;
    }

    public void setCarrier(CarrierEntity carrier) {
        this.carrier = carrier;
    }

    public TruckDriverEntity getTruckDriver() {
        return truckDriver;
    }

    public void setTruckDriver(TruckDriverEntity truckDriver) {
        this.truckDriver = truckDriver;
    }

    public TautlinerEntity getTautliner() {
        return tautliner;
    }

    public void setTautliner(TautlinerEntity tautliner) {
        this.tautliner = tautliner;
    }
}
