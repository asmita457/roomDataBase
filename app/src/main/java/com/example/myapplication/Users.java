package com.example.myapplication;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Users
{
    @PrimaryKey(autoGenerate =true)
    private int id;

    @ColumnInfo(name ="url")
    private String url;

    @ColumnInfo(name ="name")
    private String name;

    @ColumnInfo(name ="date")
    private String date;

    @ColumnInfo(name ="gender")
    private String gender;

    @ColumnInfo(name ="profession")
    private String profession;

    @ColumnInfo(name ="email")
    private String email;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
