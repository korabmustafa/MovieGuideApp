package com.arb.movieguideapp.clients;

import com.arb.movieguideapp.wrappers.GenreWrapper;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetGenreDataService {
    @GET("genre/movie/list?api_key=81229d17288cd3c3a979724d4d5c9cae&language=en-US")
    Call<GenreWrapper> getGenre();
}
