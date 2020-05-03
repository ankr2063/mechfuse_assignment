package com.ankit.moviesassignment.views.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ankit.moviesassignment.views.interfaces.ItemClickListener;
import com.ankit.moviesassignment.model.classmodel.MovieListData;
import com.ankit.moviesassignment.R;
import com.ankit.moviesassignment.views.activities.FavoritesActivity;
import com.ankit.moviesassignment.views.activities.MainActivity;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.MyViewHolder> {


    private ArrayList<MovieListData> movies;
    private Context context;
    private ItemClickListener clickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView movie_name, release_date;
        ImageView movie_poster, like;

        public MyViewHolder(View view) {
            super(view);
            this.movie_name = view.findViewById(R.id.moviename);
            this.movie_poster = view.findViewById(R.id.poster_image);
            this.release_date = view.findViewById(R.id.date);
            this.like = view.findViewById(R.id.like);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(FavoritesActivity itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public FavoritesAdapter(ArrayList<MovieListData> movies) {
        this.movies = movies;
    }

    @Override
    public FavoritesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_movies, parent, false);

        context = itemView.getContext();

        return new FavoritesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FavoritesAdapter.MyViewHolder holder, int position) {
        MovieListData movie = movies.get(position);
        holder.movie_name.setText(movie.getTitle());
        holder.release_date.setText(context.getResources().getString(R.string.Release_date)+movie.getRelease_date());
        Picasso.with(context).load("https://image.tmdb.org/t/p/original"+movie.getPoster_path()).fit().centerCrop().into(holder.movie_poster);
        holder.like.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_delete_black_24dp));
        holder.like.setOnClickListener(v -> {
            FavoritesActivity.favorites.remove(movie);
            MainActivity.favorite_movie_ids.remove(new Integer(movie.getId()));
            FavoritesActivity.recyclerView_favorites.getAdapter().notifyDataSetChanged();
            saveFavoritesIdsArrayList(MainActivity.favorite_movie_ids);
        });
    }

    public void saveFavoritesIdsArrayList(ArrayList<Integer> list){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("favorite_movie_ids", json);
        editor.apply();
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}