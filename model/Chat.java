package com.is.istant.model;

import java.sql.Timestamp;

/**
 * entity 'chat' of the database
 */
public class Chat {

    private final Timestamp createdAt;
    private String displayName;
    private String message;
    private String photoUrl;
    private final User uid;

    public Chat(Timestamp createdAt, String displayName, String message, String photoUrl, User uid) {
        this.createdAt = createdAt;
        this.displayName = displayName;
        this.message = message;
        this.photoUrl = photoUrl;
        this.uid = uid;
    }
}
