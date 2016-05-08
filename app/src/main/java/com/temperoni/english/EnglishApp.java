package com.temperoni.english;

import android.app.Application;

import com.temperoni.english.database.DataBase;

/**
 * Created by leandro.temperoni on 4/28/2016.
 */
public class EnglishApp extends Application {

    public static EnglishApp getInstance() {
        return sInstance;
    }

    private static EnglishApp sInstance;

    private static DataBase mDataBase;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mDataBase = new DataBase(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // Close the realm instance.
        mDataBase.close();
    }

    public static DataBase getDataBase() {
        return mDataBase;
    }
}
