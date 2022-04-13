package com.example.istant.model;

import java.sql.Timestamp;

/**
 * entity 'chat' of the database
 */
public class Chat {
    private final String id;
    private final Timestamp createdAt;
    private String displayName;
    private String message;
    private String photoUrl;
    private final String uid;

    public Chat(String id, Timestamp createdAt, String displayName,
                String message, String photoUrl, String uid) {
        this.id = id;
        this.createdAt = createdAt;
        this.displayName = displayName;
        this.message = message;
        this.photoUrl = photoUrl;
        this.uid = uid;
    }
}
