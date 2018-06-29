package com.phonglongapp.xk.phuclongapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phonglongapp.xk.phuclongapp.Database.ModelDB.Cart;
import com.phonglongapp.xk.phuclongapp.Model.Order;
import com.phonglongapp.xk.phuclongapp.R;
import com.phonglongapp.xk.phuclongapp.Utils.Common;
import com.phonglongapp.xk.phuclongapp.ViewHolder.HistoryViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

    Context context;
    List<Cart> cartList;

    public HistoryAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_item_layout,parent,false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        if(cartList.get(position).cStatus.equals("cold")) {
            Picasso.with(context).load(cartList.get(position).cImageCold).into(holder.imageView_history);
        }
        else
        {
            Picasso.with(context).load(cartList.get(position).cImageHot).into(holder.imageView_history);
        }
        holder.money_drink.setText(Common.ConvertIntToMoney(cartList.get(position).cPrice+""));
        holder.name_drink_history.setText(cartList.get(position).cName);
        holder.quanity_drink.setText(cartList.get(position).cQuanity+"");
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
}
