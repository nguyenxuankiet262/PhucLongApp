package com.phonglongapp.xk.phuclongapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.phonglongapp.xk.phuclongapp.Adapter.HistoryAdapter;
import com.phonglongapp.xk.phuclongapp.Adapter.OrderAdapter;
import com.phonglongapp.xk.phuclongapp.Database.ModelDB.Cart;
import com.phonglongapp.xk.phuclongapp.Model.Order;
import com.phonglongapp.xk.phuclongapp.Utils.Common;

import java.util.ArrayList;
import java.util.List;

public class ViewDrinkFragment extends Fragment {

    Toolbar toolbar;

    OrderAdapter adapter;

    RecyclerView recyclerView;

    List<Order> orderList;

    FirebaseDatabase database;
    DatabaseReference order;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Common.BackPress = 1;
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_drink, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        orderList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        order = database.getReference("Order");
        recyclerView = view.findViewById(R.id.list_history);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        toolbar = view.findViewById(R.id.tool_bar_view_drink);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        adapter = new OrderAdapter(getActivity(),orderList);
        loadHistory();
        recyclerView.setAdapter(adapter);
        super.onViewCreated(view, savedInstanceState);
    }

    private void loadHistory() {
        order.orderByChild("userID").equalTo(Common.CurrentUser.getId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Order temp = dataSnapshot.getValue(Order.class);
                temp.setId(dataSnapshot.getKey());
                orderList.add(temp);
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
}
