package com.piotrdomagalski.planning.tautliner;

import com.piotrdomagalski.planning.constraint.PlatesConstrraint;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class TautlinerNewUpdateDTO {

    @NotNull(groups = {AddTautliner.class}, message = "Tautliner must be declared either XPO or not")
    private Boolean isXpo;
    @Size(groups = {AddTautliner.class, UpdateTautliner.class}, min = 3, max = 15, message = "Tautliner's plates must be between 3 and 15 characters")
    @PlatesConstrraint(groups = {AddTautliner.class, UpdateTautliner.class}, message = "Tautliner's plates must start with 2-3 letters, eg. PO23211")
    private String tautlinerPlates;
    @NotNull(groups = {AddTautliner.class}, message = "Tautliner shoud have technical inspection date declared")
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


    @Override
    public String toString() {
        return "TautlinerNewUpdateDTO{" +
                "isXpo=" + isXpo +
                ", tautlinerPlates='" + tautlinerPlates + '\'' +
                ", techInspection='" + techInspection + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TautlinerNewUpdateDTO)) return false;
        TautlinerNewUpdateDTO that = (TautlinerNewUpdateDTO) o;
        return Objects.equals(isXpo, that.isXpo) && Objects.equals(tautlinerPlates, that.tautlinerPlates) && Objects.equals(techInspection, that.techInspection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isXpo, tautlinerPlates, techInspection);
    }
}

interface AddTautliner{}
interface UpdateTautliner{}
