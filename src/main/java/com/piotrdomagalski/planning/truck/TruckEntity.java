package com.piotrdomagalski.planning.truck;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.piotrdomagalski.planning.app.DatabaseEntity;
import com.piotrdomagalski.planning.app_user.AppUser;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.tautliner.TautlinerEntity;
import com.piotrdomagalski.planning.truck_driver.TruckDriverEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entity model class for trucks.
 */

@Entity
public class TruckEntity extends DatabaseEntity implements Comparable<TruckEntity> {
    private String truckPlates;
    private Boolean isMega;
    @ManyToOne
    @JsonBackReference(value = "carrier-truck")
    private CarrierEntity carrier;
    @OneToOne
    @JsonBackReference(value = "truck-driver")
    private TruckDriverEntity truckDriver;
    @OneToOne
    @JsonBackReference(value = "truck-taut")
    private TautlinerEntity tautliner;
    @ManyToMany(mappedBy = "favoritesTrucks")
    List<AppUser> appUser = new ArrayList<>();

    public TruckEntity() {
    }

    public TruckEntity(Long id, String truckPlates, Boolean isMega, CarrierEntity carrier, TruckDriverEntity truckDriver, TautlinerEntity tautliner) {
        super(id);
        this.truckPlates = truckPlates;
        this.isMega = isMega;
        this.carrier = carrier;
        this.truckDriver = truckDriver;
        this.tautliner = tautliner;
    }

    public TruckEntity(String truckPlates, Boolean isMega, CarrierEntity carrier, TruckDriverEntity truckDriver, TautlinerEntity tautliner) {
        this.truckPlates = truckPlates;
        this.isMega = isMega;
        this.carrier = carrier;
        this.truckDriver = truckDriver;
        this.tautliner = tautliner;
    }

    public static TruckEntity newTruck(String truckPlates, Boolean isMega) {
        return new TruckEntity(truckPlates, isMega, null, null, null);
    }

    @Override
    public String toString() {
        return "TruckEntity{" +
                super.toString() +
                "truckPlates='" + truckPlates + '\'' +
                ", isMega=" + isMega +
                ", carrier=" + (carrier != null ? carrier.getName() : " no carrier") +
                ", truckDriver=" + (truckDriver != null ? truckDriver.getFullName() : " no driver") +
                ", tautliner=" + (tautliner != null ? tautliner.getTautlinerPlates() : " no tautliner") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TruckEntity)) return false;
        if (!super.equals(o)) return false;
        TruckEntity that = (TruckEntity) o;
        return Objects.equals(truckPlates, that.truckPlates) && Objects.equals(isMega, that.isMega) && Objects.equals(carrier, that.carrier) && Objects.equals(truckDriver, that.truckDriver) && Objects.equals(tautliner, that.tautliner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), truckPlates, isMega, carrier, truckDriver, tautliner);
    }

    @Override
    public int compareTo(TruckEntity truck2) {
        return this.getTruckPlates().compareTo(truck2.getTruckPlates());
    }

    public String getTruckPlates() {
        return truckPlates;
    }

    public void setTruckPlates(String truckPlates) {
        this.truckPlates = truckPlates;
    }

    public Boolean getMega() {
        return isMega;
    }

    public void setMega(Boolean mega) {
        isMega = mega;
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
