package com.piotrdomagalski.planning.truck;

import com.piotrdomagalski.planning.app.PlatesConstrraint;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class TruckNewUpdateDTO {
    @Size(groups = {AddTruck.class, UpdateTruck.class}, min = 3, max = 15, message = "Truck's plates must be between 3 and 15 characters")
    @PlatesConstrraint(groups = {AddTruck.class, UpdateTruck.class}, message = "Trucks's plates must start with 2-3 letters, eg. PO23211")
    private String truckPlates;
    @NotNull(groups = {AddTruck.class, UpdateTruck.class}, message = "Truck must be declared either mega or standard")
    private Boolean isMega;

    public TruckNewUpdateDTO(String truckPlates, Boolean isMega) {
        this.truckPlates = truckPlates;
        this.isMega = isMega;
    }

    @Override
    public String toString() {
        return "TruckNewUpdateDTO{" +
                "truckPlates='" + truckPlates + '\'' +
                ", isMega=" + isMega +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TruckNewUpdateDTO)) return false;
        TruckNewUpdateDTO that = (TruckNewUpdateDTO) o;
        return Objects.equals(truckPlates, that.truckPlates) && Objects.equals(isMega, that.isMega);
    }

    @Override
    public int hashCode() {
        return Objects.hash(truckPlates, isMega);
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
}

interface AddTruck {
}

interface UpdateTruck {
}

