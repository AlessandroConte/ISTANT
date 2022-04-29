package com.example.istant.model;

import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * entity 'child' of the database
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

    public Child(){};

    // constructor
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

    // getter and setter
    public String getId() {
        return id;
    }

    public List<String> getAllergy() {
        return allergy;
    }

    public void setAllergy(List<String> allergy) {
        this.allergy = allergy;
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

    // methods
    /**
     * childUser returns all the children managed by the user
     * @param idUser String
     * @param lc List<Child>
     * @return ArrayList<Child>
     */
    public static ArrayList<Child> childUser(String idUser, List<Child> lc) {
        ArrayList<Child> result = new ArrayList<>();
        for (Child c: lc) {
            if (c.getParent().equals(idUser)) {
                result.add(c);
            }
        }
        return result;
    }

    /**
     * getChild returns the Child's object given its id
     * @param idChild String
     * @param lc List<Child>
     * @return Child
     */
    public static Child getChild(String idChild, List<Child> lc) {
        for (Child c: lc) {
            if (c.getId().equals(idChild)) {
                return c;
            }
        }
        return null;
    }
}
