package com.adem.raffleapp.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adem.raffleapp.databinding.RecyclerRowBinding;
import com.adem.raffleapp.model.User;
import com.adem.raffleapp.view.ListActivity;
import com.adem.raffleapp.view.MainActivity;

import java.util.List;
import java.util.Random;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder>{

    List<User> userList;

    public UserAdapter(List<User> userList){
        this.userList=userList;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerRowBinding recyclerRowBinding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new UserHolder(recyclerRowBinding);

    }



    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.recyclerRowBinding.recyclerViewTextView.setText(userList.get(position).name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.itemView.getContext(), MainActivity.class);
                intent.putExtra("info","old");
                intent.putExtra("userId",userList.get(position));

                holder.itemView.getContext().startActivity(intent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return userList.size();
    }



    public class UserHolder extends RecyclerView.ViewHolder{
        RecyclerRowBinding recyclerRowBinding;

        public UserHolder(@NonNull RecyclerRowBinding recyclerRowBinding) {

            super(recyclerRowBinding.getRoot());
            this.recyclerRowBinding=recyclerRowBinding;


        }
    }


}
