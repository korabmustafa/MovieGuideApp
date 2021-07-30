package com.arb.movieguideapp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arb.movieguideapp.R;
import com.arb.movieguideapp.clients.GetGenreDataService;
import com.arb.movieguideapp.clients.GetMovieDataService;
import com.arb.movieguideapp.listeners.MovieClickListener;
import com.arb.movieguideapp.models.Genre;
import com.arb.movieguideapp.models.Movie;
import com.arb.movieguideapp.wrappers.GenreWrapper;
import com.arb.movieguideapp.wrappers.MovieWrapper;
import com.arb.movieguideapp.ui.activity.MovieDetailActivity;
import com.arb.movieguideapp.adapters.SearchMovieAdapter;
import com.arb.movieguideapp.utils.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener {

    private RecyclerView searchRecyclerView;
    private SearchMovieAdapter movieAdapter;
    private List<Genre> genreList = new ArrayList<>();

    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchView = view.findViewById(R.id.search_movie);
        searchView.setOnQueryTextListener(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMovies();
    }

    private void getMovieGenres() {
        try {
            GetGenreDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetGenreDataService.class);
            Call<GenreWrapper> call = service.getGenre();
            call.enqueue(new Callback<GenreWrapper>() {
                @Override
                public void onResponse(Call<GenreWrapper> call, Response<GenreWrapper> response) {
                    if (response.body() != null) {
                        genreList = response.body().getGenre();
                    } else {
                        showError();
                    }
                }

                @Override
                public void onFailure(Call<GenreWrapper> call, Throwable t) {
                    showError();
                }
            });
        }
        catch (Exception e) {
            showError();
        }
    }

    private void populateMovies(List<Movie> movieList){
        movieAdapter = new SearchMovieAdapter(movieList, new MovieClickListener() {
            @Override
            public void onMovieClick(Movie movie) {
                vibratePhoneOnClick(getContext(), (short) 50);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("movie", movie);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void getMovies() {
        searchRecyclerView = getActivity().findViewById(R.id.movie_search_recycler);
        searchRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        searchRecyclerView.setItemAnimator(new DefaultItemAnimator());

        getMovieGenres();

        String input_movie = searchView.getQuery().toString();

        GetMovieDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetMovieDataService.class);
        Call<MovieWrapper> callMovieSearch = service.getMovie(input_movie);
        Call<MovieWrapper> callUpcomingMovies = service.getUpcoming();

        getMovie(searchRecyclerView, callMovieSearch);
        getMovie(searchRecyclerView, callUpcomingMovies);
    }

    private void getMovie(final RecyclerView recyclerView, Call<MovieWrapper> call) {
        try {
            call.enqueue(new Callback<MovieWrapper>() {
                @Override
                public void onResponse(Call<MovieWrapper> call, Response<MovieWrapper> response) {
                    if (response.body() != null) {
                        List<Movie> movieList = response.body().getMovies();

                        for (Movie m : movieList)
                            m.mapGenres(genreList);

                        populateMovies(movieList);

                        recyclerView.setAdapter(movieAdapter);
                        recyclerView.smoothScrollToPosition(0);
                    }
                }

                @Override
                public void onFailure(Call<MovieWrapper> call, Throwable t) {
                    showError();
                }
            });
        }
        catch (Exception e) {
            showError();
        }
    }

    private void showError() {
        Toast.makeText(getActivity(), "Please check your internet connection!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        getMovies();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void vibratePhoneOnClick(Context context, short vibrateMilliSeconds) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(vibrateMilliSeconds);
    }
}
