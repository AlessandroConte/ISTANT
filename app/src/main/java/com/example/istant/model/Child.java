package com.example.istant.model;

import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *  Entity 'child' of the database
 */
public class Child {
    private String id;
    private List<String> allergy;
    private Timestamp dateBorn;
    private int gender;
    private String info;
    private String name;
    private String surname;
    private String parent;


    // CONSTRUCTORS
    public Child(){};

    public Child(String id, List<String> allergy, Timestamp dateBorn, int gender, String info, String name, String surname, String parent) {
        this.id = id;
        this.allergy = allergy;
        this.dateBorn = dateBorn;
        this.gender = gender;
        this.info = info;
        this.name = name;
        this.surname = surname;
        this.parent = parent;
    }


    // GETTERS AND SETTERS
    public String getId() {
        return id;
    }

    public Timestamp getDateBorn() {
        return dateBorn;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getParent() {
        return parent;
    }

}
