package com.phonglongapp.xk.phuclongapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.phonglongapp.xk.phuclongapp.Model.Rating;
import com.phonglongapp.xk.phuclongapp.Model.User;
import com.phonglongapp.xk.phuclongapp.R;
import com.phonglongapp.xk.phuclongapp.ViewHolder.CommentViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

    Context context;
    List<Rating> ratingList;
    List<User> userList;

    public CommentAdapter(Context context, List<Rating> ratingList, List<User> userList) {
        this.context = context;
        this.ratingList = ratingList;
        this.userList = userList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item_layout,parent,false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        if(!userList.get(position).getImage().equals("empty")){
            Picasso.with(context).load(userList.get(position).getImage()).into(holder.image_cmt);
        }
        holder.name_cmt.setText(userList.get(position).getName());
        holder.date_comment.setText("-"+ratingList.get(position).getDate()+"-");
        holder.comment.setText(ratingList.get(position).getComment());
        int count = 0, sum = 0;
        holder.ratingBar.setRating(Float.parseFloat(ratingList.get(position).getRate()));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
