package com.example.istant.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * entity 'user' of the database
 */
public class User implements Parcelable {
    private String id;
    private String address;
    private Timestamp dateBorn;
    private String email;
    private String fiscalCode;
    private int gender;
    private String photoUrl;
    private String name;
    private String surname;
    private String telephoneNumber;


    // CONSTRUCTORS
    protected User(Parcel in){
        this.id = in.readString();
        this.address = in.readString();
        this.email = in.readString();
        this.fiscalCode = in.readString();
        this.gender = in.readInt();
        this.photoUrl = in.readString();
        this.name = in.readString();
        this.surname = in.readString();
        this.telephoneNumber = in.readString();
    }

    public User(String id, String address, Timestamp dateBorn, String email,
                String fiscalCode, int gender, String photoUrl, String name,
                String surname, String telephoneNumber) {
        this.id = id;
        this.address = address;
        this.dateBorn = dateBorn;
        this.email = email;
        this.fiscalCode = fiscalCode;
        this.gender = gender;
        this.photoUrl = photoUrl;
        this.name = name;
        this.surname = surname;
        this.telephoneNumber = telephoneNumber;
    }


    // GETTERS AND SETTERS
    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getId() {return id;}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getDateBorn() {
        return dateBorn;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }


    // METHODS
    // override of toString method
    @NonNull
    @Override
    public String toString() {
        return this.name + " " + this.telephoneNumber;
    }

    // methods used to implement the Parcelable interface
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(address);
        parcel.writeString(email);
        parcel.writeString(fiscalCode);
        parcel.writeInt(gender);
        parcel.writeString(photoUrl);
        parcel.writeString(name);
        parcel.writeString(surname);
        parcel.writeString(telephoneNumber);
    }

    public static final Creator<User> CREATOR = new Parcelable.Creator<User>() {

        @Override
        public User createFromParcel(Parcel parcel) {
            return new User(parcel);
        }

        @Override
        public User[] newArray(int i) {
            return new User[0];
        }
    };
}
