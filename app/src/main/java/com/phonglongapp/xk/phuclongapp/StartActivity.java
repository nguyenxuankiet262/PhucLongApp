package com.phonglongapp.xk.phuclongapp;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.phonglongapp.xk.phuclongapp.Model.User;
import com.phonglongapp.xk.phuclongapp.Utils.Common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

import static android.os.SystemClock.sleep;

public class StartActivity extends AppCompatActivity {

    private ImageView logoView;
    private TextView contText;
    private Animation anim_alpha,anim_blink;
    private FirebaseAuth auth;
    //FirebaseDatabase
    FirebaseDatabase database;
    DatabaseReference user;
    FirebaseUser currentUser;
    String uid;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        auth = FirebaseAuth.getInstance();
        logoView = (ImageView) findViewById(R.id.logo_Image);
        contText = (TextView)findViewById(R.id.continueText);
        contText.setVisibility(View.INVISIBLE);
        anim_blink = new AlphaAnimation(0.0f,1.0f);
        anim_blink.setDuration(20);
        anim_blink.setStartOffset(20);
        anim_blink.setRepeatMode(Animation.REVERSE);
        anim_blink.setRepeatCount(Animation.INFINITE);
        anim_alpha = AnimationUtils.loadAnimation(this,R.anim.anim_alpha);
        anim_alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                contText.setVisibility(View.VISIBLE);
                contText.startAnimation(anim_blink);
                new Timer().schedule(new TimerTask(){
                    public void run() {
                        StartActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                if (auth.getCurrentUser() != null) {
                                    database = FirebaseDatabase.getInstance();
                                    currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                    uid = currentUser.getUid();
                                    user = database.getReference("User").child(uid);
                                    user.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                            User user = dataSnapshot.getValue(User.class);
                                            user.setId(dataSnapshot.getKey());
                                            Common.CurrentUser = user;
                                            Intent intent = new Intent(StartActivity.this, MainActivity.class);;
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                        }

                                        @Override
                                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                                            FirebaseAuth.getInstance().signOut();
                                            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    // User is signed in (getCurrentUser() will be null if not signed in)
                                }
                                else {
                                    Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }
                }, 3000);
            }


            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        logoView.startAnimation(anim_alpha);
    }
}
