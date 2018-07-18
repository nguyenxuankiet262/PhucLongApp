package com.phonglongapp.xk.phuclongapp.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.phonglongapp.xk.phuclongapp.MainActivity;
import com.phonglongapp.xk.phuclongapp.Model.Rating;
import com.phonglongapp.xk.phuclongapp.Model.User;
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
import com.squareup.picasso.Target;
import com.stepstone.apprating.AppRatingDialog;

import java.text.NumberFormat;
import java.util.ArrayList;
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

    List<Rating> ratingList;
    List<User> userList;

    CommentAdapter adapter;
    RecyclerView listCmt;

    //FirebaseDatabase
    FirebaseDatabase database;
    DatabaseReference rating, user;
    ShareDialog shareDialog;

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();
            if (ShareDialog.canShow(SharePhotoContent.class)) {
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                shareDialog.show(content);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Toast.makeText(context, "Faild", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    public DrinkAdapter(Context context, List<Drink> drinkList, FragmentActivity drinkactivity) {
        this.context = context;
        this.drinkList = drinkList;
        this.drinkactivity = drinkactivity;
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.drink_layout, parent, false);
        return new DrinkViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DrinkViewHolder holder, final int position) {
        //Init facebook
        shareDialog = new ShareDialog(drinkactivity);
        holder.share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drinkList.get(position).getImage_cold().equals("empty")) {
                    Picasso.with(context).load(drinkList.get(position).getImage_cold()).into(target);
                } else if (!drinkList.get(position).getImage_hot().equals("empty")) {
                    Picasso.with(context).load(drinkList.get(position).getImage_hot()).into(target);
                }
            }
        });
        //Khởi tạo image
        if (!drinkList.get(position).getImage_cold().equals("empty")) {
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
        } else if (!drinkList.get(position).getImage_hot().equals("empty")) {
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
        } else {
            holder.image_product.setImageResource(R.drawable.thumb_default);
        }
        //Favorite System
        if (Common.favoriteRepository.isFavorite(Integer.parseInt(drinkList.get(position).getId()), Common.CurrentUser.getId()) == 1) {
            Log.d("EMM User_Fav", Common.CurrentUser.getId());
            holder.fav_btn.setImageResource(R.drawable.ic_favorite_green_24dp);
        } else {
            holder.fav_btn.setImageResource(R.drawable.ic_favorite_border_green_24dp);
        }
        holder.fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.favoriteRepository.isFavorite(Integer.parseInt(drinkList.get(position).getId()), Common.CurrentUser.getId()) != 1) {
                    AddOrRemoveFavorite(drinkList.get(position), true);
                    holder.fav_btn.setImageResource(R.drawable.ic_favorite_green_24dp);
                } else {
                    AddOrRemoveFavorite(drinkList.get(position), false);
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
                ratingList = new ArrayList<>();
                userList = new ArrayList<>();
                Common.idDrink = drinkList.get(position).getId();
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View itemView = LayoutInflater.from(context).inflate(R.layout.activity_drink_detail, null);
                listCmt = itemView.findViewById(R.id.list_comment);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
                mLayoutManager.setReverseLayout(true);
                mLayoutManager.setStackFromEnd(true);
                listCmt.setLayoutManager(mLayoutManager);
                database = FirebaseDatabase.getInstance();
                rating = database.getReference("Rating");
                user = database.getReference("User");
                //Ánh xạ listCmt
                adapter = new CommentAdapter(context, ratingList, userList);
                listCmt.setAdapter(adapter);
                getItemCmt(drinkList.get(position).getId());

                //Ánh xạ
                loadRating(drinkList.get(position).getId(), position);

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
                        if (Common.cartRepository.isCart(Integer.parseInt(drinkList.get(position).getId()), Common.CurrentUser.getId()) != 1) {
                            alertDialog.dismiss();
                            //Create DB
                            Cart cartItem = new Cart();
                            cartItem.uId = Common.CurrentUser.getId();
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
                        } else {
                            alertDialog.dismiss();
                            Toast.makeText(context, "Sản phẩm đã có trong giỏ hàng!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            private void showDialogRating() {
                new AppRatingDialog.Builder()
                        .setPositiveButtonText("Chấp nhận")
                        .setNegativeButtonText("Hủy")
                        .setNoteDescriptions(Arrays.asList("Very Bad", "Not Good", "Normal", "Good", "Very Good"))
                        .setDefaultRating(5)
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
                if (!drink.getImage_cold().equals("empty")) {
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
                } else if (!drink.getImage_hot().equals("empty")) {
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
                } else {
                    image_banner.setImageResource(R.drawable.thumb_default);
                    status = "empty";
                }
                drinkname.setText(drink.getName().toString());
                drinkprice.setText(Common.ConvertIntToMoney(drink.getPrice()));
                if (drink.getImage_cold().equals("empty")) {
                    image_cold.setVisibility(View.INVISIBLE);
                }
                if (drink.getImage_hot().equals("empty")) {
                    image_hot.setVisibility(View.INVISIBLE);
                }
                if (drink.getImage_hot().equals("empty") && drink.getImage_cold().equals("empty")) {
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

    private void getItemCmt(String id) {
        rating.orderByChild("status").equalTo("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    Rating rate = post.getValue(Rating.class);
                    rate.setId(post.getKey());
                    ratingList.add(rate);
                    user.child(rate.getId()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            user.setId(dataSnapshot.getKey());
                            userList.add(user);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadRating(String id, final int position) {
        rating.child(drinkList.get(position).getId()).addValueEventListener(new ValueEventListener() {
            int count = 0, sum = 0;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Rating item = postSnapshot.getValue(Rating.class);
                    item.setId(postSnapshot.getKey());
                    item.setDrinkId(drinkList.get(position).getId());
                    sum += Integer.parseInt(item.getRate());
                    count++;
                }
                if (count != 0) {
                    float average = sum / count;
                    ratingBar.setRating(average);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void AddOrRemoveFavorite(Drink drink, boolean isAdd) {
        Favorite favorite = new Favorite();
        favorite.uId = Common.CurrentUser.getId();
        favorite.fId = Integer.parseInt(drink.getId());
        favorite.fName = drink.getName();
        favorite.fImageCold = drink.getImage_cold();
        favorite.fImageHot = drink.getImage_hot();
        favorite.fPrice = Integer.parseInt(drink.getPrice());
        favorite.fMenu = drink.getMenuId();
        if (isAdd) {
            Common.favoriteRepository.insertCart(favorite);
        } else {
            Common.favoriteRepository.deleteFavItem(favorite);
        }
    }

    @Override
    public int getItemCount() {
        return drinkList.size();
    }
}
