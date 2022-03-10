package com.is.istant.model;

/**
 * entity 'takenLoan' of the database
 */
public class TakenLoan {
    private final Loan idLoan;
    private int taken;
    private final User uid;

    public TakenLoan(Loan idLoan, int taken, User uid) {
        this.idLoan = idLoan;
        this.taken = taken;
        this.uid = uid;
    }
}
