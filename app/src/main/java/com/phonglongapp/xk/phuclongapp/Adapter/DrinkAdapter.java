package com.phonglongapp.xk.phuclongapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.phonglongapp.xk.phuclongapp.Utils.Common;
import com.phonglongapp.xk.phuclongapp.Database.ModelDB.Cart;
import com.phonglongapp.xk.phuclongapp.Database.ModelDB.Favorite;
import com.phonglongapp.xk.phuclongapp.Interface.ItemClickListener;
import com.phonglongapp.xk.phuclongapp.Model.Drink;
import com.phonglongapp.xk.phuclongapp.R;
import com.phonglongapp.xk.phuclongapp.ViewHolder.DrinkViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkViewHolder> {

    Context context;
    List<Drink> drinkList;
    ImageView image_banner, image_cold, image_hot;
    TextView drinkname, drinkprice;
    FloatingActionButton cart_btn, rating_btn;
    ElegantNumberButton numberButton;
    RatingBar ratingBar;
    int price;
    String status = "empty";
    FragmentActivity drinkactivity;

    public DrinkAdapter(Context context, List<Drink> drinkList, FragmentActivity drinkactivity) {
        this.context = context;
        this.drinkList = drinkList;
        this.drinkactivity = drinkactivity;
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.drink_layout,parent,false);
        return new DrinkViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DrinkViewHolder holder, final int position) {
        //Khởi tạo image
        if(!drinkList.get(position).getImage_cold().equals("empty")) {
            Picasso picasso = Picasso.with(context);
            picasso.setIndicatorsEnabled(false);
            picasso.load(drinkList.get(position).getImage_cold()).networkPolicy(NetworkPolicy.OFFLINE)
                    .into(holder.image_product, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso picasso = Picasso.with(context);
                            picasso.setIndicatorsEnabled(false);
                            picasso.load(drinkList.get(position).getImage_cold()).into(holder.image_product);
                        }
                    });
            status = "cold";
        }
        else if (!drinkList.get(position).getImage_hot().equals("empty")) {
            Picasso picasso = Picasso.with(context);
            picasso.setIndicatorsEnabled(false);
            picasso.load(drinkList.get(position).getImage_hot()).networkPolicy(NetworkPolicy.OFFLINE)
                    .into(holder.image_product, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso picasso = Picasso.with(context);
                            picasso.setIndicatorsEnabled(false);
                            picasso.load(drinkList.get(position).getImage_hot()).into(holder.image_product);
                        }
                    });
            status = "hot";
        }
        else {
            holder.image_product.setImageResource(R.drawable.thumb_default);

        }
        //Favorite System
        if(Common.favoriteRepository.isFavorite(Integer.parseInt(drinkList.get(position).getId())) == 1){
            holder.fav_btn.setImageResource(R.drawable.ic_favorite_green_24dp);
        }
        else{
            holder.fav_btn.setImageResource(R.drawable.ic_favorite_border_green_24dp);
        }
        holder.fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.favoriteRepository.isFavorite(Integer.parseInt(drinkList.get(position).getId())) != 1){
                    AddOrRemoveFavorite(drinkList.get(position),true);
                    holder.fav_btn.setImageResource(R.drawable.ic_favorite_green_24dp);
                }
                else{
                    AddOrRemoveFavorite(drinkList.get(position),false);
                    holder.fav_btn.setImageResource(R.drawable.ic_favorite_border_green_24dp);
                }
            }
        });
        //Khởi tạo price
        price = Integer.parseInt(drinkList.get(position).getPrice());
        holder.price_drink.setText(Common.ConvertIntToMoney(drinkList.get(position).getPrice()));
        holder.name_drink.setText(drinkList.get(position).getName());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v, final int position) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View itemView = LayoutInflater.from(context).inflate(R.layout.activity_drink_detail,null);

                //Ánh xạ
                rating_btn = itemView.findViewById(R.id.btn_rating);
                ratingBar = itemView.findViewById(R.id.rating_bar);
                image_banner = itemView.findViewById(R.id.image_detail_drink);
                image_cold = itemView.findViewById(R.id.cold_drink_image);
                image_hot = itemView.findViewById(R.id.hot_drink_image);

                drinkname = itemView.findViewById(R.id.drink_name);
                drinkprice = itemView.findViewById(R.id.drink_price);

                cart_btn = itemView.findViewById(R.id.btn_cart);
                numberButton = itemView.findViewById(R.id.elegant_btn);
                loadDrinkDetail(drinkList.get(position));

                image_cold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Picasso picasso = Picasso.with(v.getContext());
                        picasso.setIndicatorsEnabled(false);
                        picasso.load(drinkList.get(position).getImage_cold()).networkPolicy(NetworkPolicy.OFFLINE)
                                .into(image_banner, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Picasso picasso = Picasso.with(v.getContext());
                                        picasso.setIndicatorsEnabled(false);
                                        picasso.load(drinkList.get(position).getImage_cold()).into(image_banner);
                                    }
                                });
                        status = "cold";
                    }
                });
                image_hot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        Picasso picasso = Picasso.with(v.getContext());
                        picasso.setIndicatorsEnabled(false);
                        picasso.load(drinkList.get(position).getImage_hot()).networkPolicy(NetworkPolicy.OFFLINE)
                                .into(image_banner, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Picasso picasso = Picasso.with(v.getContext());
                                        picasso.setIndicatorsEnabled(false);
                                        picasso.load(drinkList.get(position).getImage_hot()).into(image_banner);
                                    }
                                });
                        status = "hot";
                    }
                });

                builder.setView(itemView);
                final AlertDialog alertDialog = builder.show();
                //Click rating button
                rating_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.hide();
                        showDialogRating();
                    }
                });
                //Click cart button
                cart_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Common.cartRepository.isCart(Integer.parseInt(drinkList.get(position).getId())) != 1) {
                            alertDialog.dismiss();
                            //Create DB

                            Cart cartItem = new Cart();
                            cartItem.cId = Integer.parseInt(drinkList.get(position).getId());
                            cartItem.cName = drinkList.get(position).getName();
                            cartItem.cQuanity = Integer.parseInt(numberButton.getNumber());
                            cartItem.cPrice = price;
                            cartItem.cStatus = status;
                            cartItem.cImageCold = drinkList.get(position).getImage_cold();
                            cartItem.cImageHot = drinkList.get(position).getImage_hot();
                            cartItem.cPriceItem = Integer.parseInt(drinkList.get(position).getPrice());
                            //Add to DB
                            Common.cartRepository.insertCart(cartItem);
                            Toast.makeText(context, "Đã thêm " + numberButton.getNumber() + " " + drinkList.get(position).getName() + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            alertDialog.dismiss();
                            Toast.makeText(context,"Sản phẩm đã có trong giỏ hàng!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            private void showDialogRating() {
                new AppRatingDialog.Builder()
                        .setPositiveButtonText("Chấp nhận")
                        .setNegativeButtonText("Hủy")
                        .setNoteDescriptions(Arrays.asList("Very Bad", "Not Good", "Normal", "Good", "Very Good"))
                        .setDefaultRating(2)
                        .setTitle("Rating this drink")
                        .setDescription("Please select start and give us your feedback")
                        .setTitleTextColor(R.color.colorPrimaryDark)
                        .setDescriptionTextColor(R.color.colorPrimaryDark)
                        .setHint("Write somethings ...")
                        .setCommentTextColor(R.color.colorWhite)
                        .setCommentBackgroundColor(R.color.colorPrimaryDark)
                        .setWindowAnimation(R.style.RatingDialogFadeAnim)
                        .create(drinkactivity)
                        .show();
            }

            private void loadDrinkDetail(final Drink drink) {
                if(!drink.getImage_cold().equals("empty")) {
                    Picasso picasso = Picasso.with(context);
                    picasso.setIndicatorsEnabled(false);
                    picasso.load(drink.getImage_cold()).networkPolicy(NetworkPolicy.OFFLINE)
                            .into(image_banner, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso picasso = Picasso.with(context);
                                    picasso.setIndicatorsEnabled(false);
                                    picasso.load(drink.getImage_cold()).into(image_banner);
                                }
                            });
                    status = "cold";
                }
                else if (!drink.getImage_hot().equals("empty")) {
                    Picasso picasso = Picasso.with(context);
                    picasso.setIndicatorsEnabled(false);
                    picasso.load(drink.getImage_hot()).networkPolicy(NetworkPolicy.OFFLINE)
                            .into(image_banner, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso picasso = Picasso.with(context);
                                    picasso.setIndicatorsEnabled(false);
                                    picasso.load(drink.getImage_hot()).into(image_banner);
                                }
                            });
                    status = "hot";
                }
                else {
                    image_banner.setImageResource(R.drawable.thumb_default);
                    status = "empty";
                }
                drinkname.setText(drink.getName().toString());
                drinkprice.setText(Common.ConvertIntToMoney(drink.getPrice()));
                if(drink.getImage_cold().equals("empty")){
                    image_cold.setVisibility(View.INVISIBLE);
                }
                if(drink.getImage_hot().equals("empty")){
                    image_hot.setVisibility(View.INVISIBLE);
                }
                if(drink.getImage_hot().equals("empty") && drink.getImage_cold().equals("empty")){
                    image_hot.setVisibility(View.INVISIBLE);
                    image_cold.setVisibility(View.INVISIBLE);
                }
                numberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                    @Override
                    public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                        price = Integer.parseInt(drink.getPrice()) * Integer.parseInt(numberButton.getNumber());
                        //Toast.makeText(context,price,Toast.LENGTH_SHORT).show();
                        drinkprice.setText(NumberFormat.getNumberInstance(Locale.US).format(price) + " VNĐ");
                    }
                });
            }
        });
    }

    private void AddOrRemoveFavorite(Drink drink, boolean isAdd) {
        Favorite favorite = new Favorite();
        favorite.fId = Integer.parseInt(drink.getId());
        favorite.fName = drink.getName();
        favorite.fImageCold = drink.getImage_cold();
        favorite.fImageHot = drink.getImage_hot();
        favorite.fPrice = Integer.parseInt(drink.getPrice());
        favorite.fMenu = drink.getMenuId();
        if(isAdd){
            Common.favoriteRepository.insertCart(favorite);
        }
        else {
            Common.favoriteRepository.deleteFavItem(favorite);
        }
    }
    @Override
    public int getItemCount() {
        return drinkList.size();
    }
}
