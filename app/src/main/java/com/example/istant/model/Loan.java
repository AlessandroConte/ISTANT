package com.example.istant.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *  Entity 'loan' of the database
 */
public class Loan implements Parcelable {
    private String id;
    private Timestamp dateStart;
    private Timestamp dateEnd;
    private String photoLoan;
    private String description;
    private String nameLoan;
    private int isTaken;
    private String takenUser;
    private String uid;

    // CONSTRUCTORS
    public Loan () {}

    public Loan (String id, Timestamp dateStart, Timestamp dateEnd, String photoLoan, String description, String nameLoan, int isTaken, String takenUser, String uid) {
        this.id = id;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.photoLoan = photoLoan;
        this.description = description;
        this.nameLoan = nameLoan;
        this.isTaken = isTaken;
        this.takenUser = takenUser;
        this.uid = uid;
    }

    protected Loan(Parcel in){
        this.id = in.readString();
        this.photoLoan = in.readString();
        this.description = in.readString();
        this.nameLoan = in.readString();
        this.uid = in.readString();
        this.isTaken = in.readInt();
        this.takenUser = in.readString();
    }

    // GETTERS AND SETTERS
    public String getId() {
        return id;
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

    public String getNameLoan() {
        return nameLoan;
    }

    public String getUid() {
        return uid;
    }

    public String getPhotoLoan() {
        return photoLoan;
    }

    public int getIsTaken() {
        return isTaken;
    }

    public String getTakenUser() {
        return takenUser;
    }


    // methods used to implement the Parcelable interface
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(photoLoan);
        parcel.writeString(description);
        parcel.writeString(nameLoan);
        parcel.writeString(uid);
        parcel.writeInt(isTaken);
        parcel.writeString(takenUser);
    }

    public static final Creator<Loan> CREATOR = new Parcelable.Creator<Loan>() {

        @Override
        public Loan createFromParcel(Parcel parcel) {
            return new Loan(parcel);
        }

        @Override
        public Loan[] newArray(int i) {
            return new Loan[0];
        }
    };
}