package dev.nelson.mot.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by Nelson on 2/1/17.
 */

public class MyApplication extends Application{
    private static MyApplication instance;

    public MyApplication(){
        instance = this;
    }

    public static Context getContext(){
        return instance.getApplicationContext();
    }
}
