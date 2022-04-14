package com.example.istant.model;

/**
 * entity 'takenLoan' of the database
 */
public class TakenLoan {
    private final String id;
    private final String idLoan;
    private int taken;
    private final String uid;

    // constructor
    public TakenLoan(String id, String idLoan, int taken, String uid) {
        this.id = id;
        this.idLoan = idLoan;
        this.taken = taken;
        this.uid = uid;
    }

    // getter and setter
    public String getId() {
        return id;
    }

    public String getIdLoan() {
        return idLoan;
    }

    public int getTaken() {
        return taken;
    }

    public void setTaken(int taken) {
        this.taken = taken;
    }

    public String getUid() {
        return uid;
    }


    // methods
}
