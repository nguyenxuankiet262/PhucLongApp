package com.phonglongapp.xk.phuclongapp;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.phonglongapp.xk.phuclongapp.Adapter.FavoriteAdapter;
import com.phonglongapp.xk.phuclongapp.Model.Drink;
import com.phonglongapp.xk.phuclongapp.Utils.Common;
import com.phonglongapp.xk.phuclongapp.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    private RecyclerView list_fav;
    FrameLayout favLayout;
    RelativeLayout emptyLayout;
    CoordinatorLayout existLayout;

    public FavoriteFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        emptyLayout = view.findViewById(R.id.empty_fav_layout);
        Common.parentFavLayout = view.findViewById(R.id.myCoordinatorLayout);
        list_fav = view.findViewById(R.id.list_favorite);
        list_fav.setLayoutManager(new GridLayoutManager(getActivity(),2));
        list_fav.setHasFixedSize(true);
        favLayout = view.findViewById(R.id.fav_layout);
        existLayout = view.findViewById(R.id.myCoordinatorLayout);
        loadFavItem();
        super.onViewCreated(view, savedInstanceState);
    }

    private void loadFavItem() {
        Common.favoriteRepository.getFavItems()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Favorite>>() {
                    @Override
                    public void accept(List<Favorite> favorites) throws Exception {
                        if(Common.favoriteRepository.countFavItem()== 0) {
                            emptyLayout.setVisibility(View.VISIBLE);
                        }
                        else {
                            existLayout.setVisibility(View.VISIBLE);
                            emptyLayout.setVisibility(View.GONE);
                            displayFav(favorites);
                        }
                    }
                });
    }

    private void displayFav(List<Favorite> favorites) {
        FavoriteAdapter adapter = new FavoriteAdapter(getActivity(),favorites,this);
        list_fav.setAdapter(adapter);
    }
}
