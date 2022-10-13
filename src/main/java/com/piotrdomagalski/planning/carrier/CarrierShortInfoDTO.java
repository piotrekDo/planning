package com.piotrdomagalski.planning.carrier;

/**
 * DTO class for carriers returning basic information about carrier and handful information about fleet.
 */

public class CarrierShortInfoDTO {
    private String sap;
    private String name;
    private String origin;
    private double rate;
    private int trucks;
    private int megas;

    public CarrierShortInfoDTO(String sap, String name, String origin, double rate, int trucks, int megas) {
        this.sap = sap;
        this.name = name;
        this.origin = origin;
        this.rate = rate;
        this.trucks = trucks;
        this.megas = megas;
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

    public int getTrucks() {
        return trucks;
    }

    public void setTrucks(int trucks) {
        this.trucks = trucks;
    }

    public int getMegas() {
        return megas;
    }

    public void setMegas(int megas) {
        this.megas = megas;
    }
}
