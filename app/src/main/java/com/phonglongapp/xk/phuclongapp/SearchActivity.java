package com.phonglongapp.xk.phuclongapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.phonglongapp.xk.phuclongapp.Adapter.DrinkAdapter;
import com.phonglongapp.xk.phuclongapp.Model.Drink;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements RatingDialogListener {

    MaterialSearchBar searchBar;
    RecyclerView searchRecyclerList;

    //FirebaseDatabase
    FirebaseDatabase database;
    DatabaseReference searchItem;

    List<String> suggest_list;
    List<Drink> seach_drink_list;

    int count = 5;

    //Adapter
    DrinkAdapter searchAdapter, adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        seach_drink_list = new ArrayList<>();
        suggest_list = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        searchItem = database.getReference("Drink");

        searchRecyclerList = findViewById(R.id.search_list);
        searchRecyclerList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        searchRecyclerList.hasFixedSize();

        searchBar = (MaterialSearchBar) findViewById(R.id.search_bar);
        searchBar.setHint("Search some drinks...");

        adapter = new DrinkAdapter(this,seach_drink_list,SearchActivity.this);

        searchRecyclerList.setAdapter(adapter);



        loadDrinks();

        //Setting searchBar
        searchBar.setMaxSuggestionCount(5);
        searchBar.setLastSuggestions(suggest_list);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest = new ArrayList<String>();
                for(String search:suggest_list){
                    if(search.toLowerCase().contains(searchBar.getText().toLowerCase())){
                        suggest.add(search);
                    }
                }
                searchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled)
                    searchRecyclerList.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                    startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {
        List<Drink> result = new ArrayList<Drink>();
        for(Drink drink:seach_drink_list){
            if(drink.getName().contains(text)){
                result.add(drink);
            }
        }
        searchAdapter = new DrinkAdapter(this, result,SearchActivity.this);
        searchRecyclerList.setAdapter(searchAdapter);
    }

    private void loadDrinks() {
        searchItem.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Drink drink = dataSnapshot.getValue(Drink.class);
                drink.setId(dataSnapshot.getKey());
                seach_drink_list.add(drink);
                suggest_list.add(drink.getName());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onPositiveButtonClicked(int i, String s) {
        Toast.makeText(this,"OK",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNegativeButtonClicked() {
        Toast.makeText(this,"No man", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNeutralButtonClicked() {

    }
}
