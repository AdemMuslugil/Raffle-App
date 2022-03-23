package com.adem.raffleapp.roomdb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.adem.raffleapp.model.User;

@Database(entities = {User.class},version = 1)
public abstract class UserDatabase extends RoomDatabase{

    public abstract UserDao userDao();

}
