package com.ankit.moviesassignment.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ankit.moviesassignment.model.responses.APIResponse;
import com.ankit.moviesassignment.views.interfaces.ItemClickListener;
import com.ankit.moviesassignment.views.activities.MovieDetailsActivity;
import com.ankit.moviesassignment.model.classmodel.MovieListData;
import com.ankit.moviesassignment.views.adapters.NowPlayingAdapter;
import com.ankit.moviesassignment.R;
import com.ankit.moviesassignment.network.RetrofitRestRepository;
import com.ankit.moviesassignment.utils.Constants;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.ankit.moviesassignment.MyApplication.language_code;

public class NowPlayingFragment extends Fragment implements ItemClickListener {

    CompositeSubscription compositeSubscription;
    RecyclerView recyclerView;
    NowPlayingAdapter itemsAdapter;
    public static ArrayList<MovieListData> now_playing_movies = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_items, container, false);

        itemsAdapter = new NowPlayingAdapter(now_playing_movies);
        recyclerView = view.findViewById(R.id.rview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemsAdapter);
        itemsAdapter.setClickListener(this);

        getNowPlayingMovies();
        return view;
    }

    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent(view.getContext(), MovieDetailsActivity.class);
        intent.putExtra("id", now_playing_movies.get(position).getId());
        intent.putExtra("intent_id", 1);
        startActivity(intent);
    }

    protected void getNowPlayingMovies(){
        compositeSubscription = new CompositeSubscription();
        compositeSubscription.add(RetrofitRestRepository.getRetrofit().getNowPlaying(Constants.api_key, language_code)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleOpenIssuesResponse,this::handleOpenIssuesResponseError));
    }

    protected void handleOpenIssuesResponse(APIResponse apiResponse){
        now_playing_movies.clear();
        if(apiResponse.getResults().size()>0){
            now_playing_movies.addAll(apiResponse.getResults());
            itemsAdapter.notifyDataSetChanged();
        }
    }

    protected void handleOpenIssuesResponseError(Throwable error){
        Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();

    }
}
