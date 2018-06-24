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
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

import java.util.HashMap;
import java.util.HashSet;

import info.hoang8f.widget.FButton;

public class RegisterActivity extends AppCompatActivity {

    private MaterialEditText r_pass, r_username, r_email, r_secondpass;
    private FButton r_registerBtn;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference user,User;
    FirebaseUser currentUser;
    String uid;
    private ProgressDialog progressDialog;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mToolbar = findViewById(R.id.main_tool_bar);

        r_pass = findViewById(R.id.rPass);
        r_email = findViewById(R.id.rEmail);
        r_username = findViewById(R.id.rUsername);
        r_secondpass = findViewById(R.id.rSecondPass);
        r_registerBtn = findViewById(R.id.rRegister_Btn);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Đăng kí");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        r_registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = r_username.getText().toString().trim();
                String firstpass = r_pass.getText().toString().trim();
                String secondpass = r_secondpass.getText().toString().trim();
                String email = r_email.getText().toString().trim();

                boolean check = check_user(username,email,firstpass,secondpass);
                if(check == true) {
                    progressDialog.setTitle("Registering User");
                    progressDialog.setMessage("Please wait for a minute!");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    register_user(username, email, firstpass);
                }
            }
        });
    }

    private boolean check_user(String username, String email, String firstpass, String secondpass){
        if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(firstpass) && !TextUtils.isEmpty(secondpass)){
            if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                if(firstpass.equals(secondpass)) {
                    return true;
                }
                else {
                    Toast.makeText(RegisterActivity.this,"Nhập sai mật khẩu !!!",Toast.LENGTH_LONG).show();
                    return false;
                }
            }
            else{

                Toast.makeText(RegisterActivity.this,"Nhập sai định dạng email !!!", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        else{

            Toast.makeText(RegisterActivity.this,"Vui lòng nhập đầy đủ thông tin !!!", Toast.LENGTH_LONG).show();
            return false;
        }
    }
    private void register_user(final String username, final String email, final String firstpass) {

        mAuth.createUserWithEmailAndPassword(email,firstpass).addOnCompleteListener(RegisterActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    FirebaseUser current_user = mAuth.getCurrentUser();
                    String uid = current_user.getUid();

                    user = database.getReference().child("User").child(uid);
                    HashMap<String,String> userMap = new HashMap<>();
                    userMap.put("name",username);
                    userMap.put("address","empty");
                    userMap.put("phone","empty");
                    userMap.put("image","empty");
                    userMap.put("email",email);
                    user.setValue(userMap);

                    Toast.makeText(RegisterActivity.this,"Đăng kí thành công",Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("Bạn có muốn đăng nhập không?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.setNegativeButton("Chấp nhận", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            progressDialog.setTitle("Logging in");
                            progressDialog.setMessage("Please wait for a minute!");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            if(Common.isConnectedToInternet(RegisterActivity.this)) {
                                login(email,firstpass);
                            }
                            else{
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        progressDialog.hide();
                                        Toast.makeText(RegisterActivity.this,"Kiểm tra kết nối mạng!",Toast.LENGTH_SHORT).show();
                                    }
                                }, 3000);

                            }
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else {
                    progressDialog.hide();
                    Toast.makeText(RegisterActivity.this,"Tài khoản đã có người sử dụng!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void login(String email, String firstpass) {
        mAuth.signInWithEmailAndPassword(email,firstpass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    uid = currentUser.getUid();
                    User = database.getReference("User").child(uid);;
                    User.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            user.setId(dataSnapshot.getKey());
                            Common.CurrentUser = user;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
                else{
                    progressDialog.hide();
                    Toast.makeText(RegisterActivity.this,"Tài khoản email hoặc mật khẩu không đúng!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
