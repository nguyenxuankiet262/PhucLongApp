package com.phonglongapp.xk.phuclongapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import info.hoang8f.widget.FButton;

public class LoginActivity extends AppCompatActivity {

    private FButton loginBtn,regisBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        loginBtn = findViewById(R.id.login_Btn);
        regisBtn = findViewById(R.id.regis_Btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(LoginActivity.this,LoginMainActivity.class);
                startActivity(loginIntent);
            }
        });
        regisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(loginIntent);
            }
        });
    }
}
