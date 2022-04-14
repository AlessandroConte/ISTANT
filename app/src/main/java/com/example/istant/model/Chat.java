package com.example.istant.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * entity 'chat' of the database
 */
public class Chat {
    private final String id;
    private final Timestamp createdAt;
    private final String displayName;
    private final String message;
    private final String photoUrl;
    private final String uid;

    // constructor
    public Chat(String id, Timestamp createdAt, String displayName,
                String message, String photoUrl, String uid) {
        this.id = id;
        this.createdAt = createdAt;
        this.displayName = displayName;
        this.message = message;
        this.photoUrl = photoUrl;
        this.uid = uid;
    }

    // getter and setter
    public String getId() {
        return id;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getMessage() {
        return message;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getUid() {
        return uid;
    }

    // methods
    /**
     * orderMessages returns the messages in chronological order
     * @param groupMessage ArrayList<Chat>
     * @return ArrayList<Chat>
     */
    public static ArrayList<Chat> orderMessages(ArrayList<Chat> groupMessage) {
        Collections.sort(groupMessage, new Comparator<Chat>() {
            @Override
            public int compare(Chat chat, Chat t1) {
                return Integer.compare(Integer.parseInt(chat.getCreatedAt().toString()), Integer.parseInt(t1.getCreatedAt().toString()));
            }
        });
        return groupMessage;
    }
}
