package com.playlistx.viewmodel;

public class HomeModel {
    private static HomeModel instance;

    private HomeModel() {
    }

    public static HomeModel get() {
        if (instance == null) return instance = new HomeModel();
        else return instance;
    }


}
