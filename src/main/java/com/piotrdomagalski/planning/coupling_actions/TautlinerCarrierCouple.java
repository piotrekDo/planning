package com.piotrdomagalski.planning.coupling_actions;

import java.util.Objects;

/**
 * DTO class required in coupling request to couple tautliner with carrier.
 */

public class TautlinerCarrierCouple {

    private String tautliner;
    private String carrierSap;

    public TautlinerCarrierCouple(String tautliner, String carrierSap) {
        this.tautliner = tautliner;
        this.carrierSap = carrierSap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TautlinerCarrierCouple)) return false;
        TautlinerCarrierCouple that = (TautlinerCarrierCouple) o;
        return Objects.equals(tautliner, that.tautliner) && Objects.equals(carrierSap, that.carrierSap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tautliner, carrierSap);
    }

    @Override
    public String toString() {
        return "TautlinerCarrierCouple{" +
                "tautliner='" + tautliner + '\'' +
                ", carrierSap='" + carrierSap + '\'' +
                '}';
    }

    public String getTautliner() {
        return tautliner;
    }

    public void setTautliner(String tautliner) {
        this.tautliner = tautliner;
    }

    public String getCarrierSap() {
        return carrierSap;
    }

    public void setCarrierSap(String carrierSap) {
        this.carrierSap = carrierSap;
    }
}
