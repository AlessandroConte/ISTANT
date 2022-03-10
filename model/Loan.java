package com.is.istant.model;

import java.sql.Timestamp;

/**
 * entity 'loan' of the database
 */
public class Loan {
    private final Timestamp dateStart;
    private final Timestamp dateEnd;
    private String description;
    private String nameLoan;
    private final User uid;

    public Loan(Timestamp dateStart, Timestamp dateEnd, String description, String nameLoan, User uid) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.description = description;
        this.nameLoan = nameLoan;
        this.uid = uid;
    }
}
