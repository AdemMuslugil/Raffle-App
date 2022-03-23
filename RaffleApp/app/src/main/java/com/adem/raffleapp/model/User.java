package com.adem.raffleapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import io.reactivex.rxjava3.core.Completable;

@Entity
public class User implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="name")
    public String name;


    @ColumnInfo(name="insta")

    public String insta;


    public User(String name,String insta){
        this.name=name;
        this.insta=insta;
    }
}
