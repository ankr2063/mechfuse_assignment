package com.ankit.moviesassignment.model.responses;

import com.ankit.moviesassignment.model.classmodel.MovieListData;

import java.util.ArrayList;

public class APIResponse {

    public ArrayList<MovieListData> results;

    public ArrayList<MovieListData> getResults() {
        return results;
    }

    public void setResults(ArrayList<MovieListData> results) {
        this.results = results;
    }
}
