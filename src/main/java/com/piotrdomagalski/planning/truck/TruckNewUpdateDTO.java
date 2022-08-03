package com.piotrdomagalski.planning.truck;

public class TruckNewUpdateDTO {
    private String truckPlates;

    public TruckNewUpdateDTO(String truckPlates) {
        this.truckPlates = truckPlates;
    }

    public String getTruckPlates() {
        return truckPlates;
    }

    public void setTruckPlates(String truckPlates) {
        this.truckPlates = truckPlates;
    }
}
