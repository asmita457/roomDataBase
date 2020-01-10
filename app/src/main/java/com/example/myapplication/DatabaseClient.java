package com.example.myapplication;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient
{
    private Context context;

    private static DatabaseClient myInstance;

    private AppDatabase appDatabase;

    public DatabaseClient(Context context)
    {
        this.context = context;

        appDatabase= Room.databaseBuilder(context,AppDatabase.class,"myDb").allowMainThreadQueries().build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (myInstance == null) {
            myInstance = new DatabaseClient(mCtx);
        }
        return myInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
