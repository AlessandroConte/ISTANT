package com.is.istant.model;

import java.sql.Timestamp;
import java.util.List;

/**
 * entity 'activity' of the database
 */
public class Activity {
    private String nameActivity;
    private String address;
    private final Timestamp dateStart;
    private final Timestamp dateEnd;
    private String description;
    private List<String> personInCharge;
    private List<String> photoEvent;

    public Activity(String nameActivity, String address, Timestamp dateStart, Timestamp dateEnd, String description, List<String> personInCharge, List<String> photoEvent) {
        this.nameActivity = nameActivity;
        this.address = address;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.description = description;
        this.personInCharge = personInCharge;
        this.photoEvent = photoEvent;
    }
}
