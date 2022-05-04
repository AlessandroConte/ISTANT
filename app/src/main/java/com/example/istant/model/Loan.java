package com.example.istant.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * entity 'loan' of the database
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

    // constructors

    public Loan () {}

    public Loan(String id, Timestamp dateStart, Timestamp dateEnd, String photoLoan,
                String description, String nameLoan, int isTaken, String takenUser, String uid) {
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
        // this.nameLoan = in.readString();
        this.uid = in.readString();
        this.isTaken = in.readInt();
        this.takenUser = in.readString();
    }

    // getter and setter
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

    public void setNameLoan(String nameLoan) {
        this.nameLoan = nameLoan;
    }

    public String getUid() {
        return uid;
    }

    public String getPhotoLoan() {
        return photoLoan;
    }

    public void setPhotoLoan(String photoLoan) {
        this.photoLoan = photoLoan;
    }

    public int getIsTaken() {
        return isTaken;
    }

    public void setIsTaken(int isTaken) {
        this.isTaken = isTaken;
    }

    public String getTakenUser() {
        return takenUser;
    }

    public void setTakenUser(String takenUser) {
        this.takenUser = takenUser;
    }

    // methods
    /**
     * loansUser returns all the loans that the user has created
     * @param idUser String
     * @param ll List<Loan>
     * @return ArrayList<Loan>
     */
    public static ArrayList<Loan> loansUser(String idUser, List<Loan> ll) {
        ArrayList<Loan> result = new ArrayList<>();
        for (Loan l: ll) {
            if (l.getUid().equals(idUser)) {
                result.add(l);
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
        parcel.writeString(photoLoan);
        parcel.writeString(description);
        // parcel.writeString(nameLoan);
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