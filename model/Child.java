package com.is.istant.model;

import java.sql.Timestamp;
import java.util.List;

/**
 * entity 'child' of the database
 */
public class Child {
    private List<String> allergy;
    private final Timestamp dateBorn;
    private int gender;
    private String info;
    private String name;
    private String surname;
    private final User parent;

    public Child(List<String> allergy, Timestamp dateBorn, int gender, String info, String name, String surname, User parent) {
        this.allergy = allergy;
        this.dateBorn = dateBorn;
        this.gender = gender;
        this.info = info;
        this.name = name;
        this.surname = surname;
        this.parent = parent;
    }
}
