package com.piotrdomagalski.planning.truck_driver;

public class TruckDriverInfoDTO {
    long id;
    private String fullName;
    private String tel;
    private String idDocument;
    private String carrierName;
    private String carrierSap;
    private String truckPlates;

    public TruckDriverInfoDTO(long id, String fullName, String tel, String idDocument, String carrierName, String carrierSap, String truckPlates) {
        this.id = id;
        this.fullName = fullName;
        this.tel = tel;
        this.idDocument = idDocument;
        this.carrierName = carrierName;
        this.carrierSap = carrierSap;
        this.truckPlates = truckPlates;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
