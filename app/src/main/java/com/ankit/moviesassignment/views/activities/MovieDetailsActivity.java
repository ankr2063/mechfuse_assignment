package com.ankit.moviesassignment.views.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ankit.moviesassignment.model.classmodel.MovieDetails;
import com.ankit.moviesassignment.R;
import com.ankit.moviesassignment.network.RetrofitRestRepository;
import com.ankit.moviesassignment.utils.Constants;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.ankit.moviesassignment.MyApplication.language_code;

public class MovieDetailsActivity extends AppCompatActivity {

    CompositeSubscription compositeSubscription;
    Intent intent;
    Integer id, intent_id;
    TextView name, avg, overview;
    ImageView poster, share, fav;
    LinearLayout genre_layout;
    String genres_st="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        intent = getIntent();
        id = intent.getIntExtra("id", 0);
        intent_id = intent.getIntExtra("intent_id", 1);

        name = findViewById(R.id.name);
        avg = findViewById(R.id.avg);
        overview = findViewById(R.id.overview);
        poster = findViewById(R.id.poster);
        share = findViewById(R.id.share);
        fav = findViewById(R.id.fav);
        genre_layout = findViewById(R.id.genre_layout);

        getMoviedetails();
    }

    protected void getMoviedetails(){
        compositeSubscription = new CompositeSubscription();
        compositeSubscription.add(RetrofitRestRepository.getRetrofit().getMovieDetails(id, Constants.api_key, language_code)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleOpenIssuesResponse,this::handleOpenIssuesResponseError));
    }

    protected void handleOpenIssuesResponse(MovieDetails movieDetails){
        name.setText(movieDetails.getTitle());
        avg.setText(movieDetails.getVote_average()+"");
        overview.setText(movieDetails.getOverview());
        Picasso.with(this).load("https://image.tmdb.org/t/p/original"+movieDetails.getPoster_path()).fit().centerCrop().into(poster);

        if(MainActivity.favorite_movie_ids.contains(id)){
            fav.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_favorite_red));
        }else {
            fav.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
        }

        fav.setOnClickListener(v -> {
            if (MainActivity.favorite_movie_ids.contains(id)) {
                fav.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                MainActivity.favorite_movie_ids.remove(new Integer(id));
            } else {
                fav.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_favorite_red));
                MainActivity.favorite_movie_ids.add(id);
            }
        saveFavoritesIdsArrayList(MainActivity.favorite_movie_ids);
        });

        setGenres(movieDetails.getGenres());

        share.setOnClickListener(v -> {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/html");
            String shareBody = "Movie name: "+movieDetails.getTitle()+"\n"+"Rating: "+movieDetails.getVote_average()+"\n"+genres_st;
            if(movieDetails.getPoster_path()!= null) {
                String img_url = "https://image.tmdb.org/t/p/original"+movieDetails.getPoster_path();
                String textToShare = "Click "+img_url+" to view poster.";
                shareBody = shareBody + "\n Image Link - " + textToShare;
            }
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        });
    }

    protected void setGenres(ArrayList<MovieDetails.Genre> genres ){
        int i;

        for (i = 0; i< genres.size(); i++){
            MovieDetails.Genre genre = genres.get(i);
            TextView textView = new TextView(genre_layout.getContext());
            textView.setText(genre.getName());
            if(i==0){
                genres_st = genre.getName();
            }else {
                genres_st = genres_st + ", " + genre.getName();
            }
            textView.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            textView.setGravity(Gravity.CENTER);
            textView.setId(i);
            textView.setBackground(this.getResources().getDrawable(R.drawable.round_rectangle_gray));
            textView.setTextColor(this.getResources().getColor(R.color.white));
            textView.setLayoutDirection(convertDpToPixels(15));
            textView.setPadding(convertDpToPixels(10), convertDpToPixels(4), convertDpToPixels(10), convertDpToPixels(4));
            genre_layout.addView(textView);
        }
    }

    public int convertDpToPixels(int dp){
        return (int) (dp * getApplicationContext().getResources().getDisplayMetrics().density);
    }

    protected void handleOpenIssuesResponseError(Throwable error){
        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();

    }

    public void saveFavoritesIdsArrayList(ArrayList<Integer> list){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("favorite_movie_ids", json);
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        if(intent_id == 1){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("main_id", 1);
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
        }
    }
}
