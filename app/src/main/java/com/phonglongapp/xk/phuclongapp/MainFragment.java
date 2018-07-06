package com.phonglongapp.xk.phuclongapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;
import com.phonglongapp.xk.phuclongapp.Adapter.CategoryAdapter;
import com.phonglongapp.xk.phuclongapp.Adapter.OrderAdapter;
import com.phonglongapp.xk.phuclongapp.Model.Order;
import com.phonglongapp.xk.phuclongapp.Utils.Common;
import com.phonglongapp.xk.phuclongapp.Database.DataSource.CartRepository;
import com.phonglongapp.xk.phuclongapp.Database.DataSource.FavoriteRepository;
import com.phonglongapp.xk.phuclongapp.Database.Local.CartDataSource;
import com.phonglongapp.xk.phuclongapp.Database.Local.DrinkRoomDatabase;
import com.phonglongapp.xk.phuclongapp.Database.Local.FavoriteDateSource;
import com.phonglongapp.xk.phuclongapp.Model.Banner;
import com.phonglongapp.xk.phuclongapp.Model.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainFragment extends Fragment {

    private Toolbar toolbar;
    private RecyclerView list_menu;
    ImageView cartBtn, user_avt;
    TextView user_name, user_email, user_info, user_name_drink, user_quanity_drink, user_time_drink, user_all_drink, user_log_out;

    //FirebaseDatabase
    FirebaseDatabase database;
    DatabaseReference catogories, banners, order;

    //Slider
    HashMap<String, String> banner_list;
    SliderLayout sliderLayout;

    //Category
    List<Category> categoryArrayList;

    //Adapter
    CategoryAdapter adapter;

    //Notification
    NotificationBadge badge;

    Point p;

    RelativeLayout user_background;
    LinearLayout user_foreground;

    Order temp;

    public MainFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        categoryArrayList = new ArrayList<>();

        //Init Firebase;
        database = FirebaseDatabase.getInstance();
        catogories = database.getReference("Category");
        banners = database.getReference("Banner");
        order = database.getReference("Order");
        catogories.keepSynced(true);
        temp = new Order();
        temp = null;

        toolbar = view.findViewById(R.id.main_tool_bar);
        list_menu = view.findViewById(R.id.list_category);
        list_menu.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        list_menu.setHasFixedSize(true);

        checkOrder();

        //Set adapter
        adapter = new CategoryAdapter(getActivity(), categoryArrayList);
        loadMenu();
        list_menu.setAdapter(adapter);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Phúc Long");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setLogo(R.drawable.logo_header_2);
        toolbar.setTitleTextAppearance(getActivity(), R.style.RobotoBoldTextAppearance);

        //Slider
        sliderLayout = view.findViewById(R.id.slider);
        banner_list = new HashMap<>();
        setupSlider();

        //Init Database
        initDB();
        super.onViewCreated(view, savedInstanceState);
    }

    private void initDB() {
        Common.drinkroomDatabase = DrinkRoomDatabase.getInstance(getActivity());
        Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.drinkroomDatabase.cartDAO()));
        Common.favoriteRepository = FavoriteRepository.getInstance(FavoriteDateSource.getInstance(Common.drinkroomDatabase.favoriteDAO()));
    }

    private void setupSlider() {
        banners.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    Banner banner = post.getValue(Banner.class);
                    banner_list.put(banner.getName(), banner.getImage());
                }
                for (String nameBanner : banner_list.keySet()) {

                    //Create slider
                    TextSliderView textSliderView = new TextSliderView(getActivity());
                    textSliderView.description(nameBanner)
                            .image(banner_list.get(nameBanner))
                            .setScaleType(BaseSliderView.ScaleType.Fit);
                    sliderLayout.addSlider(textSliderView);
                    banners.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Fade);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3000);
    }

    private void loadMenu() {
        catogories.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Category cate = dataSnapshot.getValue(Category.class);
                cate.setId(dataSnapshot.getKey());
                categoryArrayList.add(cate);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Category cate = dataSnapshot.getValue(Category.class);
                cate.setId(dataSnapshot.getKey());
                for(int i = 0; i < categoryArrayList.size();i++){
                    if(categoryArrayList.get(i).getId().equals(cate.getId())){
                        categoryArrayList.remove(i);
                        categoryArrayList.add(i,cate);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Category cate = dataSnapshot.getValue(Category.class);
                cate.setId(dataSnapshot.getKey());
                for(int i = 0; i < categoryArrayList.size();i++){
                    if(categoryArrayList.get(i).getId().equals(cate.getId())){
                        categoryArrayList.remove(i);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onStop() {
        super.onStop();
        sliderLayout.stopAutoCycle();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main_toolbar, menu);
        View view = menu.findItem(R.id.icon_cart_menu).getActionView();
        badge = view.findViewById(R.id.badge);
        cartBtn = view.findViewById(R.id.cart_icon);
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartIntent = new Intent(getActivity(), CartActivity.class);
                startActivity(cartIntent);
            }
        });
        updateCartCount();
    }

    private void updateCartCount() {
        if (badge == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Common.cartRepository.countCartItem(Common.CurrentUser.getId()) == 0)
                    badge.setVisibility(View.INVISIBLE);
                else {
                    badge.setVisibility(View.VISIBLE);
                    badge.setText(String.valueOf(Common.cartRepository.countCartItem(Common.CurrentUser.getId())));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.icon_account:
                View menuItem = getView().findViewById(R.id.icon_account);
                showPopup(menuItem);
                break;
            case R.id.icon_search:
                Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                startActivity(searchIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCartCount();
    }


    public void showPopup(View anchorView) {
        // Inflate the popup_layout.xml
        View layout = getActivity().getLayoutInflater().inflate(R.layout.popup_menu_layout, null);
        final PopupWindow popup = new PopupWindow(layout,
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        // Creating the PopupWindow
        popup.setFocusable(true);

        popup.setBackgroundDrawable(new BitmapDrawable());

        //Ánh xạ

        user_avt = layout.findViewById(R.id.user_avt);
        user_email = layout.findViewById(R.id.user_email);
        user_name = layout.findViewById(R.id.user_name);
        user_info = layout.findViewById(R.id.user_info);

        user_all_drink = layout.findViewById(R.id.user_all_drink);
        user_quanity_drink = layout.findViewById(R.id.user_quanity_drink);
        user_time_drink = layout.findViewById(R.id.user_time_drink);
        user_name_drink = layout.findViewById(R.id.user_name_drink);

        user_log_out = layout.findViewById(R.id.user_log_out);

        user_background = layout.findViewById(R.id.user_background);
        user_foreground = layout.findViewById(R.id.user_foreground);



        if(temp == null){
            user_foreground.setVisibility(View.GONE);
            user_background.setVisibility(View.VISIBLE);
        }
        else{
            user_background.setVisibility(View.GONE);
            user_foreground.setVisibility(View.VISIBLE);
            user_name_drink.setText(temp.getCartList().get(0).cName);
            user_quanity_drink.setText("Số lượng "+temp.getCartList().get(0).cQuanity+"");
            user_time_drink.setText(Common.getTimeAgo(Long.parseLong(temp.getId()),getActivity()));
        }

        //Set giá trị
        //Toast.makeText(getActivity(),Common.CurrentUser.getEmail(),Toast.LENGTH_LONG).show();
        if (!Common.CurrentUser.getImage().equals("empty")) {
            Picasso.with(getActivity()).load(Common.CurrentUser.getImage()).into(user_avt);
        }
        user_email.setText(Common.CurrentUser.getEmail());
        user_name.setText(Common.CurrentUser.getName());

        //View Info
        user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                InfoFragment infoFragment = new InfoFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragment, infoFragment, "InfoFragment").addToBackStack(null).commit();
            }
        });

        //Log out
        user_log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        //View all drink

        user_all_drink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(temp == null){

                }
                else {
                    popup.dismiss();
                    ViewDrinkFragment viewDrinkFragment = new ViewDrinkFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment, viewDrinkFragment, "InfoFragment").addToBackStack(null).commit();
                }
            }
        });

        int location[] = new int[2];

        // Get the View's(the one that was clicked in the Fragment) location
        anchorView.getLocationOnScreen(location);

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(anchorView, Gravity.NO_GRAVITY,
                location[0], location[1] + anchorView.getHeight());

    }
    private Order checkOrder() {
        order.orderByChild("userID").equalTo(Common.CurrentUser.getId()).limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                temp = dataSnapshot.getValue(Order.class);
                temp.setId(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                temp = null;
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return temp;
    }
}
