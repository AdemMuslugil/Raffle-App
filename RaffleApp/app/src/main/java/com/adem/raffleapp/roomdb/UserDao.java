package com.adem.raffleapp.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.adem.raffleapp.model.User;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    Flowable<List<User>> getAll();

    @Insert
    Completable insert(User user);

    @Delete
    Completable delete(User user);

    @Query("DELETE FROM user")
    Completable deleteAll();






}