package com.example.istant.model;

/**
 *  Entity 'scoreActivityUser' of the database
 */
public class ScoreActivityUser {
    private final String id;
    private float score;
    private String comment;
    private final String idActivity;
    private final String uid;

    // CONSTRUCTOR
    public ScoreActivityUser(String id, float score, String comment, String idActivity, String uid) {
        this.id = id;
        this.score = score;
        this.comment = comment;
        this.idActivity = idActivity;
        this.uid = uid;
    }

    // GETTERS AND SETTERS
    public float getScore() {
        return score;
    }

    public String getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public String getUid() {
        return uid;
    }

}
