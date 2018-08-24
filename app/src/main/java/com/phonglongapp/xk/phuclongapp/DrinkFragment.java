package com.phonglongapp.xk.phuclongapp;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.phonglongapp.xk.phuclongapp.Adapter.DrinkAdapter;
import com.phonglongapp.xk.phuclongapp.Model.Rating;
import com.phonglongapp.xk.phuclongapp.Utils.Common;
import com.phonglongapp.xk.phuclongapp.Model.Drink;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import java.util.ArrayList;
import java.util.List;

public class DrinkFragment extends Fragment{

    RecyclerView list_drink;

    //FirebaseDatabase
    FirebaseDatabase database;
    DatabaseReference drinks;
    //Drink
    List<Drink> drinkList;
    CollapsingToolbarLayout collapsingToolbarLayout;

    //Adapter
    DrinkAdapter adapter;

    //Extra
    String id_cate,name_cate;

    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getParentFragment();
        return inflater.inflate(R.layout.activity_drink, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Common.BackPressA = 1;
        Common.checkDrinkFragmentOpen = true;
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        drinkList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        drinks = database.getReference("Drink");
        drinks.keepSynced(true);

        //Ánh xạ
        toolbar = view.findViewById(R.id.tool_bar_drink_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        list_drink = view.findViewById(R.id.listDrink);
        list_drink.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        list_drink.setHasFixedSize(true);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);


        //Get intent
        //if(getIntent() != null){
            //id_cate = getIntent().getStringExtra("CategoryId");
            //name_cate = getIntent().getStringExtra("CategoryName");
        //}

        if(getArguments() != null){
            id_cate = getArguments().getString("CategoryId");
            name_cate = getArguments().getString("CategoryName");
        }
        if(!id_cate.isEmpty() && id_cate != null){
            collapsingToolbarLayout.setTitle(name_cate);
        }

        //Adapter
        adapter = new DrinkAdapter(getActivity(),drinkList, getActivity());
        list_drink.setAdapter(adapter);
        getDrink(id_cate);

        super.onViewCreated(view, savedInstanceState);
    }


    public void getDrink(final String id) {
        //Toast.makeText(DrinkActivity.this,""+ id,Toast.LENGTH_SHORT).show();
       drinks.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               Drink drink = dataSnapshot.getValue(Drink.class);
               if(drink.getMenuId().equals(id)) {
                   drink.setId(dataSnapshot.getKey());
                   drinkList.add(drink);
                   adapter.notifyDataSetChanged();
               }

           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               Drink drink = dataSnapshot.getValue(Drink.class);
               drink.setId(dataSnapshot.getKey());
               for(int i = 0; i < drinkList.size();i++){
                   if(drinkList.get(i).getId().equals(drink.getId())){
                       drinkList.remove(i);
                       drinkList.add(i,drink);
                       adapter.notifyDataSetChanged();
                       break;
                   }
               }
           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
               Drink drink = dataSnapshot.getValue(Drink.class);
               drink.setId(dataSnapshot.getKey());
               for(int i = 0; i < drinkList.size();i++){
                   if(drinkList.get(i).getId().equals(drink.getId())){
                       drinkList.remove(i);
                       adapter.notifyDataSetChanged();
                       break;
                   }
               }
           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
    }

}
