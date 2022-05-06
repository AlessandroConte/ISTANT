package com.example.istant.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * entity 'activity' of the database
 */
public class Activity implements Parcelable {
    private String id;
    private String nameActivity;
    private String address;
    private Timestamp dateStart;
    private Timestamp dateEnd;
    private String description;
    private List<String> personInCharge;
    private String photoEvent;

    // constructor

    public Activity () {}

    public Activity(String id, String nameActivity, String address, Timestamp dateStart, Timestamp dateEnd, String description, List<String> personInCharge, String photoEvent) {
        this.id = id;
        this.nameActivity = nameActivity;
        this.address = address;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.description = description;
        this.personInCharge = personInCharge;
        this.photoEvent = photoEvent;
    }

    public Activity(Parcel parcel) {
        this.id = parcel.readString();
        // this.nameActivity = parcel.readString();
        this.address = parcel.readString();
        this.description = parcel.readString();
        this.personInCharge = parcel.createStringArrayList();
        this.photoEvent = parcel.readString();
    }

    // getter and setter
    public String getId() {
        return id;
    }

    public String getNameActivity() {
        return nameActivity;
    }

    public void setNameActivity(String nameActivity) {
        this.nameActivity = nameActivity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getDateStart() {
        return dateStart;
    }

    public Timestamp getDateEnd() {
        return dateEnd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getPersonInCharge() {
        return personInCharge;
    }

    public void setPersonInCharge(List<String> personInCharge) {
        this.personInCharge = personInCharge;
    }

    public String getPhotoEvent() {
        return photoEvent;
    }

    public void setPhotoEvent(String photoEvent) {
        this.photoEvent = photoEvent;
    }

    // methods
    /**
     * activitiesUser returns all the activities where the user is engaged
     * @param idUser String
     * @param la  List<Activity>
     * @return ArrayList<Activity>
     */
    public static ArrayList<Activity> activitiesUser(String idUser, List<Activity> la) {
        ArrayList<Activity> result = new ArrayList<>();
        for (Activity a: la) {
            if (a.getPersonInCharge().contains(idUser)) {
                result.add(a);
            }
        }
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        // parcel.writeString(nameActivity);
        parcel.writeString(address);
        parcel.writeString(description);
        parcel.writeStringList(personInCharge);
        parcel.writeString(photoEvent);
    }

    public static final Creator<Activity> CREATOR = new Creator<Activity>() {
        @Override
        public Activity createFromParcel(Parcel parcel) {
            return new Activity(parcel);
        }

        @Override
        public Activity[] newArray(int i) {
            return new Activity[0];
        }
    };
}