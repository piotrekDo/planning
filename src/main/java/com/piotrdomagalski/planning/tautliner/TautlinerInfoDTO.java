package com.piotrdomagalski.planning.tautliner;

import java.time.LocalDateTime;

/**
 * DTO class containing simplified information about not only the trailer itself but also carrier and truck.
 */

public class TautlinerInfoDTO {
    private String tautlinerPlates;
    private LocalDateTime techInspection;
    private boolean isXpo;
    private String carrierName;
    private String carrierSap;
    private String truckPlates;

    public TautlinerInfoDTO(String tautlinerPlates, LocalDateTime techInspection, boolean isXpo, String carrierName, String carrierSap, String truckPlates) {
        this.tautlinerPlates = tautlinerPlates;
        this.techInspection = techInspection;
        this.isXpo = isXpo;
        this.carrierName = carrierName;
        this.carrierSap = carrierSap;
        this.truckPlates = truckPlates;
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

    public boolean isXpo() {
        return isXpo;
    }

    public void setXpo(boolean xpo) {
        isXpo = xpo;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public String getCarrierSap() {
        return carrierSap;
    }

    public void setCarrierSap(String carrierSap) {
        this.carrierSap = carrierSap;
    }

    public String getTruckPlates() {
        return truckPlates;
    }

    public void setTruckPlates(String truckPlates) {
        this.truckPlates = truckPlates;
    }
}
