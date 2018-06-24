package com.phonglongapp.xk.phuclongapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.phonglongapp.xk.phuclongapp.Adapter.StoreAdapter;
import com.phonglongapp.xk.phuclongapp.Utils.Common;
import com.phonglongapp.xk.phuclongapp.Model.Coordinates;
import com.phonglongapp.xk.phuclongapp.Model.Store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment {

    List<Store> list_store;
    RecyclerView recyclerView;
    StoreAdapter adapter;

    //FirebaseDatabase
    FirebaseDatabase database;
    DatabaseReference store;

    public LocationFragment(){

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance();
        store = database.getReference("Location");
        recyclerView = view.findViewById(R.id.location_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setHasFixedSize(true);;

        list_store = new ArrayList<Store>();
        adapter = new StoreAdapter(getActivity(),list_store);

        //Load into common map
        Common.coordinatesStringMap = new HashMap<String,Coordinates>();
        loadStoreList();
        recyclerView.setAdapter(adapter);
        super.onViewCreated(view, savedInstanceState);
    }

    private void loadStoreList() {
        store.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Store st = dataSnapshot.getValue(Store.class);
                st.setId(dataSnapshot.getKey());
                list_store.add(st);
                adapter.notifyDataSetChanged();
                Common.coordinatesStringMap.put(st.getId(),new Coordinates(Common.ConvertStringToDouble(st.getLat()),Common.ConvertStringToDouble(st.getLng()),st.getAddress()));
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
}
