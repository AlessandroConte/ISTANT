package com.is.istant.model;

import java.sql.Timestamp;
import java.util.List;

/**
 * entity 'child' of the database
 */
public class Child {
    private final String id;
    private List<String> allergy;
    private final Timestamp dateBorn;
    private int gender;
    private String info;
    private String name;
    private String surname;
    private final String parent;

    public Child(String id, List<String> allergy, Timestamp dateBorn, int gender,
                 String info, String name, String surname, String parent) {
        this.id = id;
        this.allergy = allergy;
        this.dateBorn = dateBorn;
        this.gender = gender;
        this.info = info;
        this.name = name;
        this.surname = surname;
        this.parent = parent;
    }
}
