package com.example.istant;

import android.app.Application;

public class JustLoggedIn extends Application {
    private Boolean justLogged = false;

    public void setJustLogged(Boolean justLogged) {
        this.justLogged = justLogged;
    }
}
