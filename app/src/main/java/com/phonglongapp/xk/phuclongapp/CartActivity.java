package com.phonglongapp.xk.phuclongapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phonglongapp.xk.phuclongapp.Adapter.CartAdapter;
import com.phonglongapp.xk.phuclongapp.Utils.Common;
import com.phonglongapp.xk.phuclongapp.Database.ModelDB.Cart;
import com.phonglongapp.xk.phuclongapp.Utils.RecyclerItemTouchHelper;
import com.phonglongapp.xk.phuclongapp.Interface.RecyclerItemTouchHelperListener;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.widget.FButton;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener{

    RelativeLayout relativeLayout;
    RecyclerView cartList;
    FButton placeButton;
    TextView total;
    CartAdapter cartAdapter;
    List<Cart> local_listcart = new ArrayList<Cart>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        relativeLayout = findViewById(R.id.cart_layout);

        cartList = findViewById(R.id.cart_list);
        placeButton = findViewById(R.id.order_button);
        total = findViewById(R.id.total_cart);

        cartList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        cartList.setHasFixedSize(true);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(cartList);

        loadCartItem();
    }

    @SuppressLint("CheckResult")
    private void loadCartItem() {
        Common.cartRepository.getCartItems()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Cart>>() {
                    @Override
                    public void accept(List<Cart> carts) throws Exception {
                        displayCart(carts);
                    }
                });
    }

    private void displayCart(List<Cart> carts) {
        local_listcart = carts;
        cartAdapter = new CartAdapter(this,carts,total);
        cartList.setAdapter(cartAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCartItem();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof RecyclerView.ViewHolder){
            String name = local_listcart.get(viewHolder.getAdapterPosition()).cName;

            final Cart deleteItem = local_listcart.get(viewHolder.getAdapterPosition());
            final int deleteItemIndex = viewHolder.getAdapterPosition();

            cartAdapter.removeCart(deleteItemIndex);

            Common.cartRepository.deleteCartItem(deleteItem);

            Snackbar snackbar = Snackbar.make(relativeLayout,new StringBuilder(name).append(" đã được xóa khỏi giỏ hàng").toString(),
                    Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartAdapter.restoreCart(deleteItem,deleteItemIndex);
                    Common.cartRepository.insertCart(deleteItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
