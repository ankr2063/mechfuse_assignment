package com.ankit.moviesassignment.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ankit.moviesassignment.views.adapters.FavoritesAdapter;
import com.ankit.moviesassignment.views.interfaces.ItemClickListener;
import com.ankit.moviesassignment.model.classmodel.MovieListData;
import com.ankit.moviesassignment.R;
import com.ankit.moviesassignment.views.fragments.NowPlayingFragment;
import com.ankit.moviesassignment.views.fragments.UpcomingFragment;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity implements ItemClickListener {

    public static RecyclerView recyclerView_favorites;
    FavoritesAdapter itemsAdapter;
    TextView no_items;
    public static ArrayList<MovieListData> favorites = new ArrayList<>();
    ArrayList<MovieListData> all_movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_favorites);

        no_items = findViewById(R.id.text_no_items);
        recyclerView_favorites = findViewById(R.id.rview);

        all_movies.clear();
        favorites.clear();
        all_movies.addAll(UpcomingFragment.upcoming_movies);
        all_movies.addAll(NowPlayingFragment.now_playing_movies);

        for(int i = 0; i<all_movies.size(); i++){
            if(MainActivity.favorite_movie_ids.contains(all_movies.get(i).getId())){
                if(!favorites.contains(all_movies.get(i))) {
                    favorites.add(all_movies.get(i));
                }
            }
        }

        if(favorites.size()==0){
            no_items.setVisibility(View.VISIBLE);
        }else {
            no_items.setVisibility(View.GONE);
            itemsAdapter = new FavoritesAdapter(favorites);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            recyclerView_favorites.setLayoutManager(mLayoutManager);
            recyclerView_favorites.setItemAnimator(new DefaultItemAnimator());
            recyclerView_favorites.setAdapter(itemsAdapter);
            itemsAdapter.setClickListener(this);
        }
    }

    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent(view.getContext(), MovieDetailsActivity.class);
        intent.putExtra("id", favorites.get(position).getId());
        intent.putExtra("intent_id", 3);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("main_id", 1);
        startActivity(intent);
    }

}
