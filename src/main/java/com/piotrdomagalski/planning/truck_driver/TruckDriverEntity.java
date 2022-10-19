package com.piotrdomagalski.planning.truck_driver;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.piotrdomagalski.planning.app.DatabaseEntity;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.truck.TruckEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Objects;

/**
 * Entity class for drivers.
 */

@Entity
public class TruckDriverEntity extends DatabaseEntity implements Comparable<TruckDriverEntity> {
    private String fullName;
    private String tel;
    private String idDocument;
    @ManyToOne
    @JsonBackReference
    private CarrierEntity carrier;
    @OneToOne
    @JsonBackReference(value = "truck-driver")
    private TruckEntity truck;

    public TruckDriverEntity() {
    }

    public TruckDriverEntity(String fullName, String tel, String idDocument, CarrierEntity carrier, TruckEntity truck) {
        this.fullName = fullName;
        this.tel = tel;
        this.idDocument = idDocument;
        this.carrier = carrier;
        this.truck = truck;
    }

    public TruckDriverEntity(Long id, String fullName, String tel, String idDocument, CarrierEntity carrier, TruckEntity truck) {
        super(id);
        this.fullName = fullName;
        this.tel = tel;
        this.idDocument = idDocument;
        this.carrier = carrier;
        this.truck = truck;
    }

    public static TruckDriverEntity newTruckDriver(String fullName, String tel, String idDocument) {
        return new TruckDriverEntity(fullName, tel, idDocument, null, null);
    }

    @Override
    public String toString() {
        return "TruckDriverEntity{" +
                super.toString() +
                "fullName='" + fullName + '\'' +
                ", tel='" + tel + '\'' +
                ", idDocument='" + idDocument + '\'' +
                ", carrier=" + (carrier.getName() != null ? carrier.getName() : null) +
                ", truck=" + (truck.getTruckPlates() != null ? truck.getTruckPlates() : null) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TruckDriverEntity)) return false;
        if (!super.equals(o)) return false;
        TruckDriverEntity that = (TruckDriverEntity) o;
        return Objects.equals(fullName, that.fullName) && Objects.equals(tel, that.tel) && Objects.equals(idDocument, that.idDocument) && Objects.equals(carrier, that.carrier) && Objects.equals(truck, that.truck);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fullName, tel, idDocument, carrier, truck);
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(String idDocument) {
        this.idDocument = idDocument;
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

    @Override
    public int compareTo(TruckDriverEntity driver2) {
        return this.fullName.compareTo(driver2.getFullName());
    }
}
