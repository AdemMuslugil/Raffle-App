package com.adem.raffleapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Database;
import androidx.room.RawQuery;
import androidx.room.Room;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.adem.raffleapp.R;
import com.adem.raffleapp.adapter.UserAdapter;
import com.adem.raffleapp.databinding.ActivityListBinding;
import com.adem.raffleapp.model.User;
import com.adem.raffleapp.roomdb.UserDao;
import com.adem.raffleapp.roomdb.UserDatabase;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ListActivity extends AppCompatActivity {

    private ActivityListBinding binding;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    UserDatabase db;
    UserDao userDao;
    SQLiteDatabase database;
    String winName, winInsta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        db = Room.databaseBuilder(getApplicationContext(), UserDatabase.class, "Users").build();
        userDao = db.userDao();

        database = openOrCreateDatabase("Users", MODE_PRIVATE, null);



        //to list data from database
        compositeDisposable.add(userDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ListActivity.this::handleResponse)
        );

    }


    //Random user is the winner
    public void raffle(View view) {
        try {

            Cursor cursor = database.rawQuery("SELECT name,insta FROM user ORDER BY random() LIMIT 1", null);
            int nameIx = cursor.getColumnIndex("name");
            int instaIx = cursor.getColumnIndex("insta");

            while (cursor.moveToNext()) {
                winName = cursor.getString(nameIx);
                winInsta = cursor.getString(instaIx);
            }
            cursor.close();

            Intent intent = new Intent(ListActivity.this, MainActivity.class);
            intent.putExtra("winName", winName);
            intent.putExtra("winInsta", winInsta);
            startActivity(intent);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void handleResponse(List<User> userList) {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        UserAdapter userAdapter = new UserAdapter(userList);
        binding.recyclerView.setAdapter(userAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.clear_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override //menu click
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.clear_list) {

            compositeDisposable.add(userDao.deleteAll()
                    .subscribeOn(Schedulers.io())
                    .subscribe()
            );
        }
        return super.onOptionsItemSelected(item);
    }
}
