package com.ankit.moviesassignment.model.classmodel;

import java.util.ArrayList;

public class MovieDetails {

    private String title;
    private String overview;
    private String poster_path;
    private Double vote_average;
    private String release_date;
    private ArrayList<Genre> genres;

    public class Genre{
        private int id;
        private String name;

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    public void setVote_average(Double vote_average) {
        this.vote_average = vote_average;
    }
}
