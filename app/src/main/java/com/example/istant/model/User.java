package com.example.istant.model;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * entity 'user' of the database
 */
public class User {
    private final String id;
    private String address;
    private final Timestamp dateBorn;
    private final String email;
    private final String fiscalCode;
    private int gender;
    private String photoUrl;
    private String name;
    private String surname;
    private final String telephoneNumber;

    // constructor
    public User(String id, String address, Timestamp dateBorn, String email,
                String fiscalCode, int gender, String photoUrl, String name,
                String surname, String telephoneNumber) {
        this.id = id;
        this.address = address;
        this.dateBorn = dateBorn;
        this.email = email;
        this.fiscalCode = fiscalCode;
        this.gender = gender;
        this.photoUrl = photoUrl;
        this.name = name;
        this.surname = surname;
        this.telephoneNumber = telephoneNumber;
    }

    // getter and setter
    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getId() {return id;}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getDateBorn() {
        return dateBorn;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name + " " + this.telephoneNumber;
    }

    // methods
    /**
     * isUserSubstring returns a list of users who match,
     * even partially, with the given string sub
     * @param sub String
     * @param lu List of User
     * @return ArrayList of User
     */
    public static ArrayList<User> isUserSubstring(String sub, List<User> lu) {
        ArrayList<User> result = new ArrayList<>();
        for (User u: lu) {
            String search_n = u.getName() + " " + u.getSurname();
            String search_s = u.getSurname() + " " + u.getName();
            String search_e = u.getEmail();
            if (search_n.matches("(.*)" + sub + "(.*)") ||
                    search_s.matches("(.*)" + sub + "(.*)") ||
                    search_e.matches("(.*)" + sub + "(.*)")) {
                result.add(u);
            }
        }
        return result;
    }
    
    /**
     * isUserDeleted returns the presence or absence of a user inside a list of User, which should
     * be coherent with the database, therefore is important that lu is updated
     * @param id String
     * @param lu List of User
     * @return boolean, which indicates the absence or presence of the user
     */
    public static boolean isUserDeleted(String id, List<User> lu) {
        boolean delete = true;
        for (User u: lu) {
            if (u.getId().equals(id)) {
                delete = false;
                break;
            }
        }
        return delete;
    }

    /**
     * getChild returns the User's object given its id
     * @param idUser String
     * @param lu List<User>
     * @return User
     */
    public static User getUser(String idUser, List<User> lu) {
        for (User u: lu) {
            if (u.getId().equals(idUser)) {
                return u;
            }
        }
        return null;
    }
}
