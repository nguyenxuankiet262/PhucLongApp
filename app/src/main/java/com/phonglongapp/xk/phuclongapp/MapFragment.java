package com.phonglongapp.xk.phuclongapp;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.phonglongapp.xk.phuclongapp.Utils.Common;
import com.phonglongapp.xk.phuclongapp.Model.Coordinates;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class MapFragment extends Fragment implements OnMapReadyCallback{

    private static View view;
    FirebaseDatabase database;
    DatabaseReference mapdatabase;
    private MapView mapView;
    private GoogleMap mMap;
    String id;
    private static final int ZoomValue = 15;

    private static final String name = "Phúc Long Coffee & Tea Express";

    private static final LatLng CN1 = new LatLng(Common.coordinatesStringMap.get("01").getLat()
                                                    ,Common.coordinatesStringMap.get("01").getLng());
    private static final LatLng CN2 = new LatLng(Common.coordinatesStringMap.get("02").getLat()
                                                    ,Common.coordinatesStringMap.get("02").getLng());
    private static final LatLng CN3 = new LatLng(Common.coordinatesStringMap.get("03").getLat()
                                                    ,Common.coordinatesStringMap.get("03").getLng());
    private static final LatLng CN4 = new LatLng(Common.coordinatesStringMap.get("04").getLat()
                                                    ,Common.coordinatesStringMap.get("04").getLng());
    private static final LatLng CN5 = new LatLng(Common.coordinatesStringMap.get("05").getLat()
                                                    ,Common.coordinatesStringMap.get("05").getLng());

    private Marker mCN1;
    private Marker mCN2;
    private Marker mCN3;
    private Marker mCN4;
    private Marker mCN5;

    List<Coordinates> coordinatesList;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Common.BackPress = 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmentof
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_map, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if(getArguments() != null){
            id = getArguments().getString("StoreID");
        }
        mapView = (MapView) view.findViewById(R.id.map_store);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        coordinatesList = new ArrayList<Coordinates>();
        database = FirebaseDatabase.getInstance();
        mapdatabase = database.getReference("Location");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mCN1 = mMap.addMarker(new MarkerOptions()
                .position(CN1)
                .title(name)
                .snippet(Common.coordinatesStringMap.get("01").getAddress()));
        mCN1.setTag(0);

        mCN2 = mMap.addMarker(new MarkerOptions()
                .position(CN2)
                .title(name)
                .snippet(Common.coordinatesStringMap.get("02").getAddress()));
        mCN2.setTag(0);

        mCN3 = mMap.addMarker(new MarkerOptions()
                .position(CN3)
                .title(name)
                .snippet(Common.coordinatesStringMap.get("03").getAddress()));
        mCN3.setTag(0);

        mCN4 = mMap.addMarker(new MarkerOptions()
                .position(CN4)
                .title(name)
                .snippet(Common.coordinatesStringMap.get("04").getAddress()));
        mCN4.setTag(0);
        mCN5 = mMap.addMarker(new MarkerOptions()
                .position(CN5)
                .title(name)
                .snippet(Common.coordinatesStringMap.get("05").getAddress()));
        mCN5.setTag(0);

        //Zoom location selected store
        if(id.equals("01")){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CN1,ZoomValue));
        }
        if(id.equals("02")){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CN2,ZoomValue));
        }
        if(id.equals("03")){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CN3,ZoomValue));
        }
        if(id.equals("04")){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CN3,ZoomValue));
        }
        if(id.equals("05")){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CN4,ZoomValue));
        }
    }

}
