package com.piotrdomagalski.planning.truck_driver;


import com.piotrdomagalski.planning.constraint.DigitsOnly;
import com.piotrdomagalski.planning.constraint.IdDocumentConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

public class TruckDriverNewUpdateDTO {
    @NotBlank(groups = AddDriver.class, message = "Driver's name cannot be blank!")
    @Size(groups = {AddDriver.class, UpdateDriver.class}, min = 3, max = 100, message = "Driver's name must be between 3 anc 100 characters")
    private String fullName;

    @NotBlank(groups = AddDriver.class, message = "Driver's tel cannot be blank!")
    @Size(groups = {AddDriver.class, UpdateDriver.class}, min = 9, max = 9, message = "Driver's tel should be 9 digits, NO SEPARATORS")
    @DigitsOnly(groups = {AddDriver.class, UpdateDriver.class}, message = "Driver's tel can be digits only")
    private String tel;

    @NotBlank(groups = AddDriver.class, message = "Driver's id cannot be blank!")
    @Size(groups = {AddDriver.class, UpdateDriver.class}, min = 8, max = 9, message = "Driver's id should be 8 characters if passport, 9 if ID document, NO SEPARATORS")
    @IdDocumentConstraint(groups = {AddDriver.class, UpdateDriver.class}, message = "ID document has to start with letters, no separators")
    private String idDocument;

    public TruckDriverNewUpdateDTO(String fullName, String tel, String idDocument) {
        this.fullName = fullName;
        this.tel = tel;
        this.idDocument = idDocument;
    }

    @Override
    public String toString() {
        return "TruckDriverNewUpdateDTO{" +
                "fullName='" + fullName + '\'' +
                ", tel='" + tel + '\'' +
                ", idDocument='" + idDocument + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TruckDriverNewUpdateDTO)) return false;
        TruckDriverNewUpdateDTO that = (TruckDriverNewUpdateDTO) o;
        return Objects.equals(fullName, that.fullName) && Objects.equals(tel, that.tel) && Objects.equals(idDocument, that.idDocument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, tel, idDocument);
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

interface AddDriver {
}

interface UpdateDriver {
}