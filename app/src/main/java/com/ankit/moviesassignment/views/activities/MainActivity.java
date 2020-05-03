package com.ankit.moviesassignment.views.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ankit.moviesassignment.model.classmodel.Userdata;
import com.ankit.moviesassignment.views.adapters.HomePageAdapter;
import com.ankit.moviesassignment.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public static ArrayList<Integer> favorite_movie_ids = new ArrayList<>();
    LinearLayout nav_header;
    Intent intent;
    Integer main_id;
    TextView name, email;
    CircleImageView profile_pic;
    public static Userdata userdata = new Userdata();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        intent = getIntent();

        nav_header = findViewById(R.id.nav_header);
        View header_view = navigationView.inflateHeaderView(R.layout.nav_header_main);

        navigationView.setNavigationItemSelectedListener(this);

        name = header_view.findViewById(R.id.name);
        email = header_view.findViewById(R.id.email);
        profile_pic = header_view.findViewById(R.id.profile_pic);
        name.setText(userdata.getName());
        email.setText(userdata.getEmail());
        if(userdata.getFilepath()!=null) {
            File f = new File(userdata.getFilepath());
            Picasso.with(this).load(f).fit().centerCrop().into(profile_pic);
        }

        main_id = intent.getIntExtra("main_id", 0);

        if(main_id == 0) {
            if(getFavoritesIdsArrayList() == null){
                favorite_movie_ids = new ArrayList<>();
            }else {
                favorite_movie_ids.addAll(getFavoritesIdsArrayList());
            }
        }

        header_view.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setCustomView(View.inflate(this, R.layout.custom_tab_layout_now_playing, null)));
        tabLayout.addTab(tabLayout.newTab().setCustomView(View.inflate(this, R.layout.custom_tab_layout_upcoming, null)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.pager);
        final HomePageAdapter adapter = new HomePageAdapter(
                this.getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public ArrayList<Integer> getFavoritesIdsArrayList(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString("favorite_movie_ids", null);
        Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
        return gson.fromJson(json, type);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int navigation_item_id = menuItem.getItemId();
        if (navigation_item_id == R.id.nav_home) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (navigation_item_id == R.id.nav_favorites) {
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }
}
