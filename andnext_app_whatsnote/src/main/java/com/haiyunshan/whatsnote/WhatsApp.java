package com.haiyunshan.whatsnote;

import android.app.Application;
import club.andnext.navigation.NavigationHelper;

public class WhatsApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        NavigationHelper.onCreate(this);
    }

}
