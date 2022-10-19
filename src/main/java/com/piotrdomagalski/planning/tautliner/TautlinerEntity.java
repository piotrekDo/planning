package com.piotrdomagalski.planning.tautliner;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.piotrdomagalski.planning.app.DatabaseEntity;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.truck.TruckEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity class for tautliners.
 */

@Entity(name = "tautliners")
public class TautlinerEntity extends DatabaseEntity implements Comparable<TautlinerEntity> {
    private Boolean isXpo;
    private String tautlinerPlates;
    private LocalDateTime techInspection;
    @ManyToOne
    @JsonBackReference
    private CarrierEntity carrier;
    @OneToOne
    @JsonBackReference(value = "truck-taut")
    private TruckEntity truck;

    public TautlinerEntity() {
    }

    public TautlinerEntity(Boolean isXpo, String tautlinerPlates, LocalDateTime techInspection, CarrierEntity carrier, TruckEntity truck) {
        this.isXpo = isXpo;
        this.tautlinerPlates = tautlinerPlates;
        this.techInspection = techInspection;
        this.carrier = carrier;
        this.truck = truck;
    }

    public TautlinerEntity(Long id, Boolean isXpo, String tautlinerPlates, LocalDateTime techInspection, CarrierEntity carrier, TruckEntity truck) {
        super(id);
        this.isXpo = isXpo;
        this.tautlinerPlates = tautlinerPlates;
        this.techInspection = techInspection;
        this.carrier = carrier;
        this.truck = truck;
    }

    public static TautlinerEntity newTautliner(Boolean isXpo, String tautlinerPlates, LocalDateTime techInspection) {
        return new TautlinerEntity(isXpo, tautlinerPlates, techInspection, null, null);
    }

    @Override
    public String toString() {
        return "TautlinerEntity{" +
                super.toString() +
                "isXpo=" + isXpo +
                ", tautlinerPlates='" + tautlinerPlates + '\'' +
                ", techInspection=" + techInspection +
                ", carrier=" + (carrier != null ? carrier.getName() : " no carrier") +
                ", truck=" + (truck != null ? truck.getTruckPlates() : " no truck") +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TautlinerEntity)) return false;
        if (!super.equals(o)) return false;
        TautlinerEntity that = (TautlinerEntity) o;
        return Objects.equals(tautlinerPlates, that.tautlinerPlates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tautlinerPlates);
    }

    @Override
    public int compareTo(TautlinerEntity tautliner2) {
        return this.tautlinerPlates.compareTo(tautliner2.getTautlinerPlates());
    }

    public Boolean getXpo() {
        return isXpo;
    }

    public void setXpo(Boolean xpo) {
        isXpo = xpo;
    }

    public String getTautlinerPlates() {
        return tautlinerPlates;
    }

    public void setTautlinerPlates(String tautlinerPlates) {
        this.tautlinerPlates = tautlinerPlates;
    }

    public LocalDateTime getTechInspection() {
        return techInspection;
    }

    public void setTechInspection(LocalDateTime techInspection) {
        this.techInspection = techInspection;
    }

    public CarrierEntity getCarrier() {
        return carrier;
    }

    public void setCarrier(CarrierEntity carrier) {
        this.carrier = carrier;
    }

    public TruckEntity getTruck() {
        return truck;
    }

    public void setTruck(TruckEntity truck) {
        this.truck = truck;
    }
}
