package com.phonglongapp.xk.phuclongapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.kofigyan.stateprogressbar.components.StateItem;
import com.kofigyan.stateprogressbar.listeners.OnStateItemClickListener;
import com.phonglongapp.xk.phuclongapp.Adapter.CartAdapter;
import com.phonglongapp.xk.phuclongapp.Adapter.ViewPagerAdapter;
import com.phonglongapp.xk.phuclongapp.Model.MyResponse;
import com.phonglongapp.xk.phuclongapp.Model.Notification;
import com.phonglongapp.xk.phuclongapp.Model.Order;
import com.phonglongapp.xk.phuclongapp.Model.Sender;
import com.phonglongapp.xk.phuclongapp.Model.Token;
import com.phonglongapp.xk.phuclongapp.Retrofit.APIService;
import com.phonglongapp.xk.phuclongapp.Utils.Common;
import com.phonglongapp.xk.phuclongapp.Database.ModelDB.Cart;
import com.phonglongapp.xk.phuclongapp.Utils.CustomViewPager;
import com.phonglongapp.xk.phuclongapp.Utils.RecyclerItemTouchHelper;
import com.phonglongapp.xk.phuclongapp.Interface.RecyclerItemTouchHelperListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.stepstone.apprating.C;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.widget.FButton;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    EditText comment_text;
    RelativeLayout relativeLayout, existLayout, emptyLayout;
    RecyclerView cartList;
    FButton placeButton;
    TextView total;
    CartAdapter cartAdapter;
    List<Cart> local_listcart = new ArrayList<>();
    FrameLayout details, payment, comment;
    FButton next_details, next_payment, back_payment, next_comment, back_comment;
    MaterialEditText name_detais, address_details, phone_details;
    RadioGroup radioGroup;

    //Firebase
    FirebaseDatabase database;
    DatabaseReference orderDatabase;
    APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        apiService = Common.getFCMService();

        relativeLayout = findViewById(R.id.cart_layout);
        existLayout = findViewById(R.id.cart_exist_layout);
        emptyLayout = findViewById(R.id.empty_cart_layout);

        database = FirebaseDatabase.getInstance();
        orderDatabase = database.getReference("Order");

        cartList = findViewById(R.id.cart_list);
        placeButton = findViewById(R.id.order_button);
        total = findViewById(R.id.total_cart);

        cartList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        cartList.setHasFixedSize(true);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(cartList);

        loadCartItem();

        placeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.cartRepository.countCartItem(Common.CurrentUser.getId()) != 0) {
                    final Order order = new Order();
                    String[] descriptionData = {"Details", "Payment", "Comment"};
                    final AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                    final View itemView = LayoutInflater.from(CartActivity.this).inflate(R.layout.dialog_submit_place_order, null);
                    final StateProgressBar stateProgressBar = itemView.findViewById(R.id.state_process_bar);
                    stateProgressBar.setStateDescriptionData(descriptionData);
                    details = itemView.findViewById(R.id.dialog_details);
                    payment = itemView.findViewById(R.id.dialog_payment);
                    comment = itemView.findViewById(R.id.dialog_comment);
                    payment.setVisibility(View.GONE);
                    comment.setVisibility(View.GONE);

                    //Ánh xạ
                    name_detais = itemView.findViewById(R.id.name_dialog);
                    address_details = itemView.findViewById(R.id.address_dialog);
                    phone_details = itemView.findViewById(R.id.phone_dialog);
                    radioGroup = itemView.findViewById(R.id.radio_group);
                    comment_text = itemView.findViewById(R.id.comment_text);

                    next_details = details.findViewById(R.id.dialog_next_details_btn);
                    back_payment = payment.findViewById(R.id.dialog_back_payment_btn);
                    next_payment = payment.findViewById(R.id.dialog_next_payment_btn);
                    back_comment = comment.findViewById(R.id.dialog_back_comment_btn);
                    next_comment = comment.findViewById(R.id.dialog_next_comment_btn);

                    //Init Details

                    name_detais.setText(Common.CurrentUser.getName());
                    ;

                    if (!Common.CurrentUser.getAddress().equals("empty")) {
                        address_details.setText(Common.CurrentUser.getAddress());
                    }
                    if (!Common.CurrentUser.getPhone().equals("empty")) {
                        phone_details.setText(Common.CurrentUser.getPhone());
                    }

                    //Check Button onClick

                    next_details.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(name_detais.getText().toString()) && !TextUtils.isEmpty(address_details.getText().toString()) && !TextUtils.isEmpty(phone_details.getText().toString())) {
                                order.setName(name_detais.getText().toString());
                                order.setAddress(address_details.getText().toString());
                                order.setPhone(phone_details.getText().toString());
                                details.setVisibility(View.GONE);
                                payment.setVisibility(View.VISIBLE);
                                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                            } else {
                                Toast.makeText(CartActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    back_payment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            payment.setVisibility(View.GONE);
                            details.setVisibility(View.VISIBLE);
                            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                        }
                    });
                    next_payment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int radioID = radioGroup.getCheckedRadioButtonId();
                            switch (radioID) {
                                case R.id.radio_credit:
                                    order.setPayment("Credit card");
                                    break;
                                case R.id.radio_paypal:
                                    order.setPayment("Paypal card");
                                    break;
                                case R.id.radi_cash_on_delivery:
                                    order.setPayment("Cash on delivery");
                                    break;
                            }
                            payment.setVisibility(View.GONE);
                            comment.setVisibility(View.VISIBLE);
                            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                        }
                    });

                    back_comment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            comment.setVisibility(View.GONE);
                            payment.setVisibility(View.VISIBLE);
                            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                        }
                    });


                    builder.setView(itemView);
                    final AlertDialog alertDialog = builder.show();

                    next_comment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            order.setPrice(total.getText().toString());
                            order.setNote(comment_text.getText().toString());
                            order.setStatus("0");
                            order.setCartList(local_listcart);
                            order.setUserID(Common.CurrentUser.getId());

                            stateProgressBar.setAllStatesCompleted(true);

                            final ProgressDialog progressDialog;

                            progressDialog = new ProgressDialog(CartActivity.this);
                            progressDialog.setTitle("Ordering...");
                            progressDialog.setMessage("Please wait for a minute!");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            final String idOrder = String.valueOf(System.currentTimeMillis());

                            orderDatabase.child(idOrder).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        alertDialog.dismiss();
                                        progressDialog.dismiss();

                                        Common.cartRepository.emptyCart();

                                        Toast.makeText(CartActivity.this, "Đặt món thành công! Xin cám ơn quý khách", Toast.LENGTH_LONG).show();


                                        total.setText("0 VNĐ");

                                        sendNotification(idOrder, alertDialog, progressDialog);



                                        loadCartItem();
                                    }
                                }
                            });


                        }
                    });
                } else {

                }
            }
        });
    }

    private void sendNotification(final String idUser, final AlertDialog alertDialog, final ProgressDialog progressDialog) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Token");
        tokens.orderByChild("tokenServer").equalTo(true).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Token serverToken = dataSnapshot.getValue(Token.class);
                Notification notification = new Notification("Có order mới #"+ idUser,"New Order");
                Sender content = new Sender(serverToken.getToken(),notification);

                apiService.sendNoti(content).enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                        if(response.body().success == 1){
                            finish();
                        }
                        else {
                            Toast.makeText(CartActivity.this, "Lỗi bất ngờ", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {
                        Log.e("Errorr",t.getMessage());
                    }
                });
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

    @SuppressLint("CheckResult")
    private void loadCartItem() {
        List<Cart> carts = Common.cartRepository.getCartByUserId(Common.CurrentUser.getId());

        if (Common.cartRepository.countCartItem(Common.CurrentUser.getId()) == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
        } else {
            existLayout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            displayCart(carts);
        }

    }

    private void displayCart(List<Cart> carts) {
        local_listcart = carts;
        cartAdapter = new CartAdapter(this, carts, total);
        cartList.setAdapter(cartAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCartItem();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof RecyclerView.ViewHolder) {
            String name = local_listcart.get(viewHolder.getAdapterPosition()).cName;

            final Cart deleteItem = local_listcart.get(viewHolder.getAdapterPosition());
            final int deleteItemIndex = viewHolder.getAdapterPosition();

            cartAdapter.removeCart(deleteItemIndex);

            Common.cartRepository.deleteCartItem(deleteItem);

            Snackbar snackbar = Snackbar.make(relativeLayout, new StringBuilder(name).append(" đã được xóa khỏi giỏ hàng").toString(),
                    Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartAdapter.restoreCart(deleteItem, deleteItemIndex);
                    Common.cartRepository.insertCart(deleteItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
            if (Common.cartRepository.countCartItem(Common.CurrentUser.getId()) == 0) {
                total.setText("0 VNĐ");
            }
        }
    }
}
