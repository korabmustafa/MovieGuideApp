package com.arb.movieguideapp.clients;

import com.arb.movieguideapp.wrappers.MovieWrapper;
import com.arb.movieguideapp.wrappers.SlideWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetMovieDataService {

    @GET("movie/popular?api_key=81229d17288cd3c3a979724d4d5c9cae&language=en-US&page=1&region=US")
    Call<MovieWrapper> getPopular();

    @GET("movie/now_playing?api_key=81229d17288cd3c3a979724d4d5c9cae&language=en-US&page=1&region=us")
    Call<MovieWrapper> getNowPlaying();

    @GET("movie/top_rated?api_key=81229d17288cd3c3a979724d4d5c9cae&language=en-US&page=1&region=US")
    Call<MovieWrapper> getTopRated();

    @GET("trending/movie/day?api_key=81229d17288cd3c3a979724d4d5c9cae")
    Call<SlideWrapper> getTrendingSlides();

    @GET("movie/upcoming?api_key=81229d17288cd3c3a979724d4d5c9cae&language=en-US&page=1&region=us")
    Call<MovieWrapper> getUpcoming();

    @GET("search/movie?api_key=81229d17288cd3c3a979724d4d5c9cae")
    Call<MovieWrapper> getMovie(@Query("query") String movie_name);

    @GET("movie/{id}/similar?api_key=81229d17288cd3c3a979724d4d5c9cae&language=en-US&page=1")
    Call<MovieWrapper> getSimilarMovie(@Path("id") int movieId);
}
