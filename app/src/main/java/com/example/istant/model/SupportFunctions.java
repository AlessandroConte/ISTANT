package com.example.istant.model;

import java.util.Random;

/**
 *  Class containing support functions
 */
public final class SupportFunctions {

    /**
     * generateRandomString generates a random string of 20 characters
     * @return a random string
     */
    public static String generateRandomString() {
        String letters = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm0123456789";
        Random rand = new Random();
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            int randIndex = rand.nextInt(letters.length());
            res.append(letters.charAt(randIndex));
        }
        return res.toString();
    }
}
