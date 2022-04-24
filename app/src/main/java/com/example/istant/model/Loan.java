package com.example.istant.model;

import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * entity 'loan' of the database
 */
public class Loan {
    private final String id;
    private final Timestamp dateStart;
    private final Timestamp dateEnd;
    private String photoLoan;
    private String description;
    private String nameLoan;
    private final String uid;

    // constructor
    public Loan(String id, Timestamp dateStart, Timestamp dateEnd,
                String photoLoan, String description, String nameLoan, String uid) {
        this.id = id;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.photoLoan = photoLoan;
        this.description = description;
        this.nameLoan = nameLoan;
        this.uid = uid;
    }

    // getter and setter
    public String getId() {
        return id;
    }

    public Timestamp getDateStart() {
        return dateStart;
    }

    public Timestamp getDateEnd() {
        return dateEnd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNameLoan() {
        return nameLoan;
    }

    public void setNameLoan(String nameLoan) {
        this.nameLoan = nameLoan;
    }

    public String getUid() {
        return uid;
    }

    public String getPhotoLoan() {
        return photoLoan;
    }

    public void setPhotoLoan(String photoLoan) {
        this.photoLoan = photoLoan;
    }

    // methods
    /**
     * loansUser returns all the loans that the user has created
     * @param idUser String
     * @param ll List<Loan>
     * @return ArrayList<Loan>
     */
    public static ArrayList<Loan> loansUser(String idUser, List<Loan> ll) {
        ArrayList<Loan> result = new ArrayList<>();
        for (Loan l: ll) {
            if (l.getUid().equals(idUser)) {
                result.add(l);
            }
        }
        return result;
    }
}
