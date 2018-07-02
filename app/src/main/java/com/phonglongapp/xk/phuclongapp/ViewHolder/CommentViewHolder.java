package com.phonglongapp.xk.phuclongapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.phonglongapp.xk.phuclongapp.R;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    public ImageView image_cmt;
    public TextView name_cmt,comment,date_comment;
    public RatingBar ratingBar;
    public CommentViewHolder(View itemView) {
        super(itemView);
        image_cmt = itemView.findViewById(R.id.image_comment);
        name_cmt = itemView.findViewById(R.id.name_comment);
        comment = itemView.findViewById(R.id.comment_in_comment);
        date_comment = itemView.findViewById(R.id.date_comment);
        ratingBar = itemView.findViewById(R.id.rating_bar_comment);
    }
}
