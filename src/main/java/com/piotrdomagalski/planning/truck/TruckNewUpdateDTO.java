package com.piotrdomagalski.planning.truck;

public class TruckNewUpdateDTO {
    private String truckPlates;
    private Boolean isMega;

    public TruckNewUpdateDTO(String truckPlates, Boolean isMega) {
        this.truckPlates = truckPlates;
        this.isMega = isMega;
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
