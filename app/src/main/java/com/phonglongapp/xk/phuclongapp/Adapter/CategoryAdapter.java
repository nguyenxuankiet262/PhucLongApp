package com.phonglongapp.xk.phuclongapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.phonglongapp.xk.phuclongapp.DrinkFragment;
import com.phonglongapp.xk.phuclongapp.Interface.ItemClickListener;
import com.phonglongapp.xk.phuclongapp.MainActivity;
import com.phonglongapp.xk.phuclongapp.MainFragment;
import com.phonglongapp.xk.phuclongapp.Model.Category;
import com.phonglongapp.xk.phuclongapp.R;
import com.phonglongapp.xk.phuclongapp.Utils.Common;
import com.phonglongapp.xk.phuclongapp.ViewHolder.CategoryViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    Context context;
    List<Category> categories;
    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.category_item_layout,parent,false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, final int position) {
        Picasso picasso = Picasso.with(context);
        picasso.setIndicatorsEnabled(false);
        picasso.load(categories.get(position).getImage()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.img_product, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso picasso = Picasso.with(context);
                picasso.setIndicatorsEnabled(false);
                picasso.load(categories.get(position).getImage()).into(holder.img_product);
            }
        });
        holder.name_product.setText(categories.get(position).getName());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                //Toast.makeText(context,""+holder.name_product.getText().toString(),Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(context, DrinkFragment.class);
                String index = categories.get(position).getId();
                //intent.putExtra("CategoryId", index);
                //intent.putExtra("CategoryName",categories.get(position).getName());
                //context.startActivity(intent);
                Bundle bundle = new Bundle();
                bundle.putString("CategoryId",index);
                bundle.putString("CategoryName",categories.get(position).getName());
                DrinkFragment drinkActivity  = new DrinkFragment();
                drinkActivity.setArguments(bundle);
                FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragment,drinkActivity,"DrinkFragment").addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
