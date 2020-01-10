package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao

public interface UserDao
{


@Insert
    void insert(Users users);


@Query("select * from users")
    List<Users>gettUsers();

@Delete
    void delete(Users users);

@Update
    void  update(Users users);


}
