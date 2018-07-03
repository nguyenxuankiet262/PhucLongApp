package com.phonglongapp.xk.phuclongapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
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
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        if(orderList.get(position).getStatus().equals("Ordered")) {
            holder.status_order.setText("Đã nhận order");
        }
        holder.id_order.setText("#" + orderList.get(position).getId());
        holder.time_order.setText(Common.getTimeAgo(Long.parseLong(orderList.get(position).getId()),context));
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.recyclerView.setHasFixedSize(true);
        adapter = new HistoryAdapter(context,orderList.get(position).getCartList());
        holder.recyclerView.setAdapter(adapter);
    }


    @Override
    public int getItemCount() {
        return orderList.size();
    }


}
