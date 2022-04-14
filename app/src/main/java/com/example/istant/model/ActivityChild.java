package com.example.istant.model;

import java.util.ArrayList;
import java.util.List;

/**
 * entity 'activityChild' of the database
 */
public class ActivityChild {
    private final String id;
    private final String idActivity;
    private final String idChild;

    // constructor
    public ActivityChild(String id, String idActivity, String idChild) {
        this.id = id;
        this.idActivity = idActivity;
        this.idChild = idChild;
    }

    // getter and setter
    public String getId() {
        return id;
    }

    public String getIdActivity() {
        return idActivity;
    }

    public String getIdChild() {
        return idChild;
    }

    // methods
    /**
     * getChildrenFromActivity returns children involved in an activity
     * @param a Activity
     * @param lac List<ActivityChild>
     * @param lc List<Child>
     * @return ArrayList<Child>
     */
    public static ArrayList<Child> getChildrenFromActivity(Activity a, List<ActivityChild> lac,
                                                           List<Child> lc) {
        ArrayList<Child> result = new ArrayList<>();
        for (ActivityChild ac: lac) {
            if (ac.getIdActivity().equals(a.getId())) {
                result.add(Child.getChild(ac.getIdChild(), lc));
            }
        }
        return result;
    }
}
