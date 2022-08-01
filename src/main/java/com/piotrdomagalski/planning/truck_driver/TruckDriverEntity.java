package com.piotrdomagalski.planning.truck_driver;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.piotrdomagalski.planning.DatabaseEntity;
import com.piotrdomagalski.planning.carrier.CarrierEntity;
import com.piotrdomagalski.planning.truck.TruckEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
public class TruckDriverEntity extends DatabaseEntity implements Comparable<TruckDriverEntity> {
    @NotBlank(message = "Driver's name cannot be blank!")
    @Size(min = 3, max = 100, message = "Driver's name must be between 3 anc 100 characters")
    private String fullName;
    @NotBlank(message = "Driver's tel cannot be blank!")
    @Size(min = 9, max = 9, message = "Driver's tel should be 9 characters, NO SEPARATORS")
    private String tel;
    @NotBlank(message = "Driver's id cannot be blank!")
    @Size(min = 8, max = 9, message = "Driver's id should be 8 characters if passport, 9 if ID document, NO SEPARATORS")
    private String idDocument;
    @ManyToOne
    @JsonBackReference
    private CarrierEntity carrier;
    @OneToOne
    @JsonBackReference
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

    @Override
    public String toString() {
        return "TruckDriverEntity{" +
                super.toString() +
                "fullName='" + fullName + '\'' +
                ", tel='" + tel + '\'' +
                ", idDocument='" + idDocument + '\'' +
                ", carrier=" + carrier +
                ", truck=" + truck +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TruckDriverEntity)) return false;
        if (!super.equals(o)) return false;
        TruckDriverEntity that = (TruckDriverEntity) o;
        return Objects.equals(fullName, that.fullName) && Objects.equals(tel, that.tel) && Objects.equals(idDocument, that.idDocument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fullName, tel, idDocument);
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
