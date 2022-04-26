package com.example.istant;

import android.app.Application;

public class JustLoggedIn extends Application {
    private Boolean justLogged = false;

    public Boolean getJustLogged() {
        return justLogged;
    }

    public void setJustLogged(Boolean justLogged) {
        this.justLogged = justLogged;
    }
}
