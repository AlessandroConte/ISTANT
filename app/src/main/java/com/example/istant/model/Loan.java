package com.example.istant.model;

import java.sql.Timestamp;

/**
 * entity 'loan' of the database
 */
public class Loan {
    private final String id;
    private final Timestamp dateStart;
    private final Timestamp dateEnd;
    private String description;
    private String nameLoan;
    private final String uid;

    public Loan(String id, Timestamp dateStart, Timestamp dateEnd,
                String description, String nameLoan, String uid) {
        this.id = id;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.description = description;
        this.nameLoan = nameLoan;
        this.uid = uid;
    }
}
