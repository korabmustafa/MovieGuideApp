package com.arb.movieguideapp.clients;

import com.arb.movieguideapp.wrappers.CreditsWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetCreditsDataService {
    @GET("movie/{id}/credits?api_key=81229d17288cd3c3a979724d4d5c9cae&language=en-US")
    Call<CreditsWrapper> getCast(@Path("id") int movieId);

    @GET("movie/{id}/credits?api_key=81229d17288cd3c3a979724d4d5c9cae&language=en-US")
    Call<CreditsWrapper> getCrew(@Path("id") int movieId);
}
