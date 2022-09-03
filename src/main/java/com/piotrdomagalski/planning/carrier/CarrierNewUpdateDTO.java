package com.piotrdomagalski.planning.carrier;

import com.piotrdomagalski.planning.constraint.DigitsOnly;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Objects;

public class CarrierNewUpdateDTO {
    @NotBlank(groups = {AddCarrier.class}, message = "Carrier SAP cannot be blank!")
    @Length(groups = {AddCarrier.class, UpdateCarrier.class}, min = 6, max = 6, message = "SAP must be 6 chracters long")
    @DigitsOnly(groups = {AddCarrier.class, UpdateCarrier.class}, message = "SAP must be numberic!")
    private String sap;
    @NotBlank(groups = {AddCarrier.class}, message = "Name cannot be blank!")
    @Size(groups = {AddCarrier.class, UpdateCarrier.class}, min = 3, max = 100, message = "Name must be between 3 anc 100 characters")
    private String name;
    @NotBlank(groups = {AddCarrier.class}, message = "Origin cannot be blank!")
    @Size(groups = {AddCarrier.class, UpdateCarrier.class}, min = 3, max = 100, message = "Origin must be between 3 anc 100 characters")
    private String origin;
    @Positive(groups = {AddCarrier.class, UpdateCarrier.class}, message = "Rate cannot be negative!")
    private Double rate;

    public CarrierNewUpdateDTO(String sap, String name, String origin, Double rate) {
        this.sap = sap;
        this.name = name;
        this.origin = origin;
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "CarrierNewUpdateDTO{" +
                "sap='" + sap + '\'' +
                ", name='" + name + '\'' +
                ", origin='" + origin + '\'' +
                ", rate=" + rate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarrierNewUpdateDTO)) return false;
        CarrierNewUpdateDTO that = (CarrierNewUpdateDTO) o;
        return Objects.equals(sap, that.sap) && Objects.equals(name, that.name) && Objects.equals(origin, that.origin) && Objects.equals(rate, that.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sap, name, origin, rate);
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
}

interface AddCarrier {
}

interface UpdateCarrier {
}
