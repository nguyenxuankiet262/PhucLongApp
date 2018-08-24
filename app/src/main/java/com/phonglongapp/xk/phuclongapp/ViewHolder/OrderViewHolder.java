package com.phonglongapp.xk.phuclongapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.phonglongapp.xk.phuclongapp.Interface.ItemClickListener;
import com.phonglongapp.xk.phuclongapp.Interface.ItemLongClickListener;
import com.phonglongapp.xk.phuclongapp.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    public TextView id_order, time_order, status_order, comment_order;
    public RecyclerView recyclerView;
    public ImageView dropdown_button;
    ItemClickListener itemClickListener;
    ItemLongClickListener itemLongClickListener;
    public int check_dropdown = 0;
    public OrderViewHolder(View itemView) {
        super(itemView);
        comment_order = itemView.findViewById(R.id.comment_order_history);
        status_order = itemView.findViewById(R.id.status_order_history);
        id_order = itemView.findViewById(R.id.id_order_history);
        time_order = itemView.findViewById(R.id.time_order_history);
        recyclerView = itemView.findViewById(R.id.list_order_history);
        dropdown_button = itemView.findViewById(R.id.dropdown_button);

    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition());
    }

    @Override
    public boolean onLongClick(View v) {
        return itemLongClickListener.onLongClick(v,getAdapterPosition());
    }
}
