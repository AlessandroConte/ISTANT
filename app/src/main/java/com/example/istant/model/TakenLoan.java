package com.is.istant.model;

/**
 * entity 'takenLoan' of the database
 */
public class TakenLoan {
    private final String id;
    private final String idLoan;
    private int taken;
    private final String uid;

    public TakenLoan(String id, String idLoan, int taken, String uid) {
        this.id = id;
        this.idLoan = idLoan;
        this.taken = taken;
        this.uid = uid;
    }
}
