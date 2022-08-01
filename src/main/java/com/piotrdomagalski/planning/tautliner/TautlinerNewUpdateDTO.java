package com.piotrdomagalski.planning.tautliner;

public class TautlinerNewUpdateDTO {

    private Boolean isXpo;
    private String tautlinerPlates;
    private String techInspection;

    public TautlinerNewUpdateDTO(Boolean isXpo, String tautlinerPlates, String techInspection) {
        this.isXpo = isXpo;
        this.tautlinerPlates = tautlinerPlates;
        this.techInspection = techInspection;
    }

    public Boolean getXpo() {
        return isXpo;
    }

    public void setXpo(Boolean xpo) {
        isXpo = xpo;
    }

    public String getTautlinerPlates() {
        return tautlinerPlates;
    }

    public void setTautlinerPlates(String tautlinerPlates) {
        this.tautlinerPlates = tautlinerPlates;
    }

    public String getTechInspection() {
        return techInspection;
    }

    public void setTechInspection(String techInspection) {
        this.techInspection = techInspection;
    }
}
