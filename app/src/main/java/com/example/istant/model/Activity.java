package com.is.istant.model;

import java.sql.Timestamp;
import java.util.List;

/**
 * entity 'activity' of the database
 */
public class Activity {
    private final String id;
    private String nameActivity;
    private String address;
    private final Timestamp dateStart;
    private final Timestamp dateEnd;
    private String description;
    private List<String> personInCharge;
    private List<String> photoEvent;

    public Activity(String id, String nameActivity, String address,
                    Timestamp dateStart, Timestamp dateEnd, String description,
                    List<String> personInCharge, List<String> photoEvent) {
        this.id = id;
        this.nameActivity = nameActivity;
        this.address = address;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.description = description;
        this.personInCharge = personInCharge;
        this.photoEvent = photoEvent;
    }

    public String getId() {
        return id;
    }
}
