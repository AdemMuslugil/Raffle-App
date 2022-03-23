package com.adem.raffleapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Insert;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.adem.raffleapp.databinding.ActivityMainBinding;
import com.adem.raffleapp.databinding.RecyclerRowBinding;
import com.adem.raffleapp.model.User;
import com.adem.raffleapp.roomdb.UserDao;
import com.adem.raffleapp.roomdb.UserDatabase;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    UserDatabase db;
    UserDao userDao;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    String info;
    int userId;
    User selectedUser;
    String winName,winInsta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        control();

        db= Room.databaseBuilder(getApplicationContext(), UserDatabase.class,"Users").build();
        userDao=db.userDao();

    }



    public void save(View view) {
        try {
            if (!binding.nameText.getText().toString().matches("") && !binding.socialText.getText().toString().matches("")) {
                User user = new User(binding.nameText.getText().toString(), binding.socialText.getText().toString());

                //Save user to database
                compositeDisposable.add(userDao.insert(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(MainActivity.this::handleResponse)
                );
            } else {
                Toast.makeText(this, "Enter Information", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void list(View view){
        handleResponse();
    }



    public void delete(View view){
        try {

            compositeDisposable.add(userDao.delete(selectedUser)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(MainActivity.this::handleResponse)
            );

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void handleResponse(){
        Intent intent=new Intent(MainActivity.this,ListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }



    public void control(){
        Intent intent=getIntent();
        info= intent.getStringExtra("info");
        userId=intent.getIntExtra("userId",0);
        winName=intent.getStringExtra("winName");
        winInsta=intent.getStringExtra("winInsta");


        if(info!=null) {
            binding.saveButton.setVisibility(View.GONE);
            binding.deleteButton.setVisibility(View.VISIBLE);

            selectedUser=(User) intent.getSerializableExtra("userId");
            String selectedName=selectedUser.name;
            String selectedInsta=selectedUser.insta;

            binding.nameText.setText(selectedName);
            binding.socialText.setText(selectedInsta);

        } else {
            binding.saveButton.setVisibility(View.VISIBLE);
            binding.deleteButton.setVisibility(View.GONE);
            binding.profileButton.setVisibility(View.GONE);
        }

        if (winName!=null || winInsta!=null){
            binding.nameText.setText(winName);
            binding.socialText.setText(winInsta);

            binding.saveButton.setVisibility(View.GONE);
            binding.profileButton.setVisibility(View.VISIBLE);
            binding.textView.setVisibility(View.VISIBLE);

        }
    }

    //profile Button click
    public void profile(View view){
        goToURL();
    }

    void goToURL() {
        Uri uri = Uri.parse("https://"+binding.socialText.getText().toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}