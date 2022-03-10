package com.is.istant.model;

/**
 * entity 'activityChild' of the database
 */
public class ActivityChild {
    private final Activity idActivity;
    private final Child idChild;

    public ActivityChild(Activity idActivity, Child idChild) {
        this.idActivity = idActivity;
        this.idChild = idChild;
    }
}
