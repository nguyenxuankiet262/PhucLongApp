package com.phonglongapp.xk.phuclongapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phonglongapp.xk.phuclongapp.Interface.ItemClickListener;
import com.phonglongapp.xk.phuclongapp.MapFragment;
import com.phonglongapp.xk.phuclongapp.Model.Store;
import com.phonglongapp.xk.phuclongapp.R;
import com.phonglongapp.xk.phuclongapp.ViewHolder.StoreViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreViewHolder> {
    Context context;
    List<Store> storeList;

    public StoreAdapter(Context context, List<Store> storeList){
        this.context = context;
        this.storeList = storeList;
    }
    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.location_item_layout,parent,false);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StoreViewHolder holder, final int position) {
        Picasso.with(context).load(storeList.get(position).getImage()).into(holder.store_image);
        holder.name_store.setText(storeList.get(position).getName());
        holder.address_store.setText(storeList.get(position).getAddress());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("StoreID", storeList.get(position).getId());
                MapFragment mapFragment  = new MapFragment();
                mapFragment.setArguments(bundle);
                FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.store_layout,mapFragment).addToBackStack(null).commit();
                //Toast.makeText(context, "ID = " + storeList.get(position).getId() + " " + Common.coordinatesStringMap.get(storeList.get(position).getId()).getAddress() +" và tọa độ là "
                        //+ Common.coordinatesStringMap.get(storeList.get(position).getId()).getLat()+"",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }
}
