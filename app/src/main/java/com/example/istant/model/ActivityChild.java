package com.is.istant.model;

/**
 * entity 'activityChild' of the database
 */
public class ActivityChild {
    private final String id;
    private final String idActivity;
    private final String idChild;

    public ActivityChild(String id, String idActivity, String idChild) {
        this.id = id;
        this.idActivity = idActivity;
        this.idChild = idChild;
    }
}
