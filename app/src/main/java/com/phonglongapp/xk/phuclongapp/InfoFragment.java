package com.phonglongapp.xk.phuclongapp;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.phonglongapp.xk.phuclongapp.Utils.Common;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;
import info.hoang8f.widget.FButton;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {

    //Storage Firebase
    private StorageReference mStorageRef;

    //Firebase
    FirebaseDatabase database;
    DatabaseReference user;
    FirebaseUser currentUser;

    //View
    TextView btn_add_credit, btn_add_paypal, btn_add_voucher;
    MaterialEditText email_user, pass_old, pass_new_1, pass_new_2, name_user, address_user, phone_user;
    FButton accept_btn_my_account, accept_btn_password, accept_btn_details;
    Switch noty_switch;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;

    private CircleImageView mProfileImage;
    ImageView camera, cover_user;
    private int mMaxScrollSize;
    AppBarLayout appbarLayout;

    //Progress dialog
    ProgressDialog progressDialog;

    //Value Camera
    private static final int GALLERY_PICK = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Common.BackPress = 1;
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance();
        user = database.getReference("User");
        toolbar = view.findViewById(R.id.tool_bar_account);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_account);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.AppbarUser);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        appbarLayout = view.findViewById(R.id.app_bar_user);
        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();
        mProfileImage = view.findViewById(R.id.image_thumb_user);
        camera = view.findViewById(R.id.camera_user);
        cover_user = view.findViewById(R.id.image_avt_user);

        //Ánh xạ
        AnhXa(view);

        //Khởi tạo
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Init();
        //On click
        cover_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog builder = new Dialog(getActivity());
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        //nothing;
                    }
                });
                ImageView imageView = new ImageView(getActivity());
                if (!Common.CurrentUser.getImage().equals("empty")) {
                    Picasso.with(getActivity()).load(Common.CurrentUser.getImage()).into(imageView);
                }//set the image in dialog popup
                //below code fullfil the requirement of xml layout file for dialoge popup

                builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                builder.show();
            }
        });

        mStorageRef = FirebaseStorage.getInstance().getReference();

        //Check click
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery, "SELECT IMAGE"), GALLERY_PICK);
            }
        });

        checkClickBtn();


        super.onViewCreated(view, savedInstanceState);
    }

    private void checkClickBtn() {
        //Check My account click
        accept_btn_my_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailText = email_user.getText().toString();
                AuthCredential credential = EmailAuthProvider.getCredential(Common.CurrentUser.getEmail(), Common.CurrentUser.getPassword());
                currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!emailText.isEmpty()) {
                            progressDialog = new ProgressDialog(getActivity());
                            progressDialog.setTitle("Uploading email...");
                            progressDialog.setMessage("Please wait for a minute!");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            currentUser.updateEmail(emailText).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.child(Common.CurrentUser.getId()).child("email").setValue(emailText).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Common.CurrentUser.setEmail(emailText);
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getActivity(), "Thay đổi email thành công!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "Vui lòng nhập địa chỉ email!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        //Check Password Click
        accept_btn_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pass_first = pass_old.getText().toString();
                final String pass_second1 = pass_new_1.getText().toString();
                final String pass_second2 = pass_new_2.getText().toString();
                Log.d("Password", pass_first + " " + pass_second1 + " " + pass_second2);
                AuthCredential credential = EmailAuthProvider.getCredential(Common.CurrentUser.getEmail(), Common.CurrentUser.getPassword());
                currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (!TextUtils.isEmpty(pass_first) && !TextUtils.isEmpty(pass_second1) && !TextUtils.isEmpty(pass_second2)) {
                                progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setTitle("Uploading password...");
                                progressDialog.setMessage("Please wait for a minute!");
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.show();
                                if (pass_first.equals(Common.CurrentUser.getPassword())) {
                                    if (pass_second1.equals(pass_second2)) {
                                        currentUser.updatePassword(pass_second1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    user.child(Common.CurrentUser.getId()).child("password").setValue(pass_second1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Common.CurrentUser.setPassword(pass_second1);
                                                                pass_old.setText(pass_second1);
                                                                pass_new_1.setText("");
                                                                pass_new_2.setText("");
                                                                progressDialog.dismiss();
                                                                Toast.makeText(getActivity(), "Thay đổi password thành công!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(), "Nhập sai mật khẩu mới!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(), "Nhập sai mật khẩu cũ!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Vui lòng nhập đủ mật khẩu!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
        //Check Details Click
        

        //Check Noty click
    }

    private void AnhXa(View view) {
        btn_add_credit = view.findViewById(R.id.credit_btn);
        btn_add_paypal = view.findViewById(R.id.paypal_btn);
        btn_add_voucher = view.findViewById(R.id.voucher_btn);
        email_user = view.findViewById(R.id.email_info);
        pass_old = view.findViewById(R.id.password_old_info);
        pass_new_1 = view.findViewById(R.id.password_new_1);
        pass_new_2 = view.findViewById(R.id.password_new_2);
        name_user = view.findViewById(R.id.name_info);
        address_user = view.findViewById(R.id.address_info);
        phone_user = view.findViewById(R.id.phone_info);
        accept_btn_my_account = view.findViewById(R.id.accept_my_account_btn);
        accept_btn_password = view.findViewById(R.id.accept_password_btn);
        accept_btn_details = view.findViewById(R.id.accept_details_btn);
        noty_switch = view.findViewById(R.id.noty_switch_btn);
    }

    private void Init() {
        if (!Common.CurrentUser.getImage().equals("empty")) {
            Picasso.with(getActivity()).load(Common.CurrentUser.getImage()).into(cover_user);
            Picasso.with(getActivity()).load(Common.CurrentUser.getImage()).into(mProfileImage);
        }
        email_user.setText(Common.CurrentUser.getEmail());
        name_user.setText(Common.CurrentUser.getName());
        pass_old.setText(Common.CurrentUser.getPassword());
        if (!Common.CurrentUser.getAddress().equals("empty")) {
            address_user.setText(Common.CurrentUser.getAddress());
        }
        if (!Common.CurrentUser.getPhone().equals("empty")) {
            phone_user.setText(Common.CurrentUser.getPhone());
        }

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null && data.getData() != null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading image...");
            progressDialog.setMessage("Please wait for a minute!");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            Uri imageUri = data.getData();

            final StorageReference riversRef = mStorageRef.child("Avatar_User").child(Common.CurrentUser.getId() + ".jpg");
            riversRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    // Get a URL to the uploaded content
                    riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();
                            Common.CurrentUser.setImage(downloadUrl);
                            if (!Common.CurrentUser.getImage().equals("empty")) {
                                Picasso.with(getActivity()).load(Common.CurrentUser.getImage()).into(cover_user);
                                Picasso.with(getActivity()).load(Common.CurrentUser.getImage()).into(mProfileImage);
                            }
                            user.child(Common.CurrentUser.getId()).child("image").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(), "Thay đổi ảnh đại diện thành công!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getActivity(), "Thay đổi ảnh đại diện thật  bại!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            mProfileImage.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(300)
                    .start();
            camera.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(300)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            mProfileImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();
            camera.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }
}
