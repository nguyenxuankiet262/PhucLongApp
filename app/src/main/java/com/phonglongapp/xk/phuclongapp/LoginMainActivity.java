package com.phonglongapp.xk.phuclongapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
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
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.leakcanary.LeakCanary;

import info.hoang8f.widget.FButton;


public class LoginMainActivity extends AppCompatActivity {

    private MaterialEditText email, password;
    private FButton loginBtn;
    private LoginButton fbBtn;
    private ProgressDialog progressDialog;
    private Toolbar mToolBar;
    private TextView forgotpassword;
    EditText fgEmail;
    FButton cancel,next;


    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;
    //FirebaseDatabase
    FirebaseDatabase database;
    DatabaseReference user;
    FirebaseUser currentUser;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        progressDialog = new ProgressDialog(this);
        mToolBar = findViewById(R.id.main_tool_bar);
        forgotpassword = findViewById(R.id.lForgotPassword);

        callbackManager = CallbackManager.Factory.create();


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();;

        email = findViewById(R.id.lEmailText);
        password = findViewById(R.id.lPassText);
        loginBtn = findViewById(R.id.lDangnhap_Btn);

        fbBtn = findViewById(R.id.lFb_Btn);
        fbBtn.setReadPermissions("email", "public_profile");
        fbBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("FacebookS", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("FacebookC", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FacebookE", "facebook:onError", error);
            }
        });

        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Đăng nhập");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotDialog();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Logging in");
                progressDialog.setMessage("Please wait for a minute!");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                if(Common.isConnectedToInternet(getBaseContext())) {
                    String mail = email.getText().toString().trim();
                    String pass = password.getText().toString().trim();
                    boolean check = check_user(mail, pass);
                    if (check == true) {
                        login_user(mail, pass);
                    }
                    else {
                        progressDialog.dismiss();
                    }
                }
                else{
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            progressDialog.hide();
                            Toast.makeText(LoginMainActivity.this,"Kiểm tra kết nối mạng!",Toast.LENGTH_SHORT).show();
                        }
                    }, 3000);

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent mainIntent = new Intent(LoginMainActivity.this,MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginMainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showForgotDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginMainActivity.this);
        final View itemView = LayoutInflater.from(LoginMainActivity.this).inflate(R.layout.forgot_password_layout, null);
        fgEmail = itemView.findViewById(R.id.email_text);
        cancel = itemView.findViewById(R.id.cancel_forgot);
        next = itemView.findViewById(R.id.next_forgot);
        builder.setView(itemView);
        final AlertDialog alertDialog = builder.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(check_email(fgEmail.getText().toString().trim())){
                    progressDialog.setTitle("Logging in");
                    progressDialog.setMessage("Please wait for a minute!");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    mAuth.sendPasswordResetEmail(fgEmail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                alertDialog.dismiss();
                                progressDialog.dismiss();
                                Toast.makeText(LoginMainActivity.this,"Kiểm tra hướng dẫn trong email của bạn!",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                alertDialog.dismiss();
                                progressDialog.dismiss();
                                Toast.makeText(LoginMainActivity.this,"Không tìm thấy địa chỉ email!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }

    private boolean check_email(String fgEmail) {
        if(!TextUtils.isEmpty(fgEmail)){
            if(Patterns.EMAIL_ADDRESS.matcher(fgEmail).matches()){
                return true;
            }
            else{
                Toast.makeText(LoginMainActivity.this,"Nhập sai định dạng email !!!", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        else{
            Toast.makeText(LoginMainActivity.this,"Vui lòng nhập đầy đủ thông tin !!!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean check_user(String email, String firstpass){
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(firstpass)){
            if(Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return true;
            }
            else {
                Toast.makeText(LoginMainActivity.this,"Nhập sai định dạng email !!!", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        else{

            Toast.makeText(LoginMainActivity.this,"Vui lòng nhập đầy đủ thông tin !!!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void login_user(String mail, String pass) {

        mAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    uid = currentUser.getUid();
                    user = database.getReference("User").child(uid);
                    user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            user.setId(dataSnapshot.getKey());
                            Common.CurrentUser = user;
                            Intent mainIntent = new Intent(LoginMainActivity.this,MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    progressDialog.hide();
                    Toast.makeText(LoginMainActivity.this,"Tài khoản email hoặc mật khẩu không đúng!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
