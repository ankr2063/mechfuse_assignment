package com.ankit.moviesassignment.network;

import com.ankit.moviesassignment.model.responses.APIResponse;
import com.ankit.moviesassignment.model.classmodel.MovieDetails;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface RetrofitInterface {

    @GET("upcoming")
    Observable<APIResponse> getUpcoming(@Query("api_key") String api_key, @Query("language") String language);

    @GET("now_playing")
    Observable<APIResponse> getNowPlaying(@Query("api_key") String api_key, @Query("language") String language);

    @GET("{movie_id}")
    Observable<MovieDetails> getMovieDetails(@Path("movie_id") Integer movie_id, @Query("api_key") String api_key, @Query("language") String language);

}
