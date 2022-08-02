package com.piotrdomagalski.planning.truck_driver;


public class TruckDriverNewUpdateDTO {
    private String fullName;
    private String tel;
    private String idDocument;

    public TruckDriverNewUpdateDTO(String fullName, String tel, String idDocument) {
        this.fullName = fullName;
        this.tel = tel;
        this.idDocument = idDocument;
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
}
