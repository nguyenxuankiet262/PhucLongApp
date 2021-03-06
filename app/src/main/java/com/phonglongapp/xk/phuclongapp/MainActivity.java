package com.phonglongapp.xk.phuclongapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.facebook.FacebookSdk;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.phonglongapp.xk.phuclongapp.Model.Rating;
import com.phonglongapp.xk.phuclongapp.Model.Token;
import com.phonglongapp.xk.phuclongapp.Utils.CustomViewPager;
import com.phonglongapp.xk.phuclongapp.Adapter.ViewPagerAdapter;
import com.phonglongapp.xk.phuclongapp.Utils.Common;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements RatingDialogListener {

    private Fragment mainFragment;
    private Fragment favoriteFragment;
    private Fragment locationFragment;
    FirebaseDatabase database;
    DatabaseReference rating;
    CustomViewPager viewPager;
    ViewPagerAdapter adapter;
    AHBottomNavigation bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        FacebookSdk.sdkInitialize(getApplicationContext());

        //bottomNavigationView = findViewById(R.id.NavBot);
        database = FirebaseDatabase.getInstance();
        rating = database.getReference("Rating");
        //Intent service = new Intent(MainActivity.this, ListenOrder.class);
        //startService(service);
        mainFragment = new MainFragment();
        favoriteFragment = new FavoriteFragment();
        locationFragment = new LocationFragment();

        viewPager = (CustomViewPager) findViewById(R.id.fragment_content);
        viewPager.setOffscreenPageLimit(3);
        adapter = new ViewPagerAdapter (MainActivity.this.getSupportFragmentManager());
        adapter.addFragment(mainFragment);
        adapter.addFragment(favoriteFragment);
        adapter.addFragment(locationFragment);
        viewPager.setAdapter(adapter);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                updateToken(newToken);
            }
        });


//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()){
//                    case R.id.home_btn_nav_bar:
//                        //setFragment(mainFragment);
//                        viewPager.setCurrentItem(0);
//                        return true;
//                    case R.id.favor_btn_nav_bar:
//                        //setFragment(favoriteFragment);
//                        viewPager.setCurrentItem(1);
//                        return true;
//                    case R.id.location_btn_nav_bar:
//                        viewPager.setCurrentItem(2);
//                        return true;
//                        default:
//                            return false;
//                }
//            }
//        });
        bottomNavigation = findViewById(R.id.NavBot);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.ic_home_black_24dp, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Favorite", R.drawable.ic_favorite_black_24dp, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Location", R.drawable.ic_location_on_black_24dp, R.color.colorPrimary);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);

        bottomNavigation.setAccentColor(Color.parseColor("#FFFFFF"));
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#176939"));
        bottomNavigation.setInactiveColor(Color.parseColor("#b2b2b2"));

        bottomNavigation.setTranslucentNavigationEnabled(true);;

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position){
                    case 0:
                        viewPager.setCurrentItem(0,false);
                        Common.checkPosision = 1;
                        return true;
                    case 1:
                        viewPager.setCurrentItem(1,false);
                        return true;
                    case 2:
                        viewPager.setCurrentItem(2,false);
                        Common.checkPosision = 2;
                        return true;
                        default:
                            return false;
                }
            }
        });
    }

    private void updateToken(String t) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference token = db.getReference("Token");
        Token temp = new Token(t,false);
        token.child(Common.CurrentUser.getId()).setValue(temp);
    }

    @Override
    public void onBackPressed() {
        if (Common.checkPosision == 1 && Common.BackPressA > 0) {
            super.onBackPressed();
            Common.BackPressA--;
            Common.checkDrinkFragmentOpen = false;
        }
        else if(Common.checkPosision == 2 && Common.BackPressB >0) {
            super.onBackPressed();
            Common.BackPressB--;
            Common.checkDrinkFragmentOpen = false;
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Bạn có muốn thoát chương trình không?");
            builder.setCancelable(false);
            builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setNegativeButton("Chấp nhận", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }


    @Override
    public void onPositiveButtonClicked(int i, String s) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Submitting...");
        progressDialog.setMessage("Please wait for a minute!");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        Rating rate = new Rating();
        rate.setDrinkId(Common.idDrink);
        rate.setRate(String.valueOf(i));
        rate.setComment(s);
        Date date = new Date();
        rate.setDate(DateFormat.getDateInstance(DateFormat.MEDIUM).format(date));
        rate.setStatus("0");
        rating.child(Common.CurrentUser.getId()).setValue(rate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,"Cảm ơn bạn đã đánh giá!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onNegativeButtonClicked() {
        Toast.makeText(this,"No man", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNeutralButtonClicked() {

    }
}
