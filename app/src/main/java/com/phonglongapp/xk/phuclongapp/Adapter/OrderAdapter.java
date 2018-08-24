package com.phonglongapp.xk.phuclongapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phonglongapp.xk.phuclongapp.Database.ModelDB.Cart;
import com.phonglongapp.xk.phuclongapp.Model.Order;
import com.phonglongapp.xk.phuclongapp.R;
import com.phonglongapp.xk.phuclongapp.Utils.Common;
import com.phonglongapp.xk.phuclongapp.ViewHolder.OrderViewHolder;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {

    Context context;
    List<Order> orderList;
    HistoryAdapter adapter;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_history_item,parent,false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder holder, int position) {
        holder.recyclerView.setVisibility(View.GONE);
        if(!orderList.get(position).getNote().isEmpty()){
            holder.comment_order.setText(orderList.get(position).getNote());
        }
        if(orderList.get(position).getStatus().equals("0")) {
            holder.status_order.setText("Đã nhận order");
            holder.status_order.setTextColor(ContextCompat.getColor(context,R.color.colorOpenStore));
        }
        if(orderList.get(position).getStatus().equals("1")){
            holder.status_order.setText("Đang trên đường");
            holder.status_order.setTextColor(ContextCompat.getColor(context,R.color.colorOTW));
        }
        if(orderList.get(position).getStatus().equals("2")){
            holder.status_order.setText("Thành công");
            holder.status_order.setTextColor(ContextCompat.getColor(context,R.color.colorSc));
        }
        if(orderList.get(position).getStatus().equals("3")){
            holder.status_order.setText("Đã hủy");
            holder.status_order.setTextColor(ContextCompat.getColor(context,R.color.colorCancel));
        }
        holder.id_order.setText("#" + orderList.get(position).getId());
        holder.time_order.setText(Common.getTimeAgo(Long.parseLong(orderList.get(position).getId()),context));
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.recyclerView.setHasFixedSize(true);
        adapter = new HistoryAdapter(context,orderList.get(position).getCartList());
        holder.recyclerView.setAdapter(adapter);
        holder.dropdown_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.check_dropdown == 0) {
                    holder.dropdown_button.setImageResource(R.drawable.ic_arrow_drop_up_black_50dp);
                    holder.recyclerView.setVisibility(View.VISIBLE);
                    holder.check_dropdown = 1;
                }
                else{
                    holder.dropdown_button.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                    holder.recyclerView.setVisibility(View.GONE);
                    holder.check_dropdown = 0;
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }


}
