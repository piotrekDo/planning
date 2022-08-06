package com.piotrdomagalski.planning.truck;

import java.time.LocalDateTime;
import java.util.Objects;

public class TruckInfoDTO {
    private String truckPlates;
    private Boolean isMega;
    private String carrierSap;
    private String carrierName;
    private Long driverid;
    private String driverFullName;
    private String driverTel;
    private String driverIdDocument;
    private String tautlinerPlates;
    private LocalDateTime tautlinerTechInsp;
    private Boolean tautlinerIsXpo;

    public TruckInfoDTO(String truckPlates, Boolean isMega, String carrierSap, String carrierName, Long driverid, String driverFullName, String driverTel, String driverIdDocument, String tautlinerPlates, LocalDateTime tautlinerTechInsp, Boolean tautlinerIsXpo) {
        this.truckPlates = truckPlates;
        this.isMega = isMega;
        this.carrierSap = carrierSap;
        this.carrierName = carrierName;
        this.driverid = driverid;
        this.driverFullName = driverFullName;
        this.driverTel = driverTel;
        this.driverIdDocument = driverIdDocument;
        this.tautlinerPlates = tautlinerPlates;
        this.tautlinerTechInsp = tautlinerTechInsp;
        this.tautlinerIsXpo = tautlinerIsXpo;
    }

    @Override
    public String toString() {
        return "TruckInfoDTO{" +
                "truckPlates='" + truckPlates + '\'' +
                ", isMega=" + isMega +
                ", carrierSap='" + carrierSap + '\'' +
                ", carrierName='" + carrierName + '\'' +
                ", driverid=" + driverid +
                ", driverFullName='" + driverFullName + '\'' +
                ", driverTel='" + driverTel + '\'' +
                ", driverIdDocument='" + driverIdDocument + '\'' +
                ", tautlinerPlates='" + tautlinerPlates + '\'' +
                ", tautlinerTechInsp=" + tautlinerTechInsp +
                ", tautlinerIsXpo=" + tautlinerIsXpo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TruckInfoDTO)) return false;
        TruckInfoDTO that = (TruckInfoDTO) o;
        return Objects.equals(truckPlates, that.truckPlates) && Objects.equals(isMega, that.isMega) && Objects.equals(carrierSap, that.carrierSap) && Objects.equals(carrierName, that.carrierName) && Objects.equals(driverid, that.driverid) && Objects.equals(driverFullName, that.driverFullName) && Objects.equals(driverTel, that.driverTel) && Objects.equals(driverIdDocument, that.driverIdDocument) && Objects.equals(tautlinerPlates, that.tautlinerPlates) && Objects.equals(tautlinerTechInsp, that.tautlinerTechInsp) && Objects.equals(tautlinerIsXpo, that.tautlinerIsXpo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(truckPlates, isMega, carrierSap, carrierName, driverid, driverFullName, driverTel, driverIdDocument, tautlinerPlates, tautlinerTechInsp, tautlinerIsXpo);
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

    public String getCarrierSap() {
        return carrierSap;
    }

    public void setCarrierSap(String carrierSap) {
        this.carrierSap = carrierSap;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public Long getDriverid() {
        return driverid;
    }

    public void setDriverid(Long driverid) {
        this.driverid = driverid;
    }

    public String getDriverFullName() {
        return driverFullName;
    }

    public void setDriverFullName(String driverFullName) {
        this.driverFullName = driverFullName;
    }

    public String getDriverTel() {
        return driverTel;
    }

    public void setDriverTel(String driverTel) {
        this.driverTel = driverTel;
    }

    public String getDriverIdDocument() {
        return driverIdDocument;
    }

    public void setDriverIdDocument(String driverIdDocument) {
        this.driverIdDocument = driverIdDocument;
    }

    public String getTautlinerPlates() {
        return tautlinerPlates;
    }

    public void setTautlinerPlates(String tautlinerPlates) {
        this.tautlinerPlates = tautlinerPlates;
    }

    public LocalDateTime getTautlinerTechInsp() {
        return tautlinerTechInsp;
    }

    public void setTautlinerTechInsp(LocalDateTime tautlinerTechInsp) {
        this.tautlinerTechInsp = tautlinerTechInsp;
    }

    public Boolean getTautlinerIsXpo() {
        return tautlinerIsXpo;
    }

    public void setTautlinerIsXpo(Boolean tautlinerIsXpo) {
        this.tautlinerIsXpo = tautlinerIsXpo;
    }
}
