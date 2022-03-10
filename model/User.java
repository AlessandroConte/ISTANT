package com.is.istant.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * entity 'user' of the database
 */
public class User {
    private String address;
    private final Timestamp dateBorn;
    private final String email;
    private final String fiscalCode;
    private int gender;
    private String photoUrl;
    private String name;
    private String surname;
    private final int telephoneNumber;

    public User(String address, Timestamp dateBorn, String email, String fiscalCode, int gender, String photoUrl, String name, String surname, int telephoneNumber) {
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

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    /**
     * isUserSubstring returns a list of users who match,
     * even partially, with the given string sub
     * @param sub String
     * @param lu List of User
     * @return List of User
     */
    public static List<User> isUserSubstring(String sub, List<User> lu) {
        List<User> result = new ArrayList<>();
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
}
