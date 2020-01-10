package com.example.myapplication;

import androidx.room.ColumnInfo;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.RoomDatabase;

import java.io.Serializable;
@Database(entities = {Users.class}, version = 1)

public abstract class AppDatabase extends RoomDatabase
{

    public abstract UserDao userDao();

}
