package com.piotrdomagalski.planning.carrier;

public class CarrierNewUpdateDTO {
    private String sap;
    private String name;
    private String origin;
    private double rate;

    public CarrierNewUpdateDTO(String sap, String name, String origin, double rate) {
        this.sap = sap;
        this.name = name;
        this.origin = origin;
        this.rate = rate;
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

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
