package dev.nelson.mot.utils;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

public class MyApplication extends Application{
    private static Context context;

    public static Context getContext(){
        return MyApplication.context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        Stetho.initializeWithDefaults(this);
    }
}
